package com.clientless.songmanagement.service.inf.mainfeature;

import com.clientless.songmanagement.dto.mainfeature.GenreDto;
import com.clientless.songmanagement.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface GenreService {
    ResponseEntity<Response> uploadGenre(GenreDto genreDtoRequest) throws JsonProcessingException;
    ResponseEntity<Response> listGenre() throws JsonProcessingException;
    ResponseEntity<Response> editGenre(String codeGenre,GenreDto genreDto) throws JsonProcessingException;
    ResponseEntity<Response> getGenreByCode(String codeGenre) throws JsonProcessingException;
    ResponseEntity<Response> deleteGenreByCode(String codeGenre) throws JsonProcessingException;
}
