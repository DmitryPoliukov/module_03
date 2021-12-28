package com.epam.esm.service.validation;

import com.epam.esm.repository.dto.CertificateDto;
import com.epam.esm.repository.dto.TagDto;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.exception.IncorrectParameterException;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class CertificateDtoValidator {
    private static final int MAX_LENGTH_NAME = 45;
    private static final int MIN_LENGTH_NAME = 1;
    private static final int MAX_LENGTH_DESCRIPTION = 45;
    private static final int MAX_DURATION = 366;
    private static final int MIN_DURATION = 1;

    public static void validate(CertificateDto certificateDto) throws IncorrectParameterException {
        validateName(certificateDto.getName());
        validateDescription(certificateDto.getDescription());
        validatePrice(certificateDto.getPrice());
        validateDuration(certificateDto.getDuration());
        validateListOfTags(certificateDto.getTags().stream().map(Tag::toDto).collect(toList()));
    }

    public static void validateListOfTags(List<TagDto> tags) throws IncorrectParameterException {
        if (tags == null) return;
        for (TagDto tag : tags) {
            TagDtoValidator.validate(tag);
        }
    }

    private static void validateName(String name) throws IncorrectParameterException {
        if (name == null || name.length() < MIN_LENGTH_NAME || name.length() > MAX_LENGTH_NAME) {
            throw new IncorrectParameterException("Incorrect certificate name");
        }
    }

    private static void validateDescription(String description) throws IncorrectParameterException {
        if (description == null || description.length() > MAX_LENGTH_DESCRIPTION) {
            throw new IncorrectParameterException("Incorrect certificate description");
        }
    }

    private static void validatePrice(double price) throws IncorrectParameterException {
        if (price < 0) {
            throw new IncorrectParameterException("Incorrect certificate price");
        }
    }

    private static void validateDuration(int duration) throws IncorrectParameterException {
        if (duration < MIN_DURATION || duration > MAX_DURATION) {
            throw new IncorrectParameterException("Incorrect certificate duration");
        }
    }
}
