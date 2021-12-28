package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.CertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.exception.ResourceNotFoundException;
import com.epam.esm.service.exception.ResourceValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CertificateServiceImplTest {

    public static final int ID = 1;
    public static final int NO_UPDATED_ROWS = 0;
    public static final int ONE_UPDATED_ROW = 1;

    TagDao tagDao = mock(TagDao.class);
    CertificateDao certificateDao = mock(CertificateDao.class);
    CertificateServiceImpl certificateService = new CertificateServiceImpl(certificateDao, tagDao);

    @Test
    void createWithTagsExistedTagDaoReadInvocation() {
        Certificate certificate = givenCertificate2();
        Certificate certificateOutput = givenCertificate2();
        certificateOutput.setTags(null);
        Tag tag = givenTag();
        when(certificateDao.create(any())).thenReturn(certificateOutput);
        when(tagDao.read(any())).thenReturn(Optional.of(tag));

        certificateService.create(certificate.toDto());

        verify(tagDao).read(any());
    }

    @Test
    void createWithTagsExistedTagDaoCreateInvocation() {
        Certificate certificate = givenCertificate2();
        Certificate certificateOutput = givenCertificate2();
        certificateOutput.setTags(null);
        Tag tag = givenTag();
        when(certificateDao.create(any())).thenReturn(certificateOutput);
        when(tagDao.read(any())).thenReturn(Optional.of(tag));

        certificateService.create(certificate.toDto());

        verify(tagDao, never()).create(any());
    }

    @Test
    void createWithTagsExistedCertificateDaoAddTagInvocation() {
        Certificate certificate = givenCertificate2();
        Certificate certificateOutput = givenCertificate2();
        certificateOutput.setTags(null);
        Tag tag = givenTag();
        when(certificateDao.create(any())).thenReturn(certificateOutput);
        when(tagDao.read(any())).thenReturn(Optional.of(tag));

        certificateService.create(certificate.toDto());

        verify(certificateDao).addTag(anyInt(), anyInt());
    }

    @Test
    void createWithTagsNotExistedTagDaoReadInvocation() {
        Certificate certificate = givenCertificate2();
        Certificate certificateOutput = givenCertificate2();
        certificateOutput.setTags(null);
        Tag tag = givenTag();
        when(certificateDao.create(any())).thenReturn(certificateOutput);
        when(tagDao.read(any())).thenReturn(Optional.empty());
        when(tagDao.create(any())).thenReturn(tag);

        certificateService.create(certificate.toDto());

        verify(tagDao).read(any());
    }

    @Test
    void createWithTagsNotExistedTagDaoCreateInvocation() {
        Certificate certificate = givenCertificate2();
        Certificate certificateOutput = givenCertificate2();
        certificateOutput.setTags(null);
        Tag tag = givenTag();
        when(certificateDao.create(any())).thenReturn(certificateOutput);
        when(tagDao.read(any())).thenReturn(Optional.empty());
        when(tagDao.create(any())).thenReturn(tag);

        certificateService.create(certificate.toDto());

        verify(tagDao).create(any());
    }

    @Test
    void createWithTagsNotExistedCertificateDaoAddTagInvocation() {
        Certificate certificate = givenCertificate2();
        Certificate certificateOutput = givenCertificate2();
        certificateOutput.setTags(null);
        Tag tag = givenTag();
        when(certificateDao.create(any())).thenReturn(certificateOutput);
        when(tagDao.read(any())).thenReturn(Optional.empty());
        when(tagDao.create(any())).thenReturn(tag);

        certificateService.create(certificate.toDto());

        verify(certificateDao).addTag(anyInt(), anyInt());
    }

    @Test
    void readExistedCertificateDaoReadInvocation() {
        Certificate certificate = givenCertificate1();
        when(certificateDao.read(anyInt())).thenReturn(Optional.of(certificate));

        certificateService.read(certificate.getId());

        verify(certificateDao).read(certificate.getId());
    }

    @Test
    void readExistedCertificateDaoReadCertificateTagsInvocation() {
        Certificate certificate = givenCertificate1();
        when(certificateDao.read(anyInt())).thenReturn(Optional.of(certificate));

        certificateService.read(certificate.getId());

        verify(certificateDao).readCertificateTags(certificate.getId());
    }

    @Test
    void readNotExistedException() {
        when(certificateDao.read(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> certificateService.read(ID));
    }

    @Test
    void readAllCertificateDaoReadAllInvocation() {
        Certificate certificate = givenCertificate1();
        when(certificateDao.readAll()).thenReturn(List.of(certificate));

        certificateService.readAll();

        verify(certificateDao).readAll();
    }

    @Test
    void deleteCertificateDaoDeleteCertificateTagsByCertificateIdInvocation() {
        when(certificateDao.delete(anyInt())).thenReturn(ONE_UPDATED_ROW);

        certificateService.delete(ID);

        verify(certificateDao).deleteBondingTagsByCertificateId(ID);
    }

    @Test
    void deleteCertificateDaoDeleteInvocation() {
        when(certificateDao.delete(anyInt())).thenReturn(ONE_UPDATED_ROW);

        certificateService.delete(ID);

        verify(certificateDao).delete(ID);
    }

    @Test
    void deleteCertificateDaoDeleteException() {
        when(certificateDao.delete(anyInt())).thenReturn(NO_UPDATED_ROWS);

        assertThrows(ResourceValidationException.class, () -> certificateService.delete(ID));
    }

    @Test
    void addTagsToDbWithTagTagDaoRead() {
        Certificate certificate = givenCertificate2();
        Tag tag = givenTag();
        when(tagDao.read(any())).thenReturn(Optional.of(tag));

        certificateService.addTagsToDb(certificate);

        verify(tagDao).read(any());
    }

    @Test
    void addTagsToDbWithExistedTagTagDaoCreate() {
        Certificate certificate = givenCertificate1();
        Tag tag = givenTag();
        when(tagDao.read(any())).thenReturn(Optional.of(tag));

        certificateService.addTagsToDb(certificate);

        verify(tagDao, never()).create(any());
    }

    @Test
    void addTagsToDbWithTagCertificateDaoAddTag() {
        Certificate certificate = givenCertificate2();
        Tag tag = givenTag();
        when(tagDao.read(any())).thenReturn(Optional.of(tag));

        certificateService.addTagsToDb(certificate);

        verify(certificateDao).addTag(anyInt(), anyInt());
    }

    @Test
    void addTagsToDbWithUnexistedTagTagDaoCreate() {
        Certificate certificate = givenCertificate2();
        Tag tag = givenTag();
        when(tagDao.read(any())).thenReturn(Optional.empty());
        when(tagDao.create(any())).thenReturn(tag);

        certificateService.addTagsToDb(certificate);

        verify(tagDao).create(any());
    }

    private static Certificate givenCertificate1() {
        return new Certificate(ID, "firstCertificate", "first description", 1.33,
                5, LocalDateTime.of(2021, 12, 25, 15, 0, 0),
                LocalDateTime.of(2020, 12, 30, 16, 30, 0), List.of(new Tag("tag1")));
    }

    private static Certificate givenCertificate2() {
        return new Certificate(2, "second certificate", "second description", 1.33,
                5, LocalDateTime.of(2021, 12, 25, 15, 0, 0),
                LocalDateTime.of(2020, 12, 30, 16, 30, 0), List.of(new Tag("tag2")));
    }

    private static Certificate givenCertificate3() {
        return new Certificate(3, "third Certificate", "third description", 1.33,
                5, LocalDateTime.of(2021, 12, 25, 15, 0, 0),
                LocalDateTime.of(2020, 12, 30, 16, 30, 0), List.of(new Tag("tag3")));
    }



    private static Tag givenTag() {
        return new Tag(1);
    }
}