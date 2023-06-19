package io.factorialsystems.msscstoreorders.repository;

import io.factorialsystems.msscstoreorders.entity.Order;
import io.factorialsystems.msscstoreorders.entity.OrderItem;
import io.factorialsystems.msscstoreorders.entity.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;


    @Test
    @Rollback
    @Transactional
    public void testCreateOrder() {
        Order order = new Order();
        order.setUserId(UUID.randomUUID().toString());
        order.setTelephone("08055572307");
        order.setAddress("25 Valerian");
        order.setTotalPrice(new BigDecimal("1200"));
        order.setOrderStatus(OrderStatus.ORDER_NEW);

        OrderItem item1 = new OrderItem(null, order, "product_1", "description_1", 1, new BigDecimal("120"), new BigDecimal("120"), BigDecimal.ZERO);
        OrderItem item2 = new OrderItem(null, order,"product_2", "description_2", 2, new BigDecimal("144"), new BigDecimal("288"), BigDecimal.ZERO);
        OrderItem item3 = new OrderItem(null, order, "product_3", "description_3", 2, new BigDecimal("180"), new BigDecimal("720"), BigDecimal.ZERO);
        OrderItem item4 = new OrderItem(null, order, "product_4", "description_4", 1, new BigDecimal("120"), new BigDecimal("120"), BigDecimal.ZERO);

        order.setItems(Set.of(item1, item2, item3, item4));

        orderRepository.save(order);

        List<Order> orders = orderRepository.findAll();
        assertThat(orders.size()).isEqualTo(8);
        assertThat(orders.get(0).getItems().size()).isEqualTo(1);

    }


    @Test
    @Rollback
    @Transactional
    public void testCreateWithNullOrder() {
        Order order = new Order();
        order.setUserId(UUID.randomUUID().toString());
        order.setTelephone("08055572307");
        order.setAddress("25 Valerian");
        order.setTotalPrice(new BigDecimal("1200"));
        order.setOrderStatus(OrderStatus.ORDER_NEW);

        OrderItem item1 = new OrderItem(null, null, "product_1", "description_1", 1, new BigDecimal("120"), new BigDecimal("120"), BigDecimal.ZERO);
        OrderItem item2 = new OrderItem(null, null,"product_2", "description_2", 2, new BigDecimal("144"), new BigDecimal("288"), BigDecimal.ZERO);
        OrderItem item3 = new OrderItem(null, null, "product_3", "description_3", 2, new BigDecimal("180"), new BigDecimal("720"), BigDecimal.ZERO);
        OrderItem item4 = new OrderItem(null, null, "product_4", "description_4", 1, new BigDecimal("120"), new BigDecimal("120"), BigDecimal.ZERO);

        order.setItems(Set.of(item1, item2, item3, item4));

        orderRepository.save(order);

        List<Order> orders = orderRepository.findAll();
        assertThat(orders.size()).isEqualTo(8);
        assertThat(orders.get(0).getItems().size()).isEqualTo(1);

    }

    @Test
    @Rollback
    @Transactional
    public void testUpdateOrder() {

        Order order = new Order();
        order.setUserId(UUID.randomUUID().toString());
        order.setTelephone("08055572307");
        order.setAddress("25 Valerian");
        order.setTotalPrice(new BigDecimal("1200"));
        order.setOrderStatus(OrderStatus.ORDER_NEW);

        OrderItem item1 = new OrderItem(null, null, "product_1", "description_1", 1, new BigDecimal("120"), new BigDecimal("120"), BigDecimal.ZERO);
        OrderItem item2 = new OrderItem(null, null,"product_2", "description_2", 2, new BigDecimal("144"), new BigDecimal("288"), BigDecimal.ZERO);
        OrderItem item3 = new OrderItem(null, null, "product_3", "description_3", 2, new BigDecimal("180"), new BigDecimal("720"), BigDecimal.ZERO);
        OrderItem item4 = new OrderItem(null, null, "product_4", "description_4", 1, new BigDecimal("120"), new BigDecimal("120"), BigDecimal.ZERO);

        order.setItems(Set.of(item1, item2));

        Order newOrder = orderRepository.save(order);

        List<Order> orders = orderRepository.findAll();
        assertThat(orders.size()).isGreaterThan(0);
        assertThat(orders.get(0).getItems().size()).isGreaterThan(0);

        Set<OrderItem> items = new HashSet<>();
        items.add(item3);
        items.add(item4);

        newOrder.setTotalPrice(new BigDecimal("1500"));
        newOrder.setItems(items);

        Order savedOrder = orderRepository.save(newOrder);

//        assertThat(savedOrder.getTotalPrice()).isEqualTo(new BigDecimal("1500"));

        List<Order> newOrders = orderRepository.findAll();
//        assertThat(newOrders.get(0).getTotalPrice()).isEqualTo(new BigDecimal("1500"));
//        assertThat(newOrders.size()).isEqualTo(1);
//        assertThat(newOrders.get(0).getItems().size()).isEqualTo(8);
    }
}