package com.epam.esm.repository.dao.impl;

import com.epam.esm.repository.dao.CertificateDao;
import com.epam.esm.repository.dao.PaginationHandler;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.exception.NullParameterException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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

    private static final String SQL_READ_BONDING_TAGS = "SELECT t.id, t.name FROM tag t JOIN gift_certificate_m2m_tag m2m ON t.id=m2m.tag_id WHERE gift_certificate_id = :gc_id";

    private static final String SQL_DELETE_BONDING_TAGS_BY_TAG_ID = "DELETE FROM gift_certificate_m2m_tag WHERE tag_id = :id";

    private static final String SQL_DELETE_BONDING_TAGS_BY_CERTIFICATE_ID =
            "DELETE FROM gift_certificate_m2m_tag WHERE gift_certificate_id = :id";

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
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("findProcedure");

        storedProcedure.registerStoredProcedureParameter("tagName", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("queryPart", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("sortBy", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("ascending", Boolean.class, ParameterMode.IN);
        storedProcedure.setParameter("tagName", tagName);
        storedProcedure.setParameter("queryPart", descriptionOrNamePart);
        storedProcedure.setParameter("sortBy", sortParameter);
        storedProcedure.setParameter("ascending", ascending);

        storedProcedure.execute();

        List<Object[]> objList = storedProcedure.getResultList();
        List<Certificate> certificates = new ArrayList<>();
        for (Object[] array : objList) {
            Certificate certificate = new Certificate();
            certificate.setId((Integer) array[0]);

            Timestamp createDate = (Timestamp) array[1];
            certificate.setCreateDate(createDate.toLocalDateTime());

            certificate.setDescription((String) array[2]);
            certificate.setDuration((Integer) array[3]);

            Timestamp lastUpdateDate = (Timestamp) array[4];
            certificate.setLastUpdateDate(lastUpdateDate.toLocalDateTime());

            certificate.setName((String) array[5]);
            certificate.setPrice((Double) array[6]);
            certificates.add(certificate);

        }
        return certificates;
    }

    @Override
    public void update(Certificate certificate) {
        entityManager.merge(certificate);
    }

    @Override
    public List<Tag> readCertificateTags(int certificateId) {
        List<Object[]> objects = entityManager.createNativeQuery(SQL_READ_BONDING_TAGS)
                .setParameter("gc_id", certificateId)
                .getResultList();
        List<Tag> tagList = new ArrayList<>();
        for(Object[] object : objects) {
            Tag tag = new Tag();
           tag.setId((Integer) object[0]);
           tag.setName((String) object[1]);
           tagList.add(tag);
        }
        return tagList;
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
