package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.dao.CertificateDao;
import com.epam.esm.repository.dao.PaginationHandler;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.entity.User;
import com.epam.esm.repository.exception.NullParameterException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional
public class CertificateDaoImpl implements CertificateDao {

    private final JdbcTemplate jdbcTemplate;
    private final PaginationHandler paginationHandler;
    private final EntityManager entityManager;

    @Autowired
    public CertificateDaoImpl(JdbcTemplate jdbcTemplate, PaginationHandler paginationHandler, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
        this.paginationHandler = paginationHandler;
    }

    private static final String SQL_DELETE_CERTIFICATE = "DELETE FROM gift_certificate WHERE id = :id";

    private static final String SQL_ADD_TAG = "INSERT INTO gift_certificate_m2m_tag (tag_id, gift_certificate_id) " +
            "VALUES (:tag_id, :gift_certificate_id)";

    private static final String SQL_REMOVE_TAG = "DELETE FROM gift_certificate_m2m_tag WHERE " +
            "gift_certificate_id = :gift_certificate_id AND tag_id = :tag_id";

    private static final String SQL_READ_BONDING_TAGS = "SELECT t.id, name FROM tag t JOIN gift_certificate_m2m_tag m2m ON t.id=m2m.tag_id WHERE gift_certificate_id = :gc_id";

    private static final String SQL_DELETE_BONDING_TAGS_BY_TAG_ID = "DELETE FROM gift_certificate_m2m_tag WHERE tag_id = :id";

    private static final String SQL_DELETE_BONDING_TAGS_BY_CERTIFICATE_ID =
            "DELETE FROM gift_certificate_m2m_tag WHERE gift_certificate_id = :id";

    private static final RowMapper<Certificate> CERTIFICATE_ROW_MAPPER =
            (rs, rowNum) -> {
                Certificate certificate = new Certificate();
                certificate.setId(rs.getInt(1));
                certificate.setName(rs.getString(6));
                certificate.setDescription(rs.getString(3));
                Double price = rs.getDouble(7);
                certificate.setPrice(price);
                Integer duration = rs.getInt(4);
                certificate.setDuration(duration);
                certificate.setCreateDate(rs.getObject(2, LocalDateTime.class));
                certificate.setLastUpdateDate(rs.getObject(5, LocalDateTime.class));
                return certificate;
            };

    public List<Certificate> readBySomeTags(List<String> tags, int page, int size) {

        Session session = entityManager.unwrap(Session.class);
/*
        String querey  = "select * fromo table";

        for (String tag : tags) {
            querey += "where (sel"
        }

 */



        org.hibernate.query.Query<Certificate> query = session.createQuery(
                "Select gc From Certificate gc JOIN Certificate.tags t WHERE t.name IN (:tags)");
        query.setParameter("tags", tags);
        paginationHandler.setPageToQuery(query, page, size);
        return query.list();
    }

    @Override
    public Certificate create(Certificate certificate) {
        if (certificate == null) {
            throw new NullParameterException("Null parameter in create certificate");
        }
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public Optional<Certificate> read(int certificateId) {
        return Optional.ofNullable(entityManager.find(Certificate.class, certificateId));
    }

    @Override
    public List<Certificate> readAll(int page, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = builder.createQuery(Certificate.class);
        Root<Certificate> from = criteriaQuery.from(Certificate.class);
        CriteriaQuery<Certificate> select = criteriaQuery.select(from);

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.select(builder.count(countQuery.from(Certificate.class)));

        TypedQuery<Certificate> typedQuery = entityManager.createQuery(select);
        paginationHandler.setPageToQuery(typedQuery, page, size);
        return typedQuery.getResultList();
    }

    @Override
    public List<Certificate> readCertificateWithParams(String tagName, String descriptionOrNamePart, String sortParameter, boolean ascending) {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        SimpleJdbcCall simpleCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("findProcedure")
                .returningResultSet("certificates",
                CERTIFICATE_ROW_MAPPER);
        Map<?,?> out = simpleCall.execute(
                new MapSqlParameterSource()
                        .addValue("tagName", tagName)
                        .addValue("queryPart", descriptionOrNamePart)
                        .addValue("sortBy", sortParameter)
                        .addValue("ascending", ascending));
        return (List<Certificate>) out.get("certificates");
    }

    @Override
    public void update(Certificate certificate) {
        entityManager.merge(certificate);
    }

    @Override
    public List<Tag> readCertificateTags(int certificateId) {
        Query query = entityManager
                        .createNativeQuery(SQL_READ_BONDING_TAGS)
                        .setParameter("gc_id", certificateId);
        return query.getResultList();
    }

    @Override
    public void addTag(int tagId, int certificateId) {
        Query query =
                entityManager
                        .createNativeQuery(SQL_ADD_TAG)
                        .setParameter("tag_id", tagId)
                        .setParameter("gift_certificate_id", certificateId);
        query.executeUpdate();
    }

    @Override
    public int removeTag(int tagId, int certificateId) {
        Query q = entityManager.createNativeQuery(SQL_REMOVE_TAG)
                        .setParameter("tag_id", tagId)
                        .setParameter("gift_certificate_id", certificateId);
        return q.executeUpdate();
    }

    @Override
    public int delete(int certificateId) {
        Query q = entityManager.createNativeQuery(SQL_DELETE_CERTIFICATE)
                .setParameter("id", certificateId);
        return q.executeUpdate();
    }

    @Override
    public int deleteBondingTagsByTagId(int tagId) {
        Query q = entityManager.createNativeQuery(SQL_DELETE_BONDING_TAGS_BY_TAG_ID)
                .setParameter("id", tagId);
        return q.executeUpdate();
    }

    @Override
    public int deleteBondingTagsByCertificateId(int certificateId) {
        Query q = entityManager.createNativeQuery(SQL_DELETE_BONDING_TAGS_BY_CERTIFICATE_ID)
                .setParameter("id", certificateId);
        return q.executeUpdate();
    }
}
