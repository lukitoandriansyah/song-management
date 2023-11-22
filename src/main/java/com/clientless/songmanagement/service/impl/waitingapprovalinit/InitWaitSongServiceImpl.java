package com.clientless.songmanagement.service.impl.waitingapprovalinit;

import com.clientless.songmanagement.domain.waitingapproval.WaitAlbum;
import com.clientless.songmanagement.domain.waitingapproval.WaitSong;
import com.clientless.songmanagement.domain.waitingapprovalinit.InitWaitAlbum;
import com.clientless.songmanagement.domain.waitingapprovalinit.InitWaitSong;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitSongDto;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitSongEditDto;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitSongStatusSubmitDto;
import com.clientless.songmanagement.exception.NotFoundException;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.projection.waitingapprovalinit.song.*;
import com.clientless.songmanagement.repository.mainfeature.ArtistRepository;
import com.clientless.songmanagement.repository.waitingapproval.WaitAlbumRepository;
import com.clientless.songmanagement.repository.waitingapproval.WaitSongRepository;
import com.clientless.songmanagement.repository.waitingapprovalinit.InitWaitAlbumRepository;
import com.clientless.songmanagement.repository.waitingapprovalinit.InitWaitSongRepository;
import com.clientless.songmanagement.service.handler.waitingapprovalinit.IdInitWaitSongHandler;
import com.clientless.songmanagement.service.handler.waitingapprovalinit.InitWaitDurationSongHandler;
import com.clientless.songmanagement.service.inf.waitingapprovalinit.InitWaitSongService;
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
public class InitWaitSongServiceImpl implements InitWaitSongService {

    @Autowired
    private InitWaitSongRepository initWaitSongRepository;
    @Autowired
    private InitWaitAlbumRepository initWaitAlbumRepository;
    @Autowired
    private WaitAlbumRepository waitAlbumRepository;
    @Autowired
    private WaitSongRepository waitSongRepository;
    @Autowired
    private ArtistRepository artistRepository;
    Logger logger = LoggerFactory.getLogger(InitWaitSongServiceImpl.class);
    @Override
    public ResponseEntity<Response> upload(InitWaitSongDto songDto) throws IOException {
        InitWaitSong initWaitSong = new InitWaitSong();
        IdInitWaitSongHandler idInitWaitSongHandler = new IdInitWaitSongHandler(initWaitSongRepository);
        InitWaitDurationSongHandler initWaitDurationSongHandler = new InitWaitDurationSongHandler();

        initWaitSong.setIdSong(idInitWaitSongHandler.handlerId());
        initWaitSong.setLanguage(songDto.getLanguage());
        initWaitSong.setExplicit(Boolean.valueOf(songDto.getExplicit()));
        initWaitSong.setCreatedBy(songDto.getCreatedBy());
        initWaitSong.setTitleSong(songDto.getSong().getOriginalFilename());
        initWaitSong.setSong(songDto.getSong().getBytes());
        initWaitSong.setSongType(songDto.getSong().getContentType());
        initWaitSong.setDeleted(false);
        initWaitSong.setSubmitted(false);


        Optional<InitWaitAlbum> check = initWaitAlbumRepository.findInitWaitAlbumByIdAlbum(songDto.getIdAlbum());
        if(check.isPresent()){
            InitWaitAlbum initWaitAlbum = check.get();
            initWaitSong.setIdAlbumMappingForSong(initWaitAlbum);
        }else {
            throw new NotFoundException("Album with ID Album "+ songDto.getIdAlbum()+ " Not Found");
        }

        //Duration Harus di set paling akhir !!!
        initWaitSong.setDurationSong(Time.valueOf(initWaitDurationSongHandler.uploadAudio(songDto.getSong())));

        initWaitSongRepository.save(initWaitSong);

        SongInitWaitUploadResponse songInitWaitUploadResponse = getSongInitWaitUploadResponse(initWaitSong);
        songInitWaitUploadResponse.setArtistName(initWaitSong.getIdAlbumMappingForSong().getIdArtistMappingForAlbum().getArtistName());
        songInitWaitUploadResponse.setTitleAlbum(initWaitSong.getIdAlbumMappingForSong().getTitleAlbum());
        songInitWaitUploadResponse.setReleasedYear(initWaitSong.getIdAlbumMappingForSong().getReleasedYear());

        return GenerateResponse.created("Success to upload", songInitWaitUploadResponse);
    }

