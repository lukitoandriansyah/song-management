package com.clientless.songmanagement.controller.mainfeature;

import com.clientless.songmanagement.dto.mainfeature.AlbumDto;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.service.inf.mainfeature.AlbumService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/v1/album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> uploadAlbum(@ModelAttribute AlbumDto request) throws IOException {
        return albumService.uploadAlbum(request);
    }

    @GetMapping(value = "/view_cover_album/{idAlbum}",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageCoverAlbumById(@PathVariable String idAlbum){
        return new ResponseEntity<>(albumService.viewImageById(idAlbum),HttpStatus.OK);
    }

    @GetMapping(value = "/view_album/{idAlbum}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getDetailAlbumById(@PathVariable String idAlbum) throws JsonProcessingException {
        return albumService.viewAlbumById(idAlbum);
    }

    @GetMapping(value = "/list_album",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> listAlbum() throws IOException {
        return albumService.listAlbum();
    }

    @DeleteMapping(value = "/delete/by/{idAlbum}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteAlbum(@PathVariable String idAlbum) throws JsonProcessingException {
        return albumService.deleteAlbumByIdAlbum(idAlbum);
    }
}
