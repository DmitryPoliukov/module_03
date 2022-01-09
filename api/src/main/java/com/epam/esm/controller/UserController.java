package com.epam.esm.controller;

import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.repository.dto.TagDto;
import com.epam.esm.repository.dto.UserDto;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final HateoasAdder<UserDto> userHateoasAdder;
    private final HateoasAdder<TagDto> tagHateoasAdder;


    @Autowired
    public UserController(UserService userService, HateoasAdder<UserDto> hateoasAdder,
                          HateoasAdder<TagDto> tagHateoasAdder) {
        this.userService = userService;
        this.userHateoasAdder = hateoasAdder;
        this.tagHateoasAdder = tagHateoasAdder;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto read(@PathVariable int id) {
        UserDto userDto = userService.read(id);
      userHateoasAdder.addLinks(userDto);
        return userDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> readAll(@RequestParam(value = "page", defaultValue = "1", required = false) @Min(1) int page,
                                  @RequestParam(value = "size", defaultValue = "5", required = false) @Min(1) int size) {
        return userService.readAll(page, size).stream()
                .peek(userHateoasAdder::addLinks)
                .collect(Collectors.toList());
    }

    @GetMapping("/most-popular-tag")
    public ResponseEntity<TagDto> readMostWidelyTagFromUserWithHighestCostOrders() {
        TagDto tag = userService.readMostWidelyTagFromUserWithHighestCostOrders();
        tagHateoasAdder.addLinks(tag);
        return ResponseEntity.status(HttpStatus.OK).body(tag);
    }
}
