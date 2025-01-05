package com.example.authservice.mapper;

import com.example.authservice.dto.RegisterRequestDTO;
import com.example.authservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegisterRequestMapper {
    RegisterRequestMapper INSTANCE = Mappers.getMapper(RegisterRequestMapper.class);

    RegisterRequestDTO toDTO(User user);

    User toEntity(RegisterRequestDTO registerRequestDTO);
}
