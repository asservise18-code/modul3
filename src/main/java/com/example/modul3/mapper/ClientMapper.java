package com.example.modul3.mapper;


import com.example.modul3.dto.ClientRequestDto;
import com.example.modul3.dto.ClientResponseDto;
import com.example.modul3.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = OrderMapper.class)
public interface ClientMapper {

    Client toEntity(ClientRequestDto dto);

    @Mapping(target = "orders", ignore = true)
    ClientResponseDto toListDto(Client client);

    ClientResponseDto toDto(Client client);
}