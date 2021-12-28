package com.epam.esm.service;


import com.epam.esm.repository.dto.CertificateDto;
import com.epam.esm.service.exception.IncorrectParameterException;

import java.util.List;

public interface CertificateService {

    /**
     * Create certificate.
     *
     * @param certificate the certificate
     * @return the certificate
     */
    CertificateDto create(CertificateDto certificate);

    /**
     * Read all list.
     *
     * @return the list
     */
    List<CertificateDto> readAll();

    /**
     * Read certificate.
     *
     * @param id the id
     * @return the certificate
     */
    CertificateDto read(int id);


    /**
     * Update certificate
     *
     * @param id
     * @param certificateDto
     * @return
     */
    void update(int id, CertificateDto certificateDto);


    /**
     * Delete certificate.
     *
     * @param id
     */
    void delete(int id);

    List<CertificateDto> readCertificateWithParams(String tagName, String descriptionOrNamePart,
                                             String sortParameter, boolean ascending);
}
