package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.repository.dto.TagDto;
import com.epam.esm.repository.dto.UserDto;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoasAdder implements HateoasAdder<UserDto> {

    private static final Class<UserController> USER_CONTROLLER = UserController.class;

    @Override
    public void addLinks(UserDto userDto) {
        userDto.add(linkTo(methodOn(USER_CONTROLLER).read(userDto.getId())).withSelfRel());
    }


}