    private static SongInitWaitUploadResponse getSongInitWaitUploadResponse(InitWaitSong initWaitSong) {
        SongInitWaitUploadResponse songInitWaitUploadResponse = new SongInitWaitUploadResponse();
        songInitWaitUploadResponse.setSong(initWaitSong.getSong());
        songInitWaitUploadResponse.setIdSong(initWaitSong.getIdSong());
        songInitWaitUploadResponse.setDurationSong(initWaitSong.getDurationSong());
        songInitWaitUploadResponse.setSongType(initWaitSong.getSongType());
        songInitWaitUploadResponse.setTitleSong(initWaitSong.getTitleSong());
        songInitWaitUploadResponse.setSubmitted(initWaitSong.isSubmitted());
        songInitWaitUploadResponse.setDeleted(initWaitSong.isDeleted());
        songInitWaitUploadResponse.setCreatedBy(initWaitSong.getCreatedBy());
        songInitWaitUploadResponse.setExplicit(initWaitSong.getExplicit());
        songInitWaitUploadResponse.setLanguage(initWaitSong.getLanguage());
        return songInitWaitUploadResponse;
    }

    @Override
    public byte[] getInitWaitSongById(String idSong) throws IOException, LineUnavailableException, UnsupportedAudioFileException, JavaLayerException {
        Optional<InitWaitSong> optionalInitWaitSong = initWaitSongRepository.findInitWaitSongByIdSong(idSong);
        InitWaitSong initWaitSong = optionalInitWaitSong.orElseThrow(() -> new IllegalArgumentException("Song not found"));// Lakukan operasi lain yang memanipulasi audio jika diperlukan
        return initWaitSong.getSong();
    }

    @Override
    public ResponseEntity<Response> listSong(String idAlbum) throws IOException {
        List<ListInitWaitSong> listInitWaitSongs = new ArrayList<>();
        for(InitWaitSong initWaitSong:initWaitSongRepository.findAllByIdAlbum(idAlbum)){
            ListInitWaitSong listInitWaitSong = new ListInitWaitSong();
            listInitWaitSong.setIdSong(initWaitSong.getIdSong());
            listInitWaitSong.setTitleSong(initWaitSong.getTitleSong());
            listInitWaitSong.setArtistName(initWaitSong.getIdAlbumMappingForSong().getIdArtistMappingForAlbum().getArtistName());
            listInitWaitSong.setAlbumName(initWaitSong.getIdAlbumMappingForSong().getTitleAlbum());
            listInitWaitSong.setYear(String.valueOf(initWaitSong.getIdAlbumMappingForSong().getReleasedYear()));
            listInitWaitSong.setGenreType(initWaitSong.getIdAlbumMappingForSong().getGenreCodeMappingForAlbum().getGenreType());
            listInitWaitSong.setDurationTime(initWaitSong.getDurationSong());
            listInitWaitSongs.add(listInitWaitSong);
        }
        return GenerateResponse.success("Success to get List Data", listInitWaitSongs);
    }

    @Override
    public ResponseEntity<Response> getDetailInitWaitSongById(String idSong) throws JsonProcessingException {
        Optional<InitWaitSong> check = initWaitSongRepository.findInitWaitSongByIdSong(idSong);
        DetailInitWaitSongById detailInitWaitSongById = new DetailInitWaitSongById();
        if(check.isPresent()){
            InitWaitSong initWaitSong = check.get();
            detailInitWaitSongById.setIdSong(initWaitSong.getIdSong());
            detailInitWaitSongById.setTitleSong(initWaitSong.getTitleSong());
            detailInitWaitSongById.setDurationSong(initWaitSong.getDurationSong());
            detailInitWaitSongById.setExplicit(initWaitSong.getExplicit());
            detailInitWaitSongById.setLanguage(initWaitSong.getLanguage());
            detailInitWaitSongById.setSongType(initWaitSong.getSongType());
            detailInitWaitSongById.setCreatedBy(initWaitSong.getCreatedBy());
            detailInitWaitSongById.setDeleted(initWaitSong.isDeleted());
            detailInitWaitSongById.setSubmitted(initWaitSong.isSubmitted());
            detailInitWaitSongById.setReleasedYear(initWaitSong.getIdAlbumMappingForSong().getReleasedYear());
            detailInitWaitSongById.setTitleAlbum(initWaitSong.getIdAlbumMappingForSong().getTitleAlbum());
            detailInitWaitSongById.setArtistName(initWaitSong.getIdAlbumMappingForSong().getIdArtistMappingForAlbum().getArtistName());
            return GenerateResponse.success("Successfully to get Data", detailInitWaitSongById);
        }else {
            throw new NotFoundException("Not Found Song with Id: "+ idSong);
        }
    }

