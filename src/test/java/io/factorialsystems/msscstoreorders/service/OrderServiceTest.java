package io.factorialsystems.msscstoreorders.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class OrderServiceTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Test
    public void createOrder() {
//        OrderDTO requestDto = new OrderDTO();
//        requestDto.setAddress("25 Valerian, NFE");
//        requestDto.setTelephone("08055572307");
//        requestDto.setTotalPrice(BigDecimal.valueOf(1_000));
//        requestDto.setUserId("aaa");
//
//        OrderItemDTO item1 = new OrderItemDTO("id", "product1", "description1", 1, BigDecimal.valueOf(1_000), BigDecimal.valueOf(1_000));
//        OrderItemDTO item2 = new OrderItemDTO("id", "product2", "description2", 2, BigDecimal.valueOf(2_000), BigDecimal.valueOf(1_000));
//        OrderItemDTO item3 = new OrderItemDTO("id", "product3", "description3", 3, BigDecimal.valueOf(3_000), BigDecimal.valueOf(1_000));
//
//        Set<OrderItemDTO> orderItems = new HashSet<>();
//        orderItems.add(item1);
//        orderItems.add(item2);
//        orderItems.add(item3);
//
//        requestDto.setItems(orderItems);

        //Order order = orderService.createOrder(requestDto);
        //log.info("Order {}", order);
    }
}