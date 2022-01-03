package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.dto.TagDto;
import com.epam.esm.repository.dto.UserDto;
import com.epam.esm.repository.entity.PaginationParameter;
import com.epam.esm.repository.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDto read(int id) {
        Optional<User> user = userDao.read(id);
        return user.orElseThrow(ResourceNotFoundException.notFoundWithUser(id)).toDto();
    }

    @Override
    public List<UserDto> readAll(PaginationParameter parameter) {
        List<User> users = userDao.readAll(parameter);
        return users.stream()
                .map(User::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto takeMostWidelyTagFromUserWithHighestCostOrders() {
        return null;
    }
}
