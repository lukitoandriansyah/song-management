package com.clientless.songmanagement.service.handler.waitingapprovalinit;

import com.clientless.songmanagement.repository.waitingapprovalinit.InitWaitAlbumRepository;
import lombok.AllArgsConstructor;

import java.util.Objects;

import static com.clientless.songmanagement.util.Constants.CODE_FOR_ALBUM;
import static com.clientless.songmanagement.util.Constants.PREFIX_ID;

@AllArgsConstructor
public class IdInitWaitAlbumHandler {

    private InitWaitAlbumRepository initWaitAlbumRepository;
    public String handlerId(){
        String format = PREFIX_ID+CODE_FOR_ALBUM;
        if(initWaitAlbumRepository.findAll().isEmpty() || initWaitAlbumRepository.idAlbumLatest().get(0)==null || Objects.equals(initWaitAlbumRepository.idAlbumLatest().get(0), "")){
            return format+1;
        }
        String[] requestToArray = initWaitAlbumRepository.idAlbumLatest().get(0).split(format);
        return format+(Integer.parseInt(requestToArray[requestToArray.length-1])+1);
    }
}
