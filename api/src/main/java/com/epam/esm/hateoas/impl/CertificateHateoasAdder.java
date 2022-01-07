package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.repository.dto.CertificateDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CertificateHateoasAdder implements HateoasAdder<CertificateDto> {
    private static final Class<CertificateController> CERTIFICATE_CONTROLLER = CertificateController.class;
    private static final Class<TagController> TAG_CONTROLLER = TagController.class;

    @Override
    public void addLinks(CertificateDto certificateDto) {
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER)
                .readCertificate(certificateDto.getId())).withSelfRel());
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER)
                .updateCertificate(certificateDto.getId(), certificateDto)).withRel("update"));
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER)
                .deleteCertificate(certificateDto.getId())).withRel("delete"));
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).createCertificate(certificateDto)).withRel("new"));
        certificateDto.getTagsDto().forEach(
                tagDto -> tagDto.add(linkTo(methodOn(TAG_CONTROLLER).readTag(tagDto.getId())).withSelfRel()));

    }
}
