package com.clientless.songmanagement.service.handler.mainfeature;

import com.clientless.songmanagement.repository.mainfeature.AlbumRepository;
import lombok.AllArgsConstructor;

import java.util.Objects;

import static com.clientless.songmanagement.util.Constants.*;

@AllArgsConstructor
public class IdAlbumHandler {

    private AlbumRepository albumRepository;
    public String handlerId(){
        String format = PREFIX_ID+CODE_FOR_ALBUM;
        if(albumRepository.findAll().isEmpty() || albumRepository.idAlbumLatest().get(0)==null || Objects.equals(albumRepository.idAlbumLatest().get(0), "")){
            return format+1;
        }
        String[] requestToArray = albumRepository.idAlbumLatest().get(0).split(format);
        return format+(Integer.parseInt(requestToArray[requestToArray.length-1])+1);
    }
}
