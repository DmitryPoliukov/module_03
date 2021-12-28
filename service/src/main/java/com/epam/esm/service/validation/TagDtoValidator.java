package com.epam.esm.service.validation;

import com.epam.esm.repository.dto.TagDto;
import com.epam.esm.service.exception.IncorrectParameterException;

public class TagDtoValidator {

    private static final int MAX_LENGTH_NAME = 20;
    private static final int MIN_LENGTH_NAME = 3;

    public static void validate(TagDto tag) throws IncorrectParameterException {
        validateName(tag.getName());
    }

    public static void validateName(String name) throws IncorrectParameterException {
        if (name.length() < MIN_LENGTH_NAME || name.length() > MAX_LENGTH_NAME) {
            throw new IncorrectParameterException("Incorrect tag name");
        }
    }
}
