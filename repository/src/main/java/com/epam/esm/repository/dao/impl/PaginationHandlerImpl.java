package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.dao.PaginationHandler;
import com.epam.esm.repository.entity.PaginationParameter;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;

@Component
public class PaginationHandlerImpl implements PaginationHandler {
    @Override
    public void setPageToQuery(TypedQuery<?> typedQuery, int page, int size) {
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
    }

    @Override
    public int calculateNumberOfPages(int numberOfElements, int pageSize) {
        return (int) Math.ceil(1.0 * numberOfElements / pageSize);    }
}
