package com.epam.esm.repository.dao;

import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface CertificateDao {

    /**
     * Create certificate.
     *
     * @param certificate the certificate
     * @return the certificate
     */
    Certificate create(Certificate certificate);

    /**
     * Read optional.
     *
     * @param certificateId the certificateId
     * @return the optional
     */
    Optional<Certificate> read(int certificateId);

    /**
     * Readl all certificates
     *
     * @return list of certificates
     */
    List<Certificate> readAll();


    List<Certificate> readCertificateWithParams(String tagName, String descriptionOrNamePart,
                                                String sortParameters, boolean ascending);

    /**
     * Update int.
     *
     * @param certificate the certificate
     * @return the int
     */
    void update(Certificate certificate);

    /**
     * Read certificate tags list.
     *
     * @param certificateId the certificate id
     * @return the list
     */
    List<Tag> readCertificateTags(int certificateId);

    /**
     * Add tag.
     *
     * @param tagId the tag id
     * @param certificateId the certificate id
     */
    void addTag(int tagId, int certificateId);

    /**
     * Remove tag int.
     *
     * @param tagId the tag id
     * @param certificateId the certificate id
     * @return the int
     */
    int removeTag(int tagId, int certificateId);

    /**
     * Delete certificate by certificate id.
     *
     * @param certificateId the certificate id
     * @return the int
     */
    int delete(int certificateId);

    /**
     * Delete certificate tags by tag id.
     *
     * @param tagId the tag id
     */
    int deleteBondingTagsByTagId(int tagId);

    /**
     * Delete certificate tags by certificate id.
     *
     * @param certificateId the certificate id
     */
    int deleteBondingTagsByCertificateId(int certificateId);

    /**
     * Update patch int.
     *
     * @param certificate the certificate
     * @return the int
     */












}