    @Override
    public ResponseEntity<Response> deleteInitWaitSongByIdSong(String idSong) throws JsonProcessingException {
        Optional<InitWaitSong> optionalSong = initWaitSongRepository.findInitWaitSongByIdSong(idSong);
        if (optionalSong.isPresent()){
            InitWaitSong songUpdate = optionalSong.get();
            if (!songUpdate.isDeleted()){
                songUpdate.setDeleted(true);
                initWaitSongRepository.save(songUpdate);
                return GenerateResponse.success("Successfully to deleted","Song with id: "+idSong);
            }else{
                songUpdate.setDeleted(false);
                initWaitSongRepository.save(songUpdate);
                return GenerateResponse.success("Successfully to recovery","Song with id: "+idSong);
            }
        }else {
            throw new NotFoundException(idSong);
        }
    }

    @Override
    public ResponseEntity<Response> editStatusSubmittedSongById(String idSong, InitWaitSongStatusSubmitDto initWaitSongStatusSubmitDto) throws JsonProcessingException {
        Optional<InitWaitSong> check = initWaitSongRepository.findInitWaitSongByIdSong(idSong);
        if(check.isPresent()){
            InitWaitSong initWaitSong = check.get();
            if(initWaitSongStatusSubmitDto.getIsSubmitted()=="Submitted"){
                initWaitSong.setSubmitted(true);
                initWaitSongRepository.save(initWaitSong);

                WaitSong waitSong = new WaitSong();

                waitSong.setIdSong(initWaitSong.getIdSong());
                waitSong.setTitleSong(initWaitSong.getTitleSong());
                waitSong.setSongType(initWaitSong.getSongType());
                waitSong.setSong(initWaitSong.getSong());
                waitSong.setCreatedBy(initWaitSong.getCreatedBy());
                waitSong.setExplicit(initWaitSong.getExplicit());
                waitSong.setLanguage(initWaitSong.getLanguage());
                waitSong.setApproved(false);
                waitSong.setDeleted(false);

                Optional<WaitAlbum> checkWaitAlbum = waitAlbumRepository.findWaitAlbumByIdAlbum(initWaitSong.getIdAlbumMappingForSong().getIdAlbum());
                if(checkWaitAlbum.isPresent()){
                    WaitAlbum waitAlbum = checkWaitAlbum.get();
                    waitSong.setIdAlbumMappingForSong(waitAlbum);
                }else {
                    throw new NotFoundException("Album with ID Album "+ initWaitSong.getIdAlbumMappingForSong().getIdAlbum()+ " Not Found");
                }

                waitSong.setDurationSong(initWaitSong.getDurationSong());

                waitSongRepository.save(waitSong);

                SongInitWaitEditStatusSubmitResponse songInitWaitEditStatusSubmitResponse = getSongInitWaitEditStatusSubmitResponse(initWaitSong);
                songInitWaitEditStatusSubmitResponse.setArtistName(initWaitSong.getIdAlbumMappingForSong().getIdArtistMappingForAlbum().getArtistName());
                songInitWaitEditStatusSubmitResponse.setTitleAlbum(initWaitSong.getIdAlbumMappingForSong().getTitleAlbum());
                songInitWaitEditStatusSubmitResponse.setReleasedYear(initWaitSong.getIdAlbumMappingForSong().getReleasedYear());

                return GenerateResponse.created("Success to edit submit status: Submitted", songInitWaitEditStatusSubmitResponse);
            }else {
                initWaitSong.setSubmitted(false);
                initWaitSongRepository.save(initWaitSong);
                SongInitWaitEditStatusSubmitResponse songInitWaitEditStatusSubmitResponse = getSongInitWaitEditStatusSubmitResponse(initWaitSong);
                songInitWaitEditStatusSubmitResponse.setArtistName(initWaitSong.getIdAlbumMappingForSong().getIdArtistMappingForAlbum().getArtistName());
                songInitWaitEditStatusSubmitResponse.setTitleAlbum(initWaitSong.getIdAlbumMappingForSong().getTitleAlbum());
                songInitWaitEditStatusSubmitResponse.setReleasedYear(initWaitSong.getIdAlbumMappingForSong().getReleasedYear());
                return GenerateResponse.created("Success to edit submit status: Cancelled", songInitWaitEditStatusSubmitResponse);

            }

        }
        throw new NotFoundException(idSong);
    }

    private static SongInitWaitEditStatusSubmitResponse getSongInitWaitEditStatusSubmitResponse(InitWaitSong initWaitSong) {
        SongInitWaitEditStatusSubmitResponse songInitWaitEditStatusSubmitResponse = new SongInitWaitEditStatusSubmitResponse();
        songInitWaitEditStatusSubmitResponse.setSong(initWaitSong.getSong());
        songInitWaitEditStatusSubmitResponse.setIdSong(initWaitSong.getIdSong());
        songInitWaitEditStatusSubmitResponse.setDurationSong(initWaitSong.getDurationSong());
        songInitWaitEditStatusSubmitResponse.setSongType(initWaitSong.getSongType());
        songInitWaitEditStatusSubmitResponse.setTitleSong(initWaitSong.getTitleSong());
        songInitWaitEditStatusSubmitResponse.setSubmitted(initWaitSong.isSubmitted());
        songInitWaitEditStatusSubmitResponse.setDeleted(initWaitSong.isDeleted());
        songInitWaitEditStatusSubmitResponse.setCreatedBy(initWaitSong.getCreatedBy());
        songInitWaitEditStatusSubmitResponse.setExplicit(initWaitSong.getExplicit());
        songInitWaitEditStatusSubmitResponse.setLanguage(initWaitSong.getLanguage());
        return songInitWaitEditStatusSubmitResponse;
    }


