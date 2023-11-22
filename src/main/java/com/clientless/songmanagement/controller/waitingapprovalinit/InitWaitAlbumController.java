package com.clientless.songmanagement.controller.waitingapprovalinit;

import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitAlbumDto;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitAlbumEditDto;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitAlbumStatusSubmitDto;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.service.inf.waitingapprovalinit.InitWaitAlbumService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins="http://localhost:8081")
@RestController
@RequestMapping("/v1/int_wait_album")
public class InitWaitAlbumController {
    @Autowired
    private InitWaitAlbumService initWaitAlbumService;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> uploadAlbum(@ModelAttribute InitWaitAlbumDto request) throws IOException {
        return initWaitAlbumService.uploadInitWaitAlbum(request);
    }

    @GetMapping(value = "/view_cover_album/{idAlbum}",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageCoverAlbumById(@PathVariable String idAlbum){
        return new ResponseEntity<>(initWaitAlbumService.viewImageById(idAlbum),HttpStatus.OK);
    }

    @GetMapping(value = "/list_album",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> listAlbum() throws IOException {
        return initWaitAlbumService.listInitWaitAlbum();
    }

    @DeleteMapping(value = "/delete/by/{idAlbum}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteAlbum(@PathVariable String idAlbum) throws JsonProcessingException {
        return initWaitAlbumService.deleteInitWaitAlbumByIdAlbum(idAlbum);
    }

    @GetMapping(value = "/view_album/{idAlbum}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getDetailAlbumById(@PathVariable String idAlbum) throws JsonProcessingException {
        return initWaitAlbumService.viewInitWaitAlbumById(idAlbum);
    }

    @PutMapping(value = "/status_submit/album/{idAlbum}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> editStatusSubmitAlbumById(@PathVariable String idAlbum, @RequestBody InitWaitAlbumStatusSubmitDto initWaitAlbumStatusSubmitDto) throws JsonProcessingException {
        return initWaitAlbumService.editStatusSubmitAlbumById(idAlbum, initWaitAlbumStatusSubmitDto);
    }

    @PutMapping(value = "/edit/album/{idAlbum}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> editAlbumById(@PathVariable String idAlbum, @ModelAttribute InitWaitAlbumEditDto initWaitAlbumEditDto) throws IOException {
        return initWaitAlbumService.editAlbumById(idAlbum, initWaitAlbumEditDto);
    }
}
