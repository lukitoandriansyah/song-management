package com.clientless.songmanagement.service.handler.mainfeature;

import com.clientless.songmanagement.repository.mainfeature.ArtistRepository;
import lombok.AllArgsConstructor;

import java.util.Objects;

import static com.clientless.songmanagement.util.Constants.CODE_FOR_ARTIST;
import static com.clientless.songmanagement.util.Constants.PREFIX_ID;

@AllArgsConstructor
public class IdArtistHandler {
    private ArtistRepository artistRepository;
    public String handlerId(){
        String format = PREFIX_ID+CODE_FOR_ARTIST;
        if(artistRepository.idArtistLatest().isEmpty() ||artistRepository.idArtistLatest().get(0)==null || Objects.equals(artistRepository.idArtistLatest().get(0), "")){
            return format+1;
        }
        String[] requestToArray = artistRepository.idArtistLatest().get(0).split(format);
        return format+ (Integer.parseInt(requestToArray[requestToArray.length-1])+1);
    }
}
