package com.epam.esm.repository.dto;

import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.User;

import java.util.Collections;
import java.util.List;

public class UserDto {

    private int id;
    private String name;
    private String surname;
    private List<Order> orders = Collections.emptyList();


    public UserDto() {}

    public User toEntity() {
        User entityUser = new User();
        entityUser.setId(this.id);
        entityUser.setName(this.name);
        entityUser.setSurname(this.surname);
        entityUser.setOrders(this.orders);
        return entityUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDto userDto = (UserDto) o;

        if (id != userDto.id) return false;
        if (name != null ? !name.equals(userDto.name) : userDto.name != null) return false;
        if (surname != null ? !surname.equals(userDto.surname) : userDto.surname != null) return false;
        return orders != null ? orders.equals(userDto.orders) : userDto.orders == null;
    }

    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;
        result = prime * result + id;
        result = prime * result + (name != null ? name.hashCode() : 0);
        result = prime * result + (surname != null ? surname.hashCode() : 0);
        result = prime * result + (orders != null ? orders.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDto{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", orders=").append(orders);
        sb.append('}');
        return sb.toString();
    }

}
