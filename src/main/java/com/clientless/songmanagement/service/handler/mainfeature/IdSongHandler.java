package com.clientless.songmanagement.service.handler.mainfeature;

import com.clientless.songmanagement.repository.mainfeature.SongRepository;
import lombok.AllArgsConstructor;

import java.util.Objects;

import static com.clientless.songmanagement.util.Constants.*;

@AllArgsConstructor
public class IdSongHandler {
    private SongRepository songRepository;
    public String handlerId(){
        String format = PREFIX_ID+CODE_FOR_SONG;
        if(songRepository.idSongLatest().size()==0|| songRepository.idSongLatest().get(0)==null || Objects.equals(songRepository.idSongLatest().get(0), "")){
            return format+1;
        }
        String[] requestToArray = songRepository.idSongLatest().get(0).split(format);
        return format+ (Integer.parseInt(requestToArray[requestToArray.length-1])+1);
    }

}
