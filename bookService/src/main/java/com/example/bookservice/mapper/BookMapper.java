package com.example.bookservice.mapper;


import com.example.bookservice.dto.BookDTO;
import com.example.bookservice.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);
    @Mapping(source = "id", target = "id")
    BookDTO toDTO(Book book);
    @Mapping(source = "id", target = "id")
    Book toEntity(BookDTO bookDTO);
}

