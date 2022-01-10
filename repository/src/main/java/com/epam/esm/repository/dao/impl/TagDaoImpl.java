package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.dao.PaginationHandler;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.NullParameterException;
import org.apache.catalina.valves.JDBCAccessLogValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class TagDaoImpl implements TagDao {

    private final EntityManager entityManager;
    private final PaginationHandler paginationHandler;
    private final JdbcTemplate jdbcTemplate;

    public TagDaoImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate, PaginationHandler paginationHandler) {
        this.entityManager = entityManager;
        this.paginationHandler = paginationHandler;
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_READ = "SELECT id,name FROM tag WHERE id=?";
    private static final String SQL_READ_ALL = "SELECT id,name FROM tag";
    private static final String SQL_DELETE = "DELETE FROM tag WHERE id=:id";
    private static final String SQL_READ_BY_NAME = "SELECT id,name FROM tag WHERE name=?";

    @Override
    public Tag create(Tag tag) {
        if(tag == null) {
            throw new NullParameterException("Null parameter in create tag");
        }
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public Optional<Tag> read(int id) {
        return jdbcTemplate
                .queryForStream(SQL_READ, new BeanPropertyRowMapper<>(Tag.class), id)
                .findAny();
    }

    @Override
    public List<Tag> readAll(int page, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = builder.createQuery(Tag.class);
        Root<Tag> from = criteriaQuery.from(Tag.class);
        CriteriaQuery<Tag> select = criteriaQuery.select(from);

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.select(builder.count(countQuery.from(Tag.class)));

        TypedQuery<Tag> typedQuery = entityManager.createQuery(select);
        paginationHandler.setPageToQuery(typedQuery, page, size);
        return typedQuery.getResultList();
    }

    @Override
    public List<Tag> readAll() {
        return jdbcTemplate.query(SQL_READ_ALL, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public void delete(int id) {
        entityManager.createNativeQuery(SQL_DELETE)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public Optional<Tag> read(String name) {
        return jdbcTemplate
                .queryForStream(SQL_READ_BY_NAME, new BeanPropertyRowMapper<>(Tag.class), name)
                .findAny();
    }
}
