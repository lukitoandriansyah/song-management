package com.clientless.songmanagement.service.handler.waitingapproval;

import com.clientless.songmanagement.repository.waitingapproval.WaitSongRepository;
import lombok.AllArgsConstructor;

import java.util.Objects;

import static com.clientless.songmanagement.util.Constants.CODE_FOR_SONG;
import static com.clientless.songmanagement.util.Constants.PREFIX_ID;

@AllArgsConstructor
public class IdWaitSongHandler {
    private WaitSongRepository waitSongRepository;
    public String handlerId(){
        String format = PREFIX_ID+CODE_FOR_SONG;
        if(waitSongRepository.idWaitSongLatest().size()==0|| waitSongRepository.idWaitSongLatest().get(0)==null || Objects.equals(waitSongRepository.idWaitSongLatest().get(0), "")){
            return format+1;
        }
        String[] requestToArray = waitSongRepository.idWaitSongLatest().get(0).split(format);
        return format+ (Integer.parseInt(requestToArray[requestToArray.length-1])+1);
    }

}
