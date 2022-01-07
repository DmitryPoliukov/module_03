package com.epam.esm.repository.dao;

import com.epam.esm.repository.entity.PaginationParameter;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User create(User user);

    Optional<User> read(int id);

    List<User> readAll(int page, int size);

    Tag takeMostWidelyTagFromUserWithHighestCostOrders();



    }
