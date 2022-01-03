package com.epam.esm.repository.dao;


import java.util.List;
import java.util.Optional;

public abstract class AbstractCRDRepository<T> {

    abstract T create(T t);

    abstract Optional<T> read(int id);

    abstract List<T> readAll();

    abstract int delete(int id);

}