    @Override
    public ResponseEntity<Response> editSongById(String idSong, InitWaitSongEditDto initWaitSongEditDto) throws IOException {
        Optional<InitWaitSong> check = initWaitSongRepository.findInitWaitSongByIdSong(idSong);
        if(check.isPresent()){
            InitWaitSong initWaitSong = check.get();

            if(initWaitSongEditDto.getIdAlbum()!=null || initWaitSongEditDto.getIdAlbum()!=""){
                Optional<InitWaitAlbum> checkInit = initWaitAlbumRepository.findInitWaitAlbumByIdAlbum(initWaitSongEditDto.getIdAlbum());
                if(checkInit.isPresent()){
                    InitWaitAlbum initWaitAlbum = checkInit.get();
                    initWaitSong.setIdAlbumMappingForSong(initWaitAlbum);
                }else {
                    throw new NotFoundException("Album with ID Album "+ initWaitSongEditDto.getIdAlbum()+ " Not Found");
                }
            }
            if(initWaitSongEditDto.getLanguage()!=null||initWaitSongEditDto.getLanguage()!=""){
                initWaitSong.setLanguage(initWaitSongEditDto.getLanguage());
            }

            if (initWaitSongEditDto.getExplicit()!=null||initWaitSongEditDto.getExplicit()!=""){
                initWaitSong.setExplicit(Boolean.valueOf(initWaitSongEditDto.getExplicit()));
            }
            if(initWaitSongEditDto.getCreatedBy()!=null||initWaitSongEditDto.getCreatedBy()!=""){
                initWaitSong.setCreatedBy(initWaitSongEditDto.getCreatedBy());
            }
            if (initWaitSongEditDto.getSong()!=null){
                InitWaitDurationSongHandler initWaitDurationSongHandler = new InitWaitDurationSongHandler();

                initWaitSong.setTitleSong(initWaitSongEditDto.getSong().getOriginalFilename());
                initWaitSong.setSong(initWaitSongEditDto.getSong().getBytes());
                initWaitSong.setSongType(initWaitSongEditDto.getSong().getContentType());
                //Duration Harus di set paling akhir !!!
                initWaitSong.setDurationSong(Time.valueOf(initWaitDurationSongHandler.uploadAudio(initWaitSongEditDto.getSong())));

            }

            initWaitSongRepository.save(initWaitSong);

            SongInitWaitEditResponse songInitWaitEditResponse = getSongInitWaitEditResponse(initWaitSong);
            songInitWaitEditResponse.setArtistName(initWaitSong.getIdAlbumMappingForSong().getIdArtistMappingForAlbum().getArtistName());
            songInitWaitEditResponse.setTitleAlbum(initWaitSong.getIdAlbumMappingForSong().getTitleAlbum());
            songInitWaitEditResponse.setReleasedYear(initWaitSong.getIdAlbumMappingForSong().getReleasedYear());
            return GenerateResponse.created("Success to edit data", songInitWaitEditResponse);


        }
        throw new NotFoundException(idSong);
    }

    private static SongInitWaitEditResponse getSongInitWaitEditResponse(InitWaitSong initWaitSong) {
        SongInitWaitEditResponse songInitWaitEditResponse = new SongInitWaitEditResponse();
        songInitWaitEditResponse.setSong(initWaitSong.getSong());
        songInitWaitEditResponse.setIdSong(initWaitSong.getIdSong());
        songInitWaitEditResponse.setDurationSong(initWaitSong.getDurationSong());
        songInitWaitEditResponse.setSongType(initWaitSong.getSongType());
        songInitWaitEditResponse.setTitleSong(initWaitSong.getTitleSong());
        songInitWaitEditResponse.setSubmitted(initWaitSong.isSubmitted());
        songInitWaitEditResponse.setDeleted(initWaitSong.isDeleted());
        songInitWaitEditResponse.setCreatedBy(initWaitSong.getCreatedBy());
        songInitWaitEditResponse.setExplicit(initWaitSong.getExplicit());
        songInitWaitEditResponse.setLanguage(initWaitSong.getLanguage());
        return songInitWaitEditResponse;
    }

}



