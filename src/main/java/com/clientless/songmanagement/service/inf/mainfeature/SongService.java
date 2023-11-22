package com.clientless.songmanagement.service.inf.mainfeature;

import com.clientless.songmanagement.dto.mainfeature.SongDto;
import com.clientless.songmanagement.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import javazoom.jl.decoder.JavaLayerException;
import org.springframework.http.ResponseEntity;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public interface SongService {
    ResponseEntity<Response> upload (SongDto songDto) throws IOException;
    ResponseEntity<Response> listSong () throws IOException;
    byte[] getSongById(String idSong) throws IOException, LineUnavailableException, UnsupportedAudioFileException, JavaLayerException;
    byte[] viewSongById(String idSong) throws IOException, LineUnavailableException, UnsupportedAudioFileException, JavaLayerException;
    ResponseEntity<Response> getDetailSongById (String idSong) throws IOException;
    ResponseEntity<Response> deleteSongByIdSong(String idSong) throws JsonProcessingException;

}
