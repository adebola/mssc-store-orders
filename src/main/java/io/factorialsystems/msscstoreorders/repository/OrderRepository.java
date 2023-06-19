package io.factorialsystems.msscstoreorders.repository;

import io.factorialsystems.msscstoreorders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}