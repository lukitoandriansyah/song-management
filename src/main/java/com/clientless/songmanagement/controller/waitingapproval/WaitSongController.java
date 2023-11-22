package com.clientless.songmanagement.controller.waitingapproval;

import com.clientless.songmanagement.dto.waitingapproval.WaitSongDto;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.service.inf.waitingapproval.WaitSongService;
import com.fasterxml.jackson.core.JsonProcessingException;
import javazoom.jl.decoder.JavaLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@CrossOrigin(origins="http://localhost:8081")
@RestController
@RequestMapping("/v1/wait_song")
public class WaitSongController {
    @Autowired
    private WaitSongService waitSongService;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> uploadSong(@ModelAttribute WaitSongDto request) throws IOException {
        return waitSongService.upload(request);
    }

    @GetMapping(value = "/play_song/{idSong}")
    public ResponseEntity<byte[]> playSong(@PathVariable String idSong) throws UnsupportedAudioFileException, LineUnavailableException, IOException, JavaLayerException {
        try {
            byte[] audioByte = waitSongService.getWaitSongById(idSong);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("audio.mp3").build());
            return ResponseEntity.ok().headers(headers).body(audioByte);
        } catch (JavaLayerException ex) {
            throw new JavaLayerException("Failed to process audio", ex);
        }
    }

    @GetMapping(value = "/list_song", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> listSong(@Param("idAlbum") String idAlbum) throws IOException {
        return waitSongService.listSong(idAlbum);
    }

    @GetMapping(value = "/view_song/{idSong}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getDetailSongById(@PathVariable String idSong) throws IOException {
        return waitSongService.getDetailWaitSongById(idSong);
    }

    @DeleteMapping(value = "/delete/by/{idSong}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteAlbum(@PathVariable String idSong) throws JsonProcessingException {
        return waitSongService.deleteWaitSongByIdSong(idSong);
    }



}
