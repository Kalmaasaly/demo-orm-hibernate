package com.example;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface MovieMapper {

    //------ DAO to DTO
    @Mapping(target = "promotion",expression = "java(movie.getTitle() + \" by  \"  +  movie.getDirector())")
    MovieDTO toDTO(Movie movie);
    //----- DTO to DAO
    @Mapping(target = "id",ignore = true)
    Movie toDAO(MovieDTO movieDTO);

    @Mapping(target = "id",ignore = true)
    Movie merge(@MappingTarget Movie target, Movie source);
}
