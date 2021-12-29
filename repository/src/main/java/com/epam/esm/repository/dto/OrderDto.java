package com.epam.esm.repository.dto;

import java.time.LocalDateTime;

public class OrderDto {

    private int id;
    private LocalDateTime createDate;
    private Double price;

    public OrderDto() {
    }

    public OrderDto(int id, LocalDateTime createDate, Double price) {
        this.id = id;
        this.createDate = createDate;
        this.price = price;
    }

}
