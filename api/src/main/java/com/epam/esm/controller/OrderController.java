package com.epam.esm.controller;

import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.repository.dto.OrderDto;
import com.epam.esm.repository.dto.UserDto;
import com.epam.esm.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final HateoasAdder<OrderDto> hateoasAdder;
    private final HateoasAdder<UserDto> userDtoHateoasAdder;

    public OrderController(OrderService orderService, HateoasAdder<OrderDto> hateoasAdder, HateoasAdder<UserDto> userDtoHateoasAdder) {
        this.orderService = orderService;
        this.hateoasAdder = hateoasAdder;
        this.userDtoHateoasAdder = userDtoHateoasAdder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@RequestBody @Valid OrderDto order) {
        OrderDto addedOrder = orderService.create(order);
        hateoasAdder.addLinks(addedOrder);
        return addedOrder;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto readOrder(@PathVariable("id") int id) {
        OrderDto order = orderService.readOrder(id);
        hateoasAdder.addLinks(order);
        return order;
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> ordersByUserId(@PathVariable int userId,
                                         @RequestParam(value = "page", defaultValue = "1", required = false) @Min(1) int page,
                                         @RequestParam(value = "size", defaultValue = "5", required = false) @Min(1) int size) {
        List<OrderDto> orders = orderService.readAllByUserId(userId, page, size);


        return orders.stream()
                .peek(orderDto -> userDtoHateoasAdder.addLinks(orderDto.getUserDto()))
                .peek(hateoasAdder::addLinks)
                .collect(Collectors.toList());
    }




}
