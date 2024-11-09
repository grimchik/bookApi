package com.example.bookservice.Mapper;

import com.example.bookservice.Dto.LibraryBookDTO;
import com.example.bookservice.Dto.LibraryBookWithoutIdDTO;
import com.example.bookservice.Entity.LibraryBook;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface LibraryBookWithoutIdMapper {

    LibraryBookWithoutIdMapper INSTANCE = Mappers.getMapper(LibraryBookWithoutIdMapper.class);
    LibraryBookWithoutIdDTO toDTO(LibraryBook libraryBook);
    LibraryBook toEntity(LibraryBookWithoutIdDTO libraryBookDTO);
}
