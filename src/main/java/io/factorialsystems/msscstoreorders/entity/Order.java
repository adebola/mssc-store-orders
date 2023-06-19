package io.factorialsystems.msscstoreorders.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Size(max = 36)
    @Column(name = "user_id", length = 36)
    private String userId;

    @Size(max = 512)
    @Column(name = "address", length = 512)
    private String address;

    @Size(max = 64)
    @Column(name = "telephone", length = 64)
    private String telephone;

    @NotNull
    @Column(name = "total_price", nullable = false, precision = 13, scale = 2)
    private BigDecimal totalPrice;

    @NotNull
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @CreationTimestamp
    @Column(name = "order_date")
    private Instant orderDate = Instant.now();

    @UpdateTimestamp
    @Column(name = "update_date")
    private Instant updateDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> items = new LinkedHashSet<>();

    @Version
    @Column(name = "version")
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return getId() != null && Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}