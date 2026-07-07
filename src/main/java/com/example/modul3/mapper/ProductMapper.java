package com.example.modul3.mapper;

import com.example.modul3.dto.ProductRequestDto;
import com.example.modul3.dto.ProductResponseDto;
import com.example.modul3.model.Product;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")

public interface ProductMapper {

    Product toEntity(ProductRequestDto dto);
    ProductResponseDto toResponseDto(Product product);


}
