package com.clientless.songmanagement.service.handler.waitingapprovalinit;

import com.clientless.songmanagement.repository.waitingapprovalinit.InitWaitSongRepository;
import lombok.AllArgsConstructor;

import java.util.Objects;

import static com.clientless.songmanagement.util.Constants.CODE_FOR_SONG;
import static com.clientless.songmanagement.util.Constants.PREFIX_ID;

@AllArgsConstructor
public class IdInitWaitSongHandler {
    private InitWaitSongRepository initWaitSongRepository;
    public String handlerId(){
        String format = PREFIX_ID+CODE_FOR_SONG;
        if(initWaitSongRepository.idInitWaitSongLatest().isEmpty() || initWaitSongRepository.idInitWaitSongLatest().get(0)==null || Objects.equals(initWaitSongRepository.idInitWaitSongLatest().get(0), "")){
            return format+1;
        }
        String[] requestToArray = initWaitSongRepository.idInitWaitSongLatest().get(0).split(format);
        return format+ (Integer.parseInt(requestToArray[requestToArray.length-1])+1);
    }

}
