package com.clientless.songmanagement.service.inf.mainfeature;

import com.clientless.songmanagement.dto.mainfeature.ArtistDto;
import com.clientless.songmanagement.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ArtistService {
    byte[] viewImageById(String idArtist);
    ResponseEntity<Response> viewArtistById(String idArtist) throws JsonProcessingException;
    ResponseEntity<Response> listArtist() throws IOException;
    ResponseEntity<Response> deleteArtistByIdArtist(String idArtist) throws JsonProcessingException;
    ResponseEntity<Response> addArtist(ArtistDto artistDto) throws IOException;
}
