package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.dao.PaginationHandler;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.dto.UserDto;
import com.epam.esm.repository.entity.PaginationParameter;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private static final String SQL_REQUEST_FOR_USER_ID_WITH_HIGHEST_COST_ORDERS =
            "(SELECT user_id FROM  "
                    + "(SELECT SUM(cost) AS summa,user_id "
                    + "FROM orders "
                    + "GROUP BY user_id) AS user_orders_cost"
                    + " ORDER BY summa desc limit 1)";

    private static final String SQL_REQUEST_FOR_WIDELY_USED_TAG_FROM_HIGHEST_COST_ORDERS_USER =
            "SELECT tag.id, tag.name "
                    + "FROM tag "
                    + "JOIN gift_certificate_m2m_tag gcm2mt on tag.id = gcm2mt.tag_id "
                    + "JOIN orders ON gcm2mt.gift_certificate_id=certificate_id "
                    + "WHERE user_id="
                    + SQL_REQUEST_FOR_USER_ID_WITH_HIGHEST_COST_ORDERS
                    + " GROUP BY tag.name "
                    + "ORDER BY count(tag.name) desc limit 1;";

    private final PaginationHandler paginationHandler;
    private final EntityManager entityManager;

    @Autowired
    public UserDaoImpl(PaginationHandler paginationHandler, EntityManager entityManager) {
        this.paginationHandler = paginationHandler;
        this.entityManager = entityManager;
    }

    @Override
    public User create(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> read(int id) {
        User user =  Optional.ofNullable(entityManager.find(User.class, id)).get();
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public List<User> readAll(PaginationParameter parameter) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> from = criteriaQuery.from(User.class);
        CriteriaQuery<User> select = criteriaQuery.select(from);

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.select(builder.count(countQuery.from(User.class)));
        int numberOfElements = entityManager.createQuery(countQuery).getSingleResult().intValue();

        int numberOfPages =
                paginationHandler.calculateNumberOfPages(numberOfElements, parameter.getSize());

        TypedQuery<User> typedQuery = entityManager.createQuery(select);
        paginationHandler.setPageToQuery(typedQuery, parameter);
        return typedQuery.getResultList();

    }

    @Override
    public Tag takeMostWidelyTagFromUserWithHighestCostOrders() {
        Query q = entityManager.createNativeQuery(
                        SQL_REQUEST_FOR_WIDELY_USED_TAG_FROM_HIGHEST_COST_ORDERS_USER);

        Optional<Object[]> tagValue = q.getResultStream().findFirst();
        if (tagValue.isPresent()) {
            Integer id = ((Integer) tagValue.get()[0]);
            String name = (String) tagValue.get()[1];
            return new Tag(id, name);
        } else throw new TagException("There is no any tags in orders");
    }
}
