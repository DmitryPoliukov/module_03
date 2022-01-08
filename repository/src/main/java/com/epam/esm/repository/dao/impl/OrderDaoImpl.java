package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.PaginationHandler;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrderDaoImpl implements OrderDao {

    private static final String FIND_BY_USER_ID_QUERY = "SELECT o FROM " + Order.class.getName()
            + " o WHERE o.user.id = :user_id";

    private final EntityManager entityManager;
    private final PaginationHandler paginationHandler;

    public OrderDaoImpl(EntityManager entityManager, PaginationHandler paginationHandler) {
        this.entityManager = entityManager;
        this.paginationHandler = paginationHandler;
    }

    @Override
    public Order create(Order order) {
        entityManager.persist(order);
/*
        order.getCertificate().set   get().stream()
                .map(certificate -> entityManager.find(Certificate.class, certificate.getId()))
                .forEach(certificate -> certificate.setOrder(order));

 */
        Order orderDB = readOrderByUser(order.getUser().getId(), order.getId()).get();
        return orderDB;
    }

    @Override
    public List<Order> readAllByUserId(int userId, int page, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = builder.createQuery(Order.class);
        Root<Order> from = criteriaQuery.from(Order.class);
        CriteriaQuery<Order> select = criteriaQuery.select(from);

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.select(builder.count(countQuery.from(Order.class)));

        TypedQuery<Order> typedQuery = entityManager.createQuery(select);
        paginationHandler.setPageToQuery(typedQuery, page, size);
        return typedQuery.getResultList();
    }

    @Override
    public Optional<Order> readOrderByUser(long userId, long orderId) {
        return Optional.ofNullable(entityManager.find(Order.class, orderId));
    }
}
