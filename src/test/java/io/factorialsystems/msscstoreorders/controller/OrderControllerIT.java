package io.factorialsystems.msscstoreorders.controller;

import io.factorialsystems.msscstoreorders.dto.OrderDTO;
import io.factorialsystems.msscstoreorders.dto.OrderItemDTO;
import io.factorialsystems.msscstoreorders.dto.PagedDTO;
import io.factorialsystems.msscstoreorders.entity.OrderStatus;
import io.factorialsystems.msscstoreorders.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class OrderControllerIT {
    @Autowired
    OrderService orderService;

    @Test
    void findAll() {
        PagedDTO<OrderDTO> orders = orderService.findAllOrders(0, 100);
        assertThat(orders.getList().size()).isGreaterThan(4);
    }

    @Test
    @Rollback
    @Transactional
    void findById() {
        final Optional<OrderDTO> savedOrderDTO = orderService.saveOrder(createDummyDTO());
        assertThat(savedOrderDTO.isPresent()).isEqualTo(true);

        final Optional<OrderDTO> orderById = orderService.findOrderById(UUID.fromString(savedOrderDTO.get().getId()));
        assertThat(orderById.isPresent()).isEqualTo(true);

        assertThat(orderById.get().getItems().size()).isEqualTo(1);
    }

    @Test
    @Rollback
    @Transactional
    void saveOrder() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderStatus(OrderStatus.ORDER_NEW);
        orderDTO.setAddress("Address");

        OrderItemDTO item1 = new OrderItemDTO(null,  "product_1", "description_1", 1, new BigDecimal("120"), new BigDecimal("120"), BigDecimal.ZERO);
        OrderItemDTO item2 = new OrderItemDTO( null,"product_2", "description_2", 2, new BigDecimal("144"), new BigDecimal("288"), BigDecimal.ZERO);
        OrderItemDTO item3 = new OrderItemDTO( null, "product_3", "description_3", 2, new BigDecimal("180"), new BigDecimal("720"), BigDecimal.ZERO);
        OrderItemDTO item4 = new OrderItemDTO(null, "product_4", "description_4", 1, new BigDecimal("120"), new BigDecimal("120"), BigDecimal.ZERO);

        orderDTO.setItems(Set.of(item1, item2, item3, item4));

        final Optional<OrderDTO> orderDTO1 = orderService.saveOrder(orderDTO);

        assertThat(orderDTO1.isPresent()).isEqualTo(true);

        final Optional<OrderDTO> orderById = orderService.findOrderById(UUID.fromString(orderDTO1.get().getId()));

        assertThat(orderById.isPresent()).isEqualTo(true);
        assertThat(orderById.get().getId()).isEqualTo(orderDTO1.get().getId());
        assertThat(orderById.get().getItems().size()).isEqualTo(4);
    }

    @Test
    @Rollback
    @Transactional
    void updateOrder() {
        final Optional<OrderDTO> savedOrderDTO = orderService.saveOrder(createDummyDTO());
        assertThat(savedOrderDTO.isPresent()).isEqualTo(true);

        OrderDTO orderDTO = savedOrderDTO.get();
        orderDTO.setUserId("NewUser");

        Set<OrderItemDTO> newItems = savedOrderDTO.get().getItems()
                .stream()
                .peek(x -> x.setId(null))
                .collect(Collectors.toSet());

        orderDTO.setItems(newItems);

        orderService.saveOrder(savedOrderDTO.get());
    }

    @Test
    @Rollback
    @Transactional
    void deleteOrder() {
        OrderDTO orderDTO = createDummyDTO();
        Optional<OrderDTO> savedOrderDTO = orderService.saveOrder(orderDTO);
        assertThat(savedOrderDTO.isPresent()).isEqualTo(true);
        assertThat(savedOrderDTO.get().getId()).isNotNull();
        OrderDTO savedOrder = savedOrderDTO.get();

        assertThat(orderService.findOrderById(UUID.fromString(savedOrder.getId())).isPresent()).isEqualTo(true);
        orderService.deleteOrderById(UUID.fromString(savedOrder.getId()));
        assertThat(orderService.findOrderById(UUID.fromString(savedOrder.getId())).isPresent()).isEqualTo(false);
    }

    private OrderDTO createDummyDTO() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderStatus(OrderStatus.ORDER_NEW);
        orderDTO.setAddress("Address");

        OrderItemDTO item1 = new OrderItemDTO(null,  "product_1", "description_1", 1, new BigDecimal("120"), new BigDecimal("120"), BigDecimal.ZERO);
        orderDTO.setItems(Set.of(item1));

        return orderDTO;
    }
}