package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.repository.dto.UserDto;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoasAdder implements HateoasAdder<UserDto> {

    private static final Class<UserController> CONTROLLER = UserController.class;

    @Override
    public void addLinks(UserDto userDto) {
        userDto.add(linkTo(methodOn(CONTROLLER).read(userDto.getId())).withSelfRel());

    }
}
