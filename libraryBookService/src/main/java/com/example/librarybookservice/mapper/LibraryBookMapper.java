package com.example.librarybookservice.mapper;

import com.example.librarybookservice.dto.LibraryBookDTO;
import com.example.librarybookservice.entity.LibraryBook;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LibraryBookMapper {
    LibraryBookMapper INSTANCE = Mappers.getMapper(LibraryBookMapper.class);
    LibraryBookDTO toDTO(LibraryBook libraryBook);
    LibraryBook toEntity(LibraryBookDTO libraryBookDTO);
}
