package com.example.bookservice.mapper;

import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.dto.BookWithoutIdDTO;
import com.example.bookservice.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface BookWithoutIdMapper {
    BookWithoutIdMapper INSTANCE = Mappers.getMapper(BookWithoutIdMapper.class);
    BookWithoutIdDTO toDTO(Book book);
    Book toEntity(BookWithoutIdDTO bookDTO);
}
