package com.epam.esm.controller;

import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.repository.dto.UserDto;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final HateoasAdder<UserDto> hateoasAdder;

    @Autowired
    public UserController(UserService userService, HateoasAdder<UserDto> hateoasAdder) {
        this.userService = userService;
        this.hateoasAdder = hateoasAdder;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto read(@PathVariable int id) {
        UserDto userDto = userService.read(id);
      hateoasAdder.addLinks(userDto);
        return userDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> readAll(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                  @RequestParam(value = "size", defaultValue = "5", required = false) int size) {
        return userService.readAll(page, size).stream()
                .peek(hateoasAdder::addLinks)
                .collect(Collectors.toList());
    }
}
