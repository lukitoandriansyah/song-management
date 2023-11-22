package com.clientless.songmanagement.service.inf.waitingapproval;

import com.clientless.songmanagement.dto.waitingapproval.WaitSongDto;
import com.clientless.songmanagement.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import javazoom.jl.decoder.JavaLayerException;
import org.springframework.http.ResponseEntity;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public interface WaitSongService {
    ResponseEntity<Response> upload (WaitSongDto songDto) throws IOException;
    byte[] getWaitSongById(String idSong) throws IOException, LineUnavailableException, UnsupportedAudioFileException, JavaLayerException;
    ResponseEntity<Response> listSong (String idAlbum) throws IOException;
    ResponseEntity<Response> getDetailWaitSongById(String idSong) throws IOException;
    ResponseEntity<Response> deleteWaitSongByIdSong(String idSong) throws JsonProcessingException;



}
