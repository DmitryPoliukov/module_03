package com.epam.esm.controller;

import com.epam.esm.repository.dto.CertificateDto;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/certificates")

public class CertificateController {

    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> readCertificate(@PathVariable int id) {
        CertificateDto certificate = certificateService.read(id);
        return ResponseEntity.status(HttpStatus.OK).body(certificate);
    }

    @GetMapping
    public List<CertificateDto> readCertificates() {
        return certificateService.readAll();
    }

    @GetMapping("/search")
    public List<CertificateDto> readCertificateWithParams(@RequestParam(required = false) String tagName, @RequestParam(required = false) String descriptionOrNamePart,
                                                          @RequestParam(required = false) String sortParameter, @RequestParam(required = false) boolean ascending) {
        return certificateService.readCertificateWithParams(tagName, descriptionOrNamePart, sortParameter, ascending);

    }

    @PostMapping
    public ResponseEntity<CertificateDto> createCertificate(
            @Valid @RequestBody CertificateDto certificateDto) {

        CertificateDto createdCertificate = certificateService.create(certificateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCertificate);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateCertificate(@PathVariable int id,
                                                        @RequestBody CertificateDto certificateDto) {
        certificateService.update(id, certificateDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificate(@PathVariable int id) {
        certificateService.delete(id);
    }
}
