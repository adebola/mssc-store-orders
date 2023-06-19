package io.factorialsystems.msscstoreorders.mapper;

import io.factorialsystems.msscstoreorders.dto.OrderDTO;
import io.factorialsystems.msscstoreorders.dto.OrderItemDTO;
import io.factorialsystems.msscstoreorders.entity.Order;
import io.factorialsystems.msscstoreorders.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface OrderMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "userId", target = "userId"),
            @Mapping(source = "address", target = "address"),
            @Mapping(source = "telephone", target = "telephone"),
            @Mapping(source = "totalPrice", target = "totalPrice"),
            @Mapping(source = "orderDate", target = "orderDate"),
            @Mapping(source = "orderStatus", target = "orderStatus"),
            @Mapping(source = "items", target = "items")
    })
    OrderDTO toDtoFromEntity(Order order);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "userId", ignore = true),
            @Mapping(source = "address", target = "address"),
            @Mapping(source = "telephone", target = "telephone"),
            @Mapping(target = "totalPrice", ignore = true),
            @Mapping(source = "orderDate", target = "orderDate"),
            @Mapping(source = "orderStatus", target = "orderStatus"),
            @Mapping(source = "items", target = "items")
    })
    Order toEntityFromDto(OrderDTO dto);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "productId", target = "productId"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "quantity", target = "quantity"),
            @Mapping(source = "unitPrice", target = "unitPrice"),
            @Mapping(source = "totalPrice", target = "totalPrice")
    })
    OrderItem toItemEntityFromItemDto(OrderItemDTO dto);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "productId", target = "productId"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "quantity", target = "quantity"),
            @Mapping(source = "unitPrice", target = "unitPrice"),
            @Mapping(source = "totalPrice", target = "totalPrice")
    })
    OrderItemDTO toItemDtoFromItemEntity(OrderItem orderItem);
}
