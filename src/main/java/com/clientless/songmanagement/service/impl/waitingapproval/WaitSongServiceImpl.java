package com.clientless.songmanagement.service.impl.waitingapproval;

import com.clientless.songmanagement.domain.waitingapproval.WaitAlbum;
import com.clientless.songmanagement.domain.waitingapproval.WaitSong;
import com.clientless.songmanagement.dto.waitingapproval.WaitSongDto;
import com.clientless.songmanagement.exception.NotFoundException;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.projection.waitingapproval.song.DetailWaitSongById;
import com.clientless.songmanagement.projection.waitingapproval.song.ListWaitSong;
import com.clientless.songmanagement.projection.waitingapproval.song.SongWaitUploadResponse;
import com.clientless.songmanagement.repository.waitingapproval.WaitAlbumRepository;
import com.clientless.songmanagement.repository.waitingapproval.WaitSongRepository;
import com.clientless.songmanagement.service.inf.waitingapproval.WaitSongService;
import com.clientless.songmanagement.service.handler.waitingapproval.IdWaitSongHandler;
import com.clientless.songmanagement.service.handler.waitingapproval.WaitDurationSongHandler;
import com.clientless.songmanagement.service.handler.waitingapproval.WaitReleasedYearHandler;
import com.clientless.songmanagement.util.GenerateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import javazoom.jl.decoder.JavaLayerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class WaitSongServiceImpl implements WaitSongService {

    @Autowired
    private WaitSongRepository waitSongRepository;
    @Autowired
    private WaitAlbumRepository waitAlbumRepository;

    Logger logger = LoggerFactory.getLogger(WaitSongServiceImpl.class);
    @Override
    public ResponseEntity<Response> upload(WaitSongDto songDto) throws IOException {
        WaitSong waitSong = new WaitSong();
        IdWaitSongHandler idWaitSongHandler = new IdWaitSongHandler(waitSongRepository);
        WaitReleasedYearHandler waitReleasedYearHandler = new WaitReleasedYearHandler();
        WaitDurationSongHandler waitDurationSongHandler = new WaitDurationSongHandler();

        waitSong.setIdSong(idWaitSongHandler.handlerId());
        waitSong.setLanguage(songDto.getLanguage());
        waitSong.setExplicit(Boolean.valueOf(songDto.getExplicit()));
        waitSong.setCreatedBy(songDto.getCreatedBy());
        waitSong.setTitleSong(songDto.getSong().getOriginalFilename());
        waitSong.setSong(songDto.getSong().getBytes());
        waitSong.setSongType(songDto.getSong().getContentType());
        waitSong.setDeleted(false);
        waitSong.setApproved(false);
        Optional<WaitAlbum> check = waitAlbumRepository.findWaitAlbumByIdAlbum(songDto.getIdAlbum());
        if(check.isPresent()){
            WaitAlbum waitAlbum = check.get();
            waitSong.setIdAlbumMappingForSong(waitAlbum);
        }else {
            throw new NotFoundException("Album with ID Album "+ songDto.getIdAlbum()+ " Not Found");
        }


        //Duration Harus di set paling akhir !!!
        waitSong.setDurationSong(Time.valueOf(waitDurationSongHandler.uploadAudio(songDto.getSong())));


        waitSongRepository.save(waitSong);

        SongWaitUploadResponse songWaitUploadResponse = getSongWaitUploadResponse(waitSong);
        songWaitUploadResponse.setArtistName(waitSong.getIdAlbumMappingForSong().getIdArtistMappingForAlbum().getArtistName());
        songWaitUploadResponse.setTitleAlbum(waitSong.getIdAlbumMappingForSong().getTitleAlbum());
        songWaitUploadResponse.setReleasedYear(waitSong.getIdAlbumMappingForSong().getReleasedYear());


        return GenerateResponse.created("Success to upload", songWaitUploadResponse);
    }

    private static SongWaitUploadResponse getSongWaitUploadResponse(WaitSong waitSong) {
        SongWaitUploadResponse songWaitUploadResponse = new SongWaitUploadResponse();
        songWaitUploadResponse.setSong(waitSong.getSong());
        songWaitUploadResponse.setIdSong(waitSong.getIdSong());
        songWaitUploadResponse.setDurationSong(waitSong.getDurationSong());
        songWaitUploadResponse.setSongType(waitSong.getSongType());
        songWaitUploadResponse.setTitleSong(waitSong.getTitleSong());
        songWaitUploadResponse.setApproved(waitSong.isApproved());
        songWaitUploadResponse.setDeleted(waitSong.isDeleted());
        songWaitUploadResponse.setCreatedBy(waitSong.getCreatedBy());
        songWaitUploadResponse.setExplicit(waitSong.getExplicit());
        songWaitUploadResponse.setLanguage(waitSong.getLanguage());
        return songWaitUploadResponse;
    }

    @Override
    public byte[] getWaitSongById(String idSong) throws IOException, LineUnavailableException, UnsupportedAudioFileException, JavaLayerException {
        Optional<WaitSong> optionalWaitSong = waitSongRepository.findWaitSongByIdSong(idSong);
        WaitSong waitSong = optionalWaitSong.orElseThrow(() -> new IllegalArgumentException("Song not found"));// Lakukan operasi lain yang memanipulasi audio jika diperlukan
        return waitSong.getSong();
    }

    @Override
    public ResponseEntity<Response> listSong(String idAlbum) throws IOException {
        List<ListWaitSong> listWaitSongs = new ArrayList<>();
        for(WaitSong waitSong:waitSongRepository.findAllByIdAlbum(idAlbum)){
            ListWaitSong listWaitSong = new ListWaitSong();
            listWaitSong.setIdSong(waitSong.getIdSong());
            listWaitSong.setTitleSong(waitSong.getTitleSong());
            listWaitSong.setArtistName(waitSong.getIdAlbumMappingForSong().getIdArtistMappingForAlbum().getArtistName());
            listWaitSong.setAlbumName(waitSong.getIdAlbumMappingForSong().getTitleAlbum());
            listWaitSong.setYear(String.valueOf(waitSong.getIdAlbumMappingForSong().getReleasedYear()));
            listWaitSong.setGenreType(waitSong.getIdAlbumMappingForSong().getGenreCodeMappingForAlbum().getGenreType());
            listWaitSong.setDurationTime(waitSong.getDurationSong());
            listWaitSongs.add(listWaitSong);
        }
        return GenerateResponse.success("Success to get List Data", listWaitSongs);
    }

    @Override
    public ResponseEntity<Response> getDetailWaitSongById(String idSong) throws JsonProcessingException {
        Optional<WaitSong> check = waitSongRepository.findWaitSongByIdSong(idSong);
        DetailWaitSongById detailWaitSongById = new DetailWaitSongById();
        if(check.isPresent()){
            WaitSong waitSong = check.get();
            detailWaitSongById.setIdSong(waitSong.getIdSong());
            detailWaitSongById.setTitleSong(waitSong.getTitleSong());
            detailWaitSongById.setDurationSong(waitSong.getDurationSong());
            detailWaitSongById.setExplicit(waitSong.getExplicit());
            detailWaitSongById.setLanguage(waitSong.getLanguage());
            detailWaitSongById.setSongType(waitSong.getSongType());
            detailWaitSongById.setCreatedBy(waitSong.getCreatedBy());
            detailWaitSongById.setDeleted(waitSong.isDeleted());
            detailWaitSongById.setApproved(waitSong.isApproved());
            detailWaitSongById.setReleasedYear(waitSong.getIdAlbumMappingForSong().getReleasedYear());
            detailWaitSongById.setTitleAlbum(waitSong.getIdAlbumMappingForSong().getTitleAlbum());
            detailWaitSongById.setArtistName(waitSong.getIdAlbumMappingForSong().getIdArtistMappingForAlbum().getArtistName());
            return GenerateResponse.success("Successfully to get Data", detailWaitSongById);
        }else {
            throw new NotFoundException("Not Found Song with Id: "+ idSong);
        }
    }

    @Override
    public ResponseEntity<Response> deleteWaitSongByIdSong(String idSong) throws JsonProcessingException {
        Optional<WaitSong> optionalSong = waitSongRepository.findWaitSongByIdSong(idSong);
        if (optionalSong.isPresent()){
            WaitSong songUpdate = optionalSong.get();
            if (!songUpdate.isDeleted()){
                songUpdate.setDeleted(true);
                waitSongRepository.save(songUpdate);
                return GenerateResponse.success("Successfully to deleted","Song with id: "+idSong);
            }else{
                songUpdate.setDeleted(false);
                waitSongRepository.save(songUpdate);
                return GenerateResponse.success("Successfully to recovery","Song with id: "+idSong);
            }
        }else {
            throw new NotFoundException(idSong);
        }
    }



}



