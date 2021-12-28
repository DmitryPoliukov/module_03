package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.CertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.dto.CertificateDto;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.ResourceException;
import com.epam.esm.service.validation.CertificateDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
public class CertificateServiceImpl implements CertificateService {

    public static final int ONE_UPDATED_ROW = 1;
    private final CertificateDao certificateDao;
    private final TagDao tagDao;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    @Override
    public CertificateDto create(CertificateDto certificateDto) {
        CertificateDtoValidator.validate(certificateDto);
        LocalDateTime timeNow = now();
        certificateDto.setCreateDate(timeNow);
        certificateDto.setLastUpdateDate(timeNow);
        Certificate createdCertificate = certificateDao.create(certificateDto.toEntity());
        createdCertificate.setTags(certificateDto.getTags());
        addTagsToDb(createdCertificate);
        return createdCertificate.toDto();
    }


    @Override
    public List<CertificateDto> readAll() {
        List<Certificate> certificates = certificateDao.readAll();
        for (Certificate certificate : certificates) {
            certificate.setTags(certificateDao.readCertificateTags(certificate.getId()));
        }
        return certificates.stream()
                .map(Certificate::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CertificateDto read(int id) {
        Optional<Certificate> certificate = certificateDao.read(id);
        certificate.ifPresent(
                actualCertificate -> actualCertificate.setTags(certificateDao.readCertificateTags(id)));
        return certificate.orElseThrow(ResourceException.notFoundWithCertificateId(id)).toDto();
    }

    @Override
    public void update(int id, CertificateDto certificateDto) {
        certificateDto.setId(id);
        CertificateDto actualCertificateDto = fillingFields(certificateDto);
        actualCertificateDto.setLastUpdateDate(LocalDateTime.now());
        List<Tag> requestTags = actualCertificateDto.getTags();
        List<Tag> createdTags = tagDao.readAll();
        saveNewTags(requestTags, createdTags);
        certificateDao.update(certificateDto.toEntity());
    }

    private CertificateDto fillingFields(CertificateDto certificateDto) {
        CertificateDto oldCertificate = certificateDao.read(certificateDto.getId())
                .orElseThrow(ResourceException.notFoundWithCertificateId(certificateDto.getId())).toDto();

        if (certificateDto.getName() == null) {
            certificateDto.setName(oldCertificate.getName());
        }
        if (certificateDto.getDescription() == null) {
            certificateDto.setDescription(oldCertificate.getDescription());
        }
        if (certificateDto.getDuration() == null) {
            certificateDto.setDuration(oldCertificate.getDuration());
        }
        if (certificateDto.getPrice() == null) {
            certificateDto.setPrice(oldCertificate.getPrice());
        }
        if (certificateDto.getTags() == null) {
            certificateDto.setTags(oldCertificate.getTags());
        }
            certificateDto.setCreateDate(oldCertificate.getCreateDate());

        return certificateDto;

    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Override
    public void delete(int id){
        certificateDao.deleteBondingTagsByCertificateId(id);
        int numberOfUpdatedRows = certificateDao.delete(id);
        if (numberOfUpdatedRows != ONE_UPDATED_ROW) {
            throw ResourceException.validationWithCertificateId(id).get();
        }
    }

    @Override
    public List<CertificateDto> readCertificateWithParams(String tagName, String descriptionOrNamePart,
                                                          String sortParameter, boolean ascending) {
        List<Certificate> certificates = certificateDao.readCertificateWithParams(tagName, descriptionOrNamePart,
                sortParameter, ascending);
        for (Certificate certificate : certificates) {
            certificate.setTags(certificateDao.readCertificateTags(certificate.getId()));
        }
        return certificates.stream()
                .map(Certificate::toDto)
                .collect(Collectors.toList());
    }

    public void addTagsToDb(Certificate certificate) {
        List<Tag> tags = certificate.getTags();
        if (tags != null) {
            for (Tag tag : tags) {
                Optional<Tag> existedTag = tagDao.read(tag.getName());
                int tagId = existedTag.map(Tag::getId).orElseGet(() -> tagDao.create(tag).getId());
                certificateDao.addTag(tagId, certificate.getId());
            }
        }
    }

    private void saveNewTags(List<Tag> requestTags, List<Tag> createdTags) {
        if (requestTags == null) {
            return;
        }
        for (Tag requestTag : requestTags) {
            boolean isExist = false;
            for (Tag createdTag : createdTags) {
                if (Objects.equals(requestTag.getName(), createdTag.getName())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                tagDao.create(requestTag);
            }
        }
    }
}
