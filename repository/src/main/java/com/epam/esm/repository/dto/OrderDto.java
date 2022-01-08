package com.epam.esm.repository.dto;

import com.epam.esm.repository.entity.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
public class OrderDto extends RepresentationModel<OrderDto> {

    private int id;
    private LocalDateTime createDate;

    @JsonIgnore
    private UserDto userDto;

    private CertificateDto certificateDto;

    private double cost;

    public OrderDto() {
    }

    public OrderDto(int id, LocalDateTime createDate, UserDto userDto, CertificateDto certificateDto, double cost) {
        this.id = id;
        this.createDate = createDate;
        this.userDto = userDto;
        this.certificateDto = certificateDto;
        this.cost = cost;
    }

    public Order toEntity() {
        Order entityOrder = new Order();
        entityOrder.setId(this.id);
        entityOrder.setCreateDate(this.getCreateDate());
        entityOrder.setUser(this.getUserDto().toEntity());
        entityOrder.setCertificate(this.getCertificateDto().toEntity());
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

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }



    public CertificateDto getCertificateDto() {
        return certificateDto;
    }

    public void setCertificateDto(CertificateDto certificateDto) {
        this.certificateDto = certificateDto;
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
        if (userDto != null ? !userDto.equals(orderDto.userDto) : orderDto.userDto != null) return false;
        if (cost != orderDto.cost) return false;

        return certificateDto != null
                ? certificateDto.equals(orderDto.certificateDto)
                : orderDto.certificateDto == null;
    }

    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;
        result = prime * result + id;
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
      //  result = 31 * result + (userDto != null ? userDto.hashCode() : 0);
        result = 31 * result + (certificateDto != null ? certificateDto.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", createDate=").append(createDate);
        sb.append(", certificate=").append(certificateDto);
        sb.append('}');
        return sb.toString();
    }
}
