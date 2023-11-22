package com.clientless.songmanagement.service.inf.waitingapprovalinit;

import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitSongDto;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitSongEditDto;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitSongStatusSubmitDto;
import com.clientless.songmanagement.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import javazoom.jl.decoder.JavaLayerException;
import org.springframework.http.ResponseEntity;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public interface InitWaitSongService {
    ResponseEntity<Response> upload (InitWaitSongDto songDto) throws IOException;
    ResponseEntity<Response> listSong (String idAlbum) throws IOException;

    byte[] getInitWaitSongById(String idSong) throws IOException, LineUnavailableException, UnsupportedAudioFileException, JavaLayerException;
    ResponseEntity<Response> getDetailInitWaitSongById(String idSong) throws IOException;
    ResponseEntity<Response> deleteInitWaitSongByIdSong(String idSong) throws JsonProcessingException;

    ResponseEntity<Response> editStatusSubmittedSongById(String idSong, InitWaitSongStatusSubmitDto initWaitSongStatusSubmitDto) throws JsonProcessingException;

    ResponseEntity<Response> editSongById(String idSong, InitWaitSongEditDto initWaitSongEditDto) throws IOException;

}
