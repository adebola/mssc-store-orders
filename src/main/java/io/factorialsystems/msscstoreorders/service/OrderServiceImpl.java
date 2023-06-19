package io.factorialsystems.msscstoreorders.service;

import io.factorialsystems.msscstoreorders.dto.OrderDTO;
import io.factorialsystems.msscstoreorders.dto.PagedDTO;
import io.factorialsystems.msscstoreorders.entity.Order;
import io.factorialsystems.msscstoreorders.entity.OrderItem;
import io.factorialsystems.msscstoreorders.exception.NotFoundException;
import io.factorialsystems.msscstoreorders.mapper.OrderMapper;
import io.factorialsystems.msscstoreorders.repository.OrderRepository;
import io.factorialsystems.msscstoreorders.utils.JwtTokenWrapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Override
    public Optional<OrderDTO> findOrderById(UUID id) {
        return orderRepository.findById(id).map(orderMapper::toDtoFromEntity);
    }

    @Override
    @Transactional
    public Optional<OrderDTO> saveOrder(OrderDTO orderDTO) {
        return save(orderDTO);
    }

    @Override
    @Transactional
    public Optional<OrderDTO> updateOrderById(UUID id, OrderDTO orderDTO) {
        orderDTO.setId(id.toString());
        return save(orderDTO);
    }

    @Override
    @Transactional
    public PagedDTO<OrderDTO> findAllOrders(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("orderDate").descending());
        return createClientDto(orderRepository.findAll(pageable));
    }

    @Override
    @Transactional
    public Boolean deleteOrderById(UUID id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) throw new NotFoundException(String.format("Delete Id Not Found : %s", id));
        orderRepository.deleteById(id);
        return true;
    }

    private Optional<OrderDTO> save(OrderDTO dto) {
        Order order = orderMapper.toEntityFromDto(dto);
        BigDecimal orderPrice = order.getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final Set<OrderItem> items = order.getItems().stream()
                .peek(o -> o.setOrder(order))
                .collect(Collectors.toSet());

        order.setTotalPrice(orderPrice);
        order.setItems(items);
        order.setUserId(JwtTokenWrapper.getUserId());

        Order savedOrder = orderRepository.save(order);
        return Optional.of(orderMapper.toDtoFromEntity(savedOrder));
    }

    private PagedDTO<OrderDTO> createClientDto(Page<Order> orders) {
        PagedDTO<OrderDTO> pagedDto = new PagedDTO<>();

        pagedDto.setTotalSize((int)orders.getTotalElements());
        pagedDto.setPageNumber(orders.getNumber());
        pagedDto.setPageSize(orders.getSize());
        pagedDto.setPages(orders.getTotalPages());

        List<Order> o = orders.stream().toList();

        List<OrderDTO> list = orders.stream()
                .map(orderMapper::toDtoFromEntity)
                .toList();

        pagedDto.setList(list);
        return pagedDto;
    }
}