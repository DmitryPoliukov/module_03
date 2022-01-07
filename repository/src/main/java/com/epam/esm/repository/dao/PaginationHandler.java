package com.epam.esm.repository.dao;

import com.epam.esm.repository.entity.PaginationParameter;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;

@Component
public interface PaginationHandler {

    void setPageToQuery(TypedQuery<?> typedQuery, int page, int size);

    int calculateNumberOfPages(int numberOfElements, int pageSize);
}
