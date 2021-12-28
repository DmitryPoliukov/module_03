package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.dao.AbstractCRDRepository;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao, AbstractCRDRepository<Tag> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_CREATE = "INSERT INTO tag(name) VALUES (?)";
    private static final String SQL_READ = "SELECT id,name FROM tag WHERE id=?";
    private static final String SQL_READ_ALL = "SELECT id,name FROM tag";
    private static final String SQL_DELETE = "DELETE FROM tag WHERE id=?";
    private static final String SQL_READ_BY_NAME = "SELECT id,name FROM tag WHERE name=?";

    @Override
    public Tag create(Tag tag) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, tag.getName());
            return stmt;
        }, keyHolder);
        tag.setId(keyHolder.getKey().intValue());

        return tag;
    }

    @Override
    public Optional<Tag> read(int id) {
        return jdbcTemplate
                .queryForStream(SQL_READ, new BeanPropertyRowMapper<>(Tag.class), id)
                .findAny();
    }

    @Override
    public List<Tag> readAll() {
        return jdbcTemplate.query(SQL_READ_ALL, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public Optional<Tag> read(String name) {
        return jdbcTemplate
                .queryForStream(SQL_READ_BY_NAME, new BeanPropertyRowMapper<>(Tag.class), name)
                .findAny();
    }
}
