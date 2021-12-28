package com.epam.esm.repository.dto;

import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;



public class CertificateDto {

    private Integer id;

    @Size(min = 1,max = 45, message = "Certificate name length should be from 1 to 45")
    private String name;

    @Size(min = 1,max = 45, message = "Certificate description length should be from 1 to 45")
    private String description;

    @PositiveOrZero(message = "Certificate price should positive or zero")
    private Double price;

    @PositiveOrZero(message = "Certificate duration should positive or zero")
    private Integer duration;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private LocalDateTime createDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private LocalDateTime lastUpdateDate;
    private List<Tag> tags;

    public CertificateDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Certificate toEntity() {
        Certificate entity = new Certificate();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setPrice(this.price);
        entity.setDuration(this.duration);
        entity.setCreateDate(this.createDate);
        entity.setLastUpdateDate(this.lastUpdateDate);
        entity.setTags(this.tags);
        return entity;
    }

}
