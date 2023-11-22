package com.clientless.songmanagement.controller.mainfeature;

import com.clientless.songmanagement.dto.mainfeature.GenreDto;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.service.inf.mainfeature.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/v1/genre/")
public class GenreController {
    @Autowired
    private GenreService genreService;

    @PostMapping(value = "upload",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> addGenre(@RequestBody GenreDto genreDto) throws IOException {
        return genreService.uploadGenre(genreDto);
    }

    @GetMapping(value = "list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> listGenre() throws IOException {
        return genreService.listGenre();
    }

    @PutMapping(value = "edit/{genreCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> editGenre(@PathVariable("genreCode") String genreCode,@RequestBody GenreDto genreDto) throws IOException {
        return genreService.editGenre(genreCode,genreDto);
    }

    @GetMapping(value = "by/{genreCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getGenreByCode(@PathVariable("genreCode") String genreCode) throws IOException {
        return genreService.getGenreByCode(genreCode);
    }

    @DeleteMapping(value = "delete/by/{genreCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteGenreById(@PathVariable("genreCode") String genreCode) throws IOException {
        return genreService.deleteGenreByCode(genreCode);
    }
}
