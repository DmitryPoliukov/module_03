package com.epam.esm.service.impl;


import com.epam.esm.repository.dao.CertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.dto.TagDto;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.IncorrectParameterException;
import com.epam.esm.service.exception.ResourceException;
import com.epam.esm.service.validation.TagDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    public static final int ONE_UPDATED_ROW = 1;

    private final TagDao tagDao;
    private final CertificateDao certificateDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao, CertificateDao certificateDao) {
        this.tagDao = tagDao;
        this.certificateDao = certificateDao;
    }

    @Override
    public TagDto create(TagDto inputTag) {
        TagDtoValidator.validate(inputTag);
        Optional<Tag> existingTag = tagDao.read(inputTag.getName());
        return existingTag.orElseGet(() -> tagDao.create(inputTag.toEntity())).toDto();
    }

    @Override
    public TagDto read(int id) {
        Optional<Tag> tag = tagDao.read(id);

        return tag.orElseThrow(ResourceException.notFoundWithTagId(id)).toDto();
    }

    @Override
    public List<TagDto> readAll() {
        List<Tag> entityList = tagDao.readAll();
        return entityList.stream()
                .map(Tag::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Override
    public void delete(int id) {
        certificateDao.deleteBondingTagsByTagId(id);
        int numberOfUpdatedRows = tagDao.delete(id);
        if (numberOfUpdatedRows != ONE_UPDATED_ROW) {
            throw ResourceException.validationWithTagId(id).get();
        }

    }

}
