package io.factorialsystems.msscstoreorders.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@Table(name = "order_item")
public class OrderItem {

    @Id
    @ToString.Include
    @JdbcTypeCode(SqlTypes.CHAR)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull
    @ToString.Include
    @Column(name = "product_id", nullable = false, length = 36)
    private String productId;

    @ToString.Include
    @Column(name = "description", nullable = false)
    private String description;

    @ToString.Include
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ToString.Include
    @Column(name = "unit_price", precision = 13, scale = 2)
    private BigDecimal unitPrice;

    @ToString.Include
    @Column(name = "total_price", precision = 13, scale = 2)
    private BigDecimal totalPrice;

    @ToString.Include
    @Column(name = "discount", precision = 13, scale = 2)
    private BigDecimal discount;

    public OrderItem(UUID id, Order order, @NotNull String productId, String description, Integer quantity, BigDecimal unitPrice, BigDecimal totalPrice, BigDecimal discount) {
        this.id = id;
        this.productId = productId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.discount = discount;

        this.setOrder(order);
    }

    public void setOrder(Order order) {
        if (order != null && order.getItems() != null) {
            this.order = order;
            order.getItems().add(this);
        } else {
            log.warn("OrderItem Entity setOrder, Order Entity is Null : {}", this);
        }
    }


}