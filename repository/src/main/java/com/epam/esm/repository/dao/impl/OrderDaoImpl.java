package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.PaginationHandler;
import com.epam.esm.repository.dto.OrderDto;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.NullParameterException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.query.Query;


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

        if (order == null) {
            throw new NullParameterException("Null parameter in create order");
        }

        entityManager.persist(order);
        return order;
    }

    @Override
    public List<Order> readAllByUserId(int userId, int page, int size) {
        Session session = entityManager.unwrap(Session.class);
        Query<Order> query = session.createQuery("From Order where user.id=:user");
        query.setParameter("user", userId);
        paginationHandler.setPageToQuery(query, page, size);
        return query.list();
    }

    @Override
    public Optional<Order> readOrder(int orderId) {
        return Optional.ofNullable(entityManager.find(Order.class, orderId));
    }
}
