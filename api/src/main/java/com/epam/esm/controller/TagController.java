package com.epam.esm.controller;

import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.repository.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class {@code TagController} is an endpoint of the API which allows to perform CRD operations on tags.
 * Annotated by {@link RestController} with no parameters to provide an answer in application/json.
 * Annotated by {@link RequestMapping} with parameter value = "/tags".
 * So that {@code TagController} is accessed by sending request to /tags.
 *
 * @author Dmitry Poliukov
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final HateoasAdder<TagDto> tagHateoasAdder;

    public TagController(TagService tagService, HateoasAdder<TagDto> tagHateoasAdder) {
        this.tagService = tagService;
        this.tagHateoasAdder = tagHateoasAdder;
    }

    /**
     * Method for getting tag by ID.
     *
     * @param id ID of tag to get
     * @return Found tag entity with hateoas
     */
    @GetMapping("/{id}")
    public TagDto readTag(@PathVariable int id) {
        TagDto tag = tagService.read(id);
        tagHateoasAdder.addLinks(tag);
        return tag;
    }

    /**
     * Method for getting list of all tags.
     *
     * @param page   the number of page for pagination
     * @param size   the size of page for pagination
     * @return List of found tags with hateoas
     */
    @GetMapping
    public List<TagDto> readTags (@RequestParam(value = "page", defaultValue = "1", required = false) @Min(1) int page,
                                @RequestParam(value = "size", defaultValue = "5", required = false) @Min(1) int size) {
        return tagService.readAll(page, size).stream()
                .peek(tagHateoasAdder::addLinks)
                .collect(Collectors.toList());
    }

    /**
     * Method for saving new tag.
     *
     * @param tagDto tag for saving
     * @return created tag with hateoas
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(@RequestBody @Valid TagDto tagDto) {
        TagDto addedTag = tagService.create(tagDto);
        tagHateoasAdder.addLinks(addedTag);
        return addedTag;
    }

    /**
     * Method for removing tag by ID.
     *
     * @param id ID of tag to remove
     * @return NO_CONTENT HttpStatus
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  ResponseEntity<Void> deleteTag(@PathVariable int id) {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Method for getting most popular tag for user with the highest cost of all orders.
     *
     * @return Found tag entity with hateoas
     */
    @GetMapping("/most-popular-tag")
    public TagDto readMostWidelyTagFromUserWithHighestCostOrders() {
        TagDto tag = tagService.readMostWidelyTagFromUserWithHighestCostOrders();
        tagHateoasAdder.addLinks(tag);
        return tag;
    }

}

