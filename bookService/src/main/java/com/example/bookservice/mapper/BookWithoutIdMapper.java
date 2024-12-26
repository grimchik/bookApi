package com.example.bookservice.mapper;

import com.example.bookservice.dto.BookWithoutIdDTO;
import com.example.bookservice.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface BookWithoutIdMapper {
    BookWithoutIdMapper INSTANCE = Mappers.getMapper(BookWithoutIdMapper.class);
    @Mapping(source = "isbn", target = "isbn")
    BookWithoutIdDTO toDTO(Book book);
    @Mapping(source = "isbn", target = "isbn")
    Book toEntity(BookWithoutIdDTO bookDTO);
}