package com.clientless.songmanagement.controller.waitingapproval;

import com.clientless.songmanagement.dto.waitingapproval.WaitAlbumDto;
import com.clientless.songmanagement.dto.waitingapproval.WaitAlbumEditDto;
import com.clientless.songmanagement.dto.waitingapproval.WaitAlbumStatusApproveDto;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitAlbumEditDto;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitAlbumStatusSubmitDto;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.service.inf.waitingapproval.WaitAlbumService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins="http://localhost:8081")
@RestController
@RequestMapping("/v1/wait_album")
public class WaitAlbumController {
    @Autowired
    private WaitAlbumService waitAlbumService;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> uploadAlbum(@ModelAttribute WaitAlbumDto request) throws IOException {
        return waitAlbumService.uploadWaitAlbum(request);
    }

    @GetMapping(value = "/view_cover_album/{idAlbum}",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageCoverAlbumById(@PathVariable String idAlbum){
        return new ResponseEntity<>(waitAlbumService.viewImageById(idAlbum),HttpStatus.OK);
    }

    @GetMapping(value = "/list_album",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> listAlbum() throws IOException {
        return waitAlbumService.listWaitAlbum();
    }

    @DeleteMapping(value = "/delete/by/{idAlbum}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteAlbum(@PathVariable String idAlbum) throws JsonProcessingException {
        return waitAlbumService.deleteWaitAlbumByIdAlbum(idAlbum);
    }
    @GetMapping(value = "/view_album/{idAlbum}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getDetailAlbumById(@PathVariable String idAlbum) throws JsonProcessingException {
        return waitAlbumService.viewWaitAlbumById(idAlbum);
    }

    @PutMapping(value = "/status_approve/album/{idAlbum}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> editStatusApproveAlbumById(@PathVariable String idAlbum, @RequestBody WaitAlbumStatusApproveDto waitAlbumStatusApproveDto) throws JsonProcessingException {
        return waitAlbumService.editStatusApproveAlbumById(idAlbum, waitAlbumStatusApproveDto);
    }

    @PutMapping(value = "/edit/album/{idAlbum}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> editAlbumById(@PathVariable String idAlbum, @ModelAttribute WaitAlbumEditDto waitAlbumEditDto) throws IOException {
        return waitAlbumService.editAlbumById(idAlbum, waitAlbumEditDto);
    }

}
