package io.factorialsystems.msscstoreorders.dto;

import io.factorialsystems.msscstoreorders.entity.OrderStatus;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @Null
    private String id;

    @Null
    private String userId;

    private String address;
    private String telephone;

    @Null
    private BigDecimal totalPrice;

    private OrderStatus orderStatus;
    private Instant orderDate;
    private Set<OrderItemDTO> items;
}