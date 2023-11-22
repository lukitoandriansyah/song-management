package com.clientless.songmanagement.service.inf.mainfeature;

import com.clientless.songmanagement.dto.mainfeature.AlbumDto;
import com.clientless.songmanagement.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AlbumService {
    ResponseEntity<Response> uploadAlbum(AlbumDto request) throws IOException;
    byte[] viewImageById(String idAlbum);
    ResponseEntity<Response> viewAlbumById(String idAlbum) throws JsonProcessingException;
    ResponseEntity<Response> listAlbum() throws IOException;
    ResponseEntity<Response> deleteAlbumByIdAlbum(String idAlbum) throws JsonProcessingException;
}
