
DROP PROCEDURE IF EXISTS findProcedure;

CREATE PROCEDURE findProcedure (tagName varchar(45), queryPart varchar(45),
                                sortBy varchar(20), ascending boolean)

BEGIN
    IF (tagName IS NOT NULL AND queryPart IS NOT NULL ) THEN
        SELECT * from gift_certificate gc
                          JOIN gift_certificate_m2m_tag m2m on gc.id = m2m.gift_certificate_id
                          JOIN tag on m2m.tag_id = tag.id
        WHERE tag.name = tagName AND (gc.name LIKE CONCAT('%', queryPart , '%')
                                        OR gc.description LIKE CONCAT('%', queryPart , '%'))
        ORDER BY
            CASE WHEN sortBy= 'name' AND ascending IS FALSE THEN gc.name END DESC,
            CASE WHEN sortBy= 'name' AND ascending IS TRUE THEN gc.name END,
            CASE WHEN sortBy= 'date' AND ascending IS TRUE THEN gc.last_update_date END,
            CASE WHEN sortBy= 'date' AND ascending IS FALSE THEN gc.last_update_date END DESC;
    ELSEIF (tagName IS NOT NULL) THEN
        SELECT * from gift_certificate gc
                          JOIN gift_certificate_m2m_tag m2m on gc.id = m2m.gift_certificate_id
                          JOIN tag on m2m.tag_id = tag.id
        WHERE tag.name = tagName
        ORDER BY
            CASE WHEN sortBy='name' AND ascending IS FALSE THEN gc.name END DESC,
            CASE WHEN sortBy='name' AND ascending IS TRUE THEN gc.name END,
            CASE WHEN sortBy='date' AND ascending IS TRUE THEN gc.last_update_date END,
            CASE WHEN sortBy='date' AND ascending IS FALSE THEN gc.last_update_date END DESC;
    ELSEIF (queryPart IS NOT NULL) THEN
        SELECT * from gift_certificate gc
                          JOIN gift_certificate_m2m_tag m2m on gc.id = m2m.gift_certificate_id
                          JOIN tag on m2m.tag_id = tag.id
        WHERE gc.name LIKE CONCAT('%', queryPart , '%')
           OR gc.description LIKE CONCAT('%', queryPart , '%')
        ORDER BY
            CASE WHEN sortBy= 'name' AND ascending IS FALSE THEN gc.name END DESC,
            CASE WHEN sortBy= 'name' AND ascending IS TRUE THEN gc.name END,
            CASE WHEN sortBy= 'date' AND ascending IS TRUE THEN gc.last_update_date END,
            CASE WHEN sortBy= 'date' AND ascending IS FALSE THEN gc.last_update_date END DESC;
    end if;
end

