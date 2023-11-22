package com.clientless.songmanagement.controller.mainfeature;

import com.clientless.songmanagement.dto.mainfeature.ArtistDto;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.service.inf.mainfeature.ArtistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("v1/artist/")
public class ArtistController {
    @Autowired
    private ArtistService artistService;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> uploadArtist(@ModelAttribute ArtistDto request) throws IOException {
        return artistService.addArtist(request);
    }

    @GetMapping(value = "/view_photo/{idArtist}",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageById(@PathVariable String idArtist){
        return new ResponseEntity<>(artistService.viewImageById(idArtist), HttpStatus.OK);
    }

    @GetMapping(value = "/view_artist/{idArtist}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getArtistById(@PathVariable String idArtist) throws JsonProcessingException {
        return artistService.viewArtistById(idArtist);
    }

    @GetMapping(value = "/list_artist",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> listArtist() throws IOException {
        return artistService.listArtist();
    }

    @DeleteMapping(value = "/delete/by/{idArtist}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteArtist(@PathVariable String idArtist) throws JsonProcessingException {
        return artistService.deleteArtistByIdArtist(idArtist);
    }
}
