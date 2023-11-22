package com.clientless.songmanagement.service.inf.waitingapprovalinit;

import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitAlbumDto;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitAlbumEditDto;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitAlbumStatusSubmitDto;
import com.clientless.songmanagement.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface InitWaitAlbumService {
    ResponseEntity<Response> uploadInitWaitAlbum(InitWaitAlbumDto request) throws IOException;
    byte[] viewImageById(String idAlbum);
    ResponseEntity<Response> listInitWaitAlbum() throws IOException;
    ResponseEntity<Response> deleteInitWaitAlbumByIdAlbum(String idAlbum) throws JsonProcessingException;
    ResponseEntity<Response> viewInitWaitAlbumById(String idAlbum) throws JsonProcessingException;
    ResponseEntity<Response> editStatusSubmitAlbumById(String idAlbum, InitWaitAlbumStatusSubmitDto initWaitAlbumStatusSubmitDto) throws JsonProcessingException;
    ResponseEntity<Response> editAlbumById(String idAlbum, InitWaitAlbumEditDto initWaitAlbumEditDto) throws IOException;

}
