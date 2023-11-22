package com.clientless.songmanagement.service.inf.waitingapproval;

import com.clientless.songmanagement.dto.waitingapproval.WaitArtistDto;
import com.clientless.songmanagement.dto.waitingapproval.WaitArtistStatusApproveDto;
import com.clientless.songmanagement.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface WaitArtistService {
    byte[] viewImageById(String idArtist);
    ResponseEntity<Response> listWaitArtist() throws IOException;
    ResponseEntity<Response> deleteWaitArtistByIdArtist(String idArtist) throws JsonProcessingException;
    ResponseEntity<Response> addWaitArtist(WaitArtistDto artistDto) throws IOException;
    ResponseEntity<Response> viewArtistById(String idArtist) throws JsonProcessingException;
    ResponseEntity<Response> editStatusApproveArtistById(String idArtist, WaitArtistStatusApproveDto waitArtistStatusApproveDto) throws JsonProcessingException;
    ResponseEntity<Response> editArtistById(String idArtist, WaitArtistDto waitArtistDto) throws IOException;

}
