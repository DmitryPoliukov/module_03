package com.epam.esm.service.impl;

import com.epam.esm.repository.dao.OrderDao;
import com.epam.esm.repository.dao.UserDao;
import com.epam.esm.repository.dto.OrderDto;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final CertificateService certificateService;

    public OrderServiceImpl(
            UserDao userDao, OrderDao orderDao, CertificateService certificateService) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.certificateService = certificateService;
    }

    @Override
    public OrderDto create(OrderDto orderDto) {
        Order order = orderDto.toEntity();
        order.setCertificate(certificateService.read(orderDto.getCertificateId()).toEntity());
        orderDao.create(order);
        return readOrder(order.getId());
    }

    @Override
    public List<OrderDto> readAllByUserId(int userId, int page, int size) {
        return orderDao.readAllByUserId(userId, page, size).stream()
                .map(Order::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto readOrder(int orderId) {
        Optional<Order> order = orderDao.readOrder(orderId);
        return order.orElseThrow(ResourceNotFoundException.notFoundWithOrder(orderId)).toDto();
    }
}
