package com.example.bookservice.Mapper;

import com.example.bookservice.Dto.LibraryBookDTO;
import com.example.bookservice.Entity.LibraryBook;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LibraryBookMapper {
    LibraryBookMapper INSTANCE = Mappers.getMapper(LibraryBookMapper.class);
    LibraryBookDTO toDTO(LibraryBook libraryBook);
    LibraryBook toEntity(LibraryBookDTO libraryBookDTO);
}
