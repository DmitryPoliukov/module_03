package com.epam.esm.service;


import com.epam.esm.repository.dto.TagDto;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.exception.IncorrectParameterException;
import com.epam.esm.service.exception.ResourceNotFoundException;

import java.util.List;

public interface TagService {

    /**
     * Create tag.
     *
     * @param tag the tag
     * @return the tag
     */
    TagDto create(TagDto tag);

    /**
     * Read tag.
     *
     * @param id the id
     * @return the tag
     */
    TagDto read(int id);

    /**
     * Read all list.
     *
     * @return the list
     */
    List<TagDto> readAll();

    /**
     * Delete.
     *
     * @param id the id
     */
    void delete(int id);

}
