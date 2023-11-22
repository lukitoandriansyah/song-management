package com.clientless.songmanagement.service.handler.waitingapproval;

import com.clientless.songmanagement.repository.waitingapproval.WaitArtistRepository;
import lombok.AllArgsConstructor;

import java.util.Objects;

import static com.clientless.songmanagement.util.Constants.CODE_FOR_ARTIST;
import static com.clientless.songmanagement.util.Constants.PREFIX_ID;

@AllArgsConstructor
public class IdWaitArtistHandler {
    private WaitArtistRepository waitArtistRepository;
    public String handlerId(){
        String format = PREFIX_ID+CODE_FOR_ARTIST;
        if(waitArtistRepository.idArtistLatest().size()==0||waitArtistRepository.idArtistLatest().get(0)==null || Objects.equals(waitArtistRepository.idArtistLatest().get(0), "")){
            return format+1;
        }
        String[] requestToArray = waitArtistRepository.idArtistLatest().get(0).split(format);
        return format+ (Integer.parseInt(requestToArray[requestToArray.length-1])+1);
    }
}
