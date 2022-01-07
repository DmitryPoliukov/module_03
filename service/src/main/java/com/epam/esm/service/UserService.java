package com.epam.esm.service;

import com.epam.esm.repository.dto.TagDto;
import com.epam.esm.repository.dto.UserDto;
import com.epam.esm.repository.entity.PaginationParameter;

import java.util.List;

public interface UserService {

    UserDto read(int id);

    List<UserDto> readAll(int page, int size);

    TagDto takeMostWidelyTagFromUserWithHighestCostOrders();
}
