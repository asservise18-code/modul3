package com.example.modul3.mapper;


import com.example.modul3.dto.OrderRequestDto;
import com.example.modul3.dto.OrderResponseDto;
import com.example.modul3.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(
            target = "clientFullName",
            expression = "java(order.getClient() != null ? order.getClient().getFirstName() + \" \" + order.getClient().getLastName() : null)"
    )
    OrderResponseDto toDto(Order order);

    Order toEntity(OrderRequestDto dto);
}