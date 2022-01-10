package com.epam.esm.controller;

import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.repository.dto.TagDto;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final HateoasAdder<TagDto> tagHateoasAdder;

    public TagController(TagService tagService, HateoasAdder<TagDto> tagHateoasAdder) {
        this.tagService = tagService;
        this.tagHateoasAdder = tagHateoasAdder;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> readTag(@PathVariable int id) {
        TagDto tag = tagService.read(id);
        tagHateoasAdder.addLinks(tag);
        return ResponseEntity.status(HttpStatus.OK).body(tag);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> readTags (@RequestParam(value = "page", defaultValue = "1", required = false) @Min(1) int page,
                                @RequestParam(value = "size", defaultValue = "5", required = false) @Min(1) int size) {
        return tagService.readAll(page, size).stream()
                .peek(tagHateoasAdder::addLinks)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(@RequestBody @Valid TagDto tagDto) {
        TagDto addedTag = tagService.create(tagDto);
        tagHateoasAdder.addLinks(addedTag);
        return addedTag;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  ResponseEntity<Void> deleteTag(@PathVariable int id) {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

