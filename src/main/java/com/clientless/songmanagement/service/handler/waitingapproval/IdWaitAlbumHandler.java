package com.clientless.songmanagement.service.handler.waitingapproval;

import com.clientless.songmanagement.repository.waitingapproval.WaitAlbumRepository;
import lombok.AllArgsConstructor;

import java.util.Objects;

import static com.clientless.songmanagement.util.Constants.CODE_FOR_ALBUM;
import static com.clientless.songmanagement.util.Constants.PREFIX_ID;

@AllArgsConstructor
public class IdWaitAlbumHandler {

    private WaitAlbumRepository waitAlbumRepository;
    public String handlerId(){
        String format = PREFIX_ID+CODE_FOR_ALBUM;
        if(waitAlbumRepository.findAll().isEmpty() || waitAlbumRepository.idAlbumLatest().get(0)==null || Objects.equals(waitAlbumRepository.idAlbumLatest().get(0), "")){
            return format+1;
        }
        String[] requestToArray = waitAlbumRepository.idAlbumLatest().get(0).split(format);
        return format+(Integer.parseInt(requestToArray[requestToArray.length-1])+1);
    }
}
