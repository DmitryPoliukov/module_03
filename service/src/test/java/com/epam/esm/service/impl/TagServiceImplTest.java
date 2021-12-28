package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.CertificateDao;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ResourceNotFoundException;
import com.epam.esm.service.exception.ResourceValidationException;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

    public static final int TAG_ID = 1;
    public static final int ONE_DELETED_ROW = 1;
    public static final int NO_DELETED_ROW = 0;
    TagDao tagDao = mock(TagDao.class);
    CertificateDao certificateDao = mock(CertificateDao.class);
    TagService tagService =
            new TagServiceImpl(tagDao, certificateDao);

    @Test
    void createTagDaoReadInvocation() {
        Tag tag = new Tag(TAG_ID, "firstTag");
        when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

        tagService.create(tag.toDto());

        verify(tagDao).read(tag.getName());
    }

    @Test
    void createIfExistedTagDaoCreateInvocation() {
        Tag tag = new Tag(TAG_ID, "firstTag");
        when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

        tagService.create(tag.toDto());

        verify(tagDao, never()).create(any());
    }

    @Test
    void createIfExistedTagDaoReadInvocation() {
        Tag tag = new Tag(TAG_ID, "firstTag");
        when(tagDao.read(tag.getName())).thenReturn(Optional.of(tag));

        tagService.create(tag.toDto());

        verify(tagDao).read(tag.getName());
    }


    @Test
    void read() {
        Tag tag = new Tag(TAG_ID, "firstTag");
        when(tagDao.read(anyInt())).thenReturn(Optional.of(tag));

        tagService.read(TAG_ID);

        verify(tagDao).read(anyInt());
    }

    @Test
    void readException() {
        assertThrows(ResourceNotFoundException.class, () -> tagService.read(TAG_ID));
    }

    @Test
    void readAll() {
        tagService.readAll();

        verify(tagDao).readAll();
    }

    @Test
    void deleteCertificateDaoDeleteCertificateTagsByTagIdInvocation() {
        when(tagDao.delete(anyInt())).thenReturn(ONE_DELETED_ROW);

        tagService.delete(TAG_ID);

        verify(certificateDao).deleteBondingTagsByTagId(TAG_ID);
    }

    @Test
    void deleteTagDaoDeleteInvocation() {
        when(tagDao.delete(anyInt())).thenReturn(ONE_DELETED_ROW);

        tagService.delete(TAG_ID);

        verify(tagDao).delete(TAG_ID);
    }

    @Test
    void deleteTagDaoDeleteException() {
        when(tagDao.delete(anyInt())).thenReturn(NO_DELETED_ROW);

        assertThrows(ResourceValidationException.class, () -> tagService.delete(TAG_ID));
    }


}