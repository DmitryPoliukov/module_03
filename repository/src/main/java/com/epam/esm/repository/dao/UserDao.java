package com.epam.esm.repository.dao;

import com.epam.esm.repository.entity.PageData;
import com.epam.esm.repository.entity.PaginationParameter;
import com.epam.esm.repository.entity.User;

import java.util.Optional;

public interface UserDao {

    User create(User user);

    Optional<User> readWithoutOrders(int id);

    PageData<User> readAll(PaginationParameter parameter);



}
