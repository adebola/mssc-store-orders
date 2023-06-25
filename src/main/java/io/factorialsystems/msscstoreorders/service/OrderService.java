package io.factorialsystems.msscstoreorders.service;

import io.factorialsystems.msscstoreorders.dto.OrderDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    Optional<OrderDTO> findOrderById(UUID id);
    Optional<OrderDTO> saveOrder(OrderDTO orderDto);
    Optional<OrderDTO> updateOrderById(UUID id, OrderDTO orderDto);
    Page<OrderDTO> findAllOrders(Integer pageNumber, Integer pageSize);
    Boolean deleteOrderById(UUID id);
}
