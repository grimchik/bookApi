package com.example.bookservice.mapper;

import com.example.bookservice.dto.LibraryBookWithoutIdDTO;
import com.example.bookservice.entity.LibraryBook;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface LibraryBookWithoutIdMapper {
    LibraryBookWithoutIdMapper INSTANCE = Mappers.getMapper(LibraryBookWithoutIdMapper.class);
    LibraryBookWithoutIdDTO toDTO(LibraryBook libraryBook);
    LibraryBook toEntity(LibraryBookWithoutIdDTO libraryBookDTO);
}
