package com.example.librarybookservice.mapper;

import com.example.librarybookservice.dto.LibraryBookWithoutIdDTO;
import com.example.librarybookservice.entity.LibraryBook;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface LibraryBookWithoutIdMapper {
    LibraryBookWithoutIdMapper INSTANCE = Mappers.getMapper(LibraryBookWithoutIdMapper.class);
    LibraryBookWithoutIdDTO toDTO(LibraryBook libraryBook);
    LibraryBook toEntity(LibraryBookWithoutIdDTO libraryBookDTO);
}
