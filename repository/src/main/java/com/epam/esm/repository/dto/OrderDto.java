package com.epam.esm.repository.dto;

import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.repository.entity.User;

import java.time.LocalDateTime;

public class OrderDto {

    private int id;
    private LocalDateTime createDate;
    private User user;
    private Certificate certificate;
    private double cost;

    public OrderDto() {
    }

    public OrderDto(int id, LocalDateTime createDate, User user, Certificate certificate, double cost) {
        this.id = id;
        this.createDate = createDate;
        this.user = user;
        this.certificate = certificate;
        this.cost = cost;
    }

    public Order toEntity() {
        Order entityOrder = new Order();
        entityOrder.setId(this.id);
        entityOrder.setCreateDate(this.getCreateDate());
        entityOrder.setUser(this.getUser());
        entityOrder.setCertificate(this.getCertificate());
        entityOrder.setCost(this.getCost());
        return entityOrder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderDto orderDto = (OrderDto) o;

        if (id != orderDto.id) return false;
        if (createDate != null ? !createDate.equals(orderDto.createDate) : orderDto.createDate != null)
            return false;
        if (user != null ? !user.equals(orderDto.user) : orderDto.user != null) return false;
        if (cost != orderDto.cost) return false;

        return certificate != null
                ? certificate.equals(orderDto.certificate)
                : orderDto.certificate == null;
    }

    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;
        result = prime * result + id;
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (certificate != null ? certificate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", createDate=").append(createDate);
        sb.append(", user=").append(user);
        sb.append(", certificate=").append(certificate);
        sb.append('}');
        return sb.toString();
    }
}
