package com.example.bookservice.mapper;

import com.example.bookservice.dto.LibraryBookDTO;
import com.example.bookservice.entity.LibraryBook;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LibraryBookMapper {
    LibraryBookMapper INSTANCE = Mappers.getMapper(LibraryBookMapper.class);
    LibraryBookDTO toDTO(LibraryBook libraryBook);
    LibraryBook toEntity(LibraryBookDTO libraryBookDTO);
}
