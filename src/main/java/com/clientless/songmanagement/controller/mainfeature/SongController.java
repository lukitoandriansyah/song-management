package com.clientless.songmanagement.controller.mainfeature;

import com.clientless.songmanagement.dto.mainfeature.SongDto;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.service.inf.mainfeature.SongService;
import com.fasterxml.jackson.core.JsonProcessingException;
import javazoom.jl.decoder.JavaLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.*;
import java.io.IOException;

@CrossOrigin(origins="http://localhost:8081")
@RestController
@RequestMapping("/v1/song")
public class SongController {
    @Autowired
    private SongService songService;

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> uploadSong(@ModelAttribute SongDto request) throws IOException {
        return songService.upload(request);
    }

    @GetMapping(value = "/list_song", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> listSong() throws IOException {
        return songService.listSong();
    }

    @GetMapping(value = "/play_song/{idSong}")
    public ResponseEntity<byte[]> playSong(@PathVariable String idSong) throws UnsupportedAudioFileException, LineUnavailableException, IOException, JavaLayerException {
        try {
            byte[] audioByte = songService.getSongById(idSong);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("audio.mp3").build());
            return ResponseEntity.ok().headers(headers).body(audioByte);
        } catch (JavaLayerException ex) {
            throw new JavaLayerException("Failed to process audio", ex);
        }
    }

    @GetMapping(value = "/view_cover_song/{idSong}",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageCoverSongById(@PathVariable String idSong) throws UnsupportedAudioFileException, LineUnavailableException, IOException, JavaLayerException {
        return new ResponseEntity<>(songService.viewSongById(idSong),HttpStatus.OK);
    }

    @GetMapping(value = "/view_song/{idSong}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getDetailSongById(@PathVariable String idSong) throws IOException {
        return songService.getDetailSongById(idSong);
    }

    @DeleteMapping(value = "/delete/by/{idSong}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteAlbum(@PathVariable String idSong) throws JsonProcessingException {
        return songService.deleteSongByIdSong(idSong);
    }

}
