package com.clientless.songmanagement.controller.waitingapproval;

import com.clientless.songmanagement.dto.waitingapproval.WaitArtistDto;
import com.clientless.songmanagement.dto.waitingapproval.WaitArtistStatusApproveDto;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.service.inf.waitingapproval.WaitArtistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins="http://localhost:8081")
@RestController
@RequestMapping("v1/wait_artist/")
public class WaitArtistController {
    @Autowired
    private WaitArtistService waitArtistService;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> uploadArtist(@ModelAttribute WaitArtistDto request) throws IOException {
        return waitArtistService.addWaitArtist(request);
    }

    @GetMapping(value = "/view_photo/{idArtist}",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageById(@PathVariable String idArtist){
        return new ResponseEntity<>(waitArtistService.viewImageById(idArtist), HttpStatus.OK);
    }

    @GetMapping(value = "/list_artist",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> listArtist() throws IOException {
        return waitArtistService.listWaitArtist();
    }

    @DeleteMapping(value = "/delete/by/{idArtist}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteArtist(@PathVariable String idArtist) throws JsonProcessingException {
        return waitArtistService.deleteWaitArtistByIdArtist(idArtist);
    }

    @GetMapping(value = "/view_artist/{idArtist}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getArtistById(@PathVariable String idArtist) throws JsonProcessingException {
        return waitArtistService.viewArtistById(idArtist);
    }

    @PutMapping(value = "/status_approved/artist/{idArtist}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> editStatusApproveArtistById(@PathVariable String idArtist, @RequestBody WaitArtistStatusApproveDto waitArtistStatusApproveDto) throws JsonProcessingException {
        return waitArtistService.editStatusApproveArtistById(idArtist, waitArtistStatusApproveDto);
    }

    @PutMapping(value = "/edit/artist/{idArtist}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> editArtistById(@PathVariable String idArtist, @ModelAttribute WaitArtistDto waitArtistDto) throws IOException {
        return waitArtistService.editArtistById(idArtist, waitArtistDto);
    }

}
