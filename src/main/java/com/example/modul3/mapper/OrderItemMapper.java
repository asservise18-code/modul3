package com.example.modul3.mapper;

import com.example.modul3.dto.OrderItemResponseDto;
import com.example.modul3.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    OrderItemResponseDto toDto(OrderItem orderItem);

    List<OrderItemResponseDto> toDtoList(List<OrderItem> orderItems);
}

