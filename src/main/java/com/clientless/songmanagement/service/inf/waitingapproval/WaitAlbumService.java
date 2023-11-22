package com.clientless.songmanagement.service.inf.waitingapproval;

import com.clientless.songmanagement.dto.waitingapproval.WaitAlbumDto;
import com.clientless.songmanagement.dto.waitingapproval.WaitAlbumEditDto;
import com.clientless.songmanagement.dto.waitingapproval.WaitAlbumStatusApproveDto;
import com.clientless.songmanagement.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface WaitAlbumService {
    ResponseEntity<Response> uploadWaitAlbum(WaitAlbumDto request) throws IOException;
    byte[] viewImageById(String idAlbum);
    ResponseEntity<Response> listWaitAlbum() throws IOException;
    ResponseEntity<Response> deleteWaitAlbumByIdAlbum(String idAlbum) throws JsonProcessingException;
    ResponseEntity<Response> viewWaitAlbumById(String idAlbum) throws JsonProcessingException;

    ResponseEntity<Response> editStatusApproveAlbumById(String idAlbum, WaitAlbumStatusApproveDto waitAlbumStatusApproveDto) throws  JsonProcessingException;
    ResponseEntity<Response> editAlbumById(String idAlbum, WaitAlbumEditDto waitAlbumEditDto) throws IOException;

}
