package com.epam.esm.repository.dao;


import java.util.List;
import java.util.Optional;

public interface AbstractCRDRepository<T> {

    T create(T t);

    Optional<T> read(int id);

    List<T> readAll();

    int delete(int id);

}
