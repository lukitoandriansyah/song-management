package com.clientless.songmanagement.service.impl.mainfeature;

import com.clientless.songmanagement.domain.mainfeature.Album;
import com.clientless.songmanagement.domain.mainfeature.Song;
import com.clientless.songmanagement.dto.mainfeature.SongDto;
import com.clientless.songmanagement.exception.NotFoundException;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.projection.mainfeature.song.DetailSongById;
import com.clientless.songmanagement.projection.mainfeature.song.ListSongForDashboard;
import com.clientless.songmanagement.projection.mainfeature.song.SongUploadResponse;
import com.clientless.songmanagement.repository.mainfeature.AlbumRepository;
import com.clientless.songmanagement.repository.mainfeature.SongRepository;
import com.clientless.songmanagement.service.inf.mainfeature.SongService;
import com.clientless.songmanagement.service.handler.mainfeature.DurationSongHandler;
import com.clientless.songmanagement.service.handler.mainfeature.IdSongHandler;
import com.clientless.songmanagement.service.handler.mainfeature.ReleasedYearHandler;
import com.clientless.songmanagement.util.GenerateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import javazoom.jl.decoder.JavaLayerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SongServiceImpl implements SongService {

    @Autowired
    private SongRepository songRepository;
    @Autowired
    private AlbumRepository albumRepository;
    Logger logger = LoggerFactory.getLogger(AlbumServiceImpl.class);
    @Override
    public ResponseEntity<Response> upload(SongDto songDto) throws IOException {
        Song song = new Song();
        IdSongHandler idSongHandler = new IdSongHandler(songRepository);
        ReleasedYearHandler releasedYearHandler = new ReleasedYearHandler();
        DurationSongHandler durationSongHandler = new DurationSongHandler();
        Optional<Album> albumCheck = albumRepository.findAlbumByIdAlbum(songDto.getIdAlbum());

        song.setIdSong(idSongHandler.handlerId());
        song.setLanguage(songDto.getLanguage());
        song.setExplicit(Boolean.valueOf(songDto.getExplicit()));
        song.setCreatedBy(songDto.getCreatedBy());

        song.setTitleSong(songDto.getSong().getOriginalFilename());
        song.setSong(songDto.getSong().getBytes());
        song.setSongType(songDto.getSong().getContentType());
        song.setTotalStreaming(12L);
        song.setTotalDownload(12L);
        song.setDeleted(false);
        if(albumCheck.isPresent()){
            song.setIdAlbumMappingForSong(albumCheck.get());
        }else {
            throw new NotFoundException("Album with ID Album "+ songDto.getIdAlbum()+ " Not Found");
        }


        //Duration Harus di set paling akhir !!!
        song.setDurationSong(Time.valueOf(durationSongHandler.uploadAudio(songDto.getSong())));

        songRepository.save(song);

        SongUploadResponse songUploadResponse=getSongUploadResponse(song);
        songUploadResponse.setArtistName(song.getIdAlbumMappingForSong().getIdArtistMappingForAlbum().getArtistName());
        songUploadResponse.setTitleAlbum(song.getIdAlbumMappingForSong().getTitleAlbum());
        songUploadResponse.setReleasedYear(song.getIdAlbumMappingForSong().getReleasedYear());


        return GenerateResponse.created("Success to upload", songUploadResponse);
    }

    private static SongUploadResponse getSongUploadResponse(Song song){
        SongUploadResponse songUploadResponse = new SongUploadResponse();
        songUploadResponse.setIdSong(song.getIdSong());
        songUploadResponse.setSong(song.getSong());
        songUploadResponse.setDurationSong(song.getDurationSong());
        songUploadResponse.setSongType(song.getSongType());
        songUploadResponse.setTitleSong(song.getTitleSong());
        songUploadResponse.setCreatedBy(song.getCreatedBy());
        songUploadResponse.setDeleted(song.isDeleted());
        songUploadResponse.setExplicit(song.getExplicit());
        songUploadResponse.setLanguage(song.getLanguage());
        songUploadResponse.setTotalDownload(song.getTotalDownload());
        songUploadResponse.setTotalStreaming(song.getTotalStreaming());

        return songUploadResponse;
    }

    @Override
    public byte[] getSongById(String idSong) throws LineUnavailableException, UnsupportedAudioFileException, JavaLayerException {
        Optional<Song> optionalSong = songRepository.findSongByIdSong(idSong);
        Song song = optionalSong.orElseThrow(() -> new IllegalArgumentException("Song not found"));// Lakukan operasi lain yang memanipulasi audio jika diperlukan
        if(!song.isDeleted()){
            return song.getSong();
        }else {
            throw new IllegalArgumentException("Song not Found");
        }
    }

    @Override
    public ResponseEntity<Response> listSong() throws IOException {
        List<ListSongForDashboard> listSongForDashboards = new ArrayList<>();
        for(Song song:songRepository.findAll()){
            ListSongForDashboard listSongForDashboard = new ListSongForDashboard();
            listSongForDashboard.setIdSong(song.getIdSong());
            listSongForDashboard.setTitleSong(song.getTitleSong());
            listSongForDashboard.setArtistName(song.getIdAlbumMappingForSong().getIdArtistMappingForAlbum().getArtistName());
            listSongForDashboard.setAlbumName(song.getIdAlbumMappingForSong().getTitleAlbum());
            listSongForDashboard.setYear(String.valueOf(song.getIdAlbumMappingForSong().getReleasedYear()));
            listSongForDashboard.setGenreType(song.getIdAlbumMappingForSong().getGenreCodeMappingForAlbum().getGenreType());
            listSongForDashboard.setDurationTime(song.getDurationSong());
            listSongForDashboards.add(listSongForDashboard);
        }
        return GenerateResponse.success("Success to get List Data", listSongForDashboards);
    }

    @Override
    public byte[] viewSongById(String idSong) throws LineUnavailableException, UnsupportedAudioFileException, JavaLayerException {
        Optional<Song> optionalSong = songRepository.findSongByIdSong(idSong);
        Song song = optionalSong.orElseThrow(() -> new IllegalArgumentException("Song not found"));// Lakukan operasi lain yang memanipulasi audio jika diperlukan
        if(!song.isDeleted()){
            return song.getIdAlbumMappingForSong().getCoverAlbum();
        }else {
            throw new IllegalArgumentException("Song not Found");
        }
    }

    @Override
    public ResponseEntity<Response> getDetailSongById(String idSong) throws JsonProcessingException {
        Optional<Song> optionalSong = songRepository.findSongByIdSong(idSong);
        DetailSongById detailSongById = new DetailSongById();
        if(optionalSong.isPresent()){
            Song song = optionalSong.get();
            detailSongById.setIdSong(song.getIdSong());
            detailSongById.setTitleSong(song.getTitleSong());
            detailSongById.setDurationSong(song.getDurationSong());
            detailSongById.setExplicit(song.getExplicit());
            detailSongById.setLanguage(song.getLanguage());
            detailSongById.setSongType(song.getSongType());
            detailSongById.setCreatedBy(song.getCreatedBy());
            detailSongById.setTotalDownload(song.getTotalDownload());
            detailSongById.setTotalStreaming(song.getTotalStreaming());
            detailSongById.setDeleted(song.isDeleted());
            detailSongById.setReleasedYear(song.getIdAlbumMappingForSong().getReleasedYear());
            detailSongById.setTitleAlbum(song.getIdAlbumMappingForSong().getTitleAlbum());
            detailSongById.setArtistName(song.getIdAlbumMappingForSong().getIdArtistMappingForAlbum().getArtistName());
            return GenerateResponse.success("Successfully to get Data", detailSongById);
        }else {
            throw new NotFoundException("Not Found Song with Id: "+ idSong);
        }
    }

    @Override
    public ResponseEntity<Response> deleteSongByIdSong(String idSong) throws JsonProcessingException {
        Optional<Song> optionalSong = songRepository.findSongByIdSong(idSong);
        if (optionalSong.isPresent()){
            Song songUpdate = optionalSong.get();
            if (!songUpdate.isDeleted()){
                songUpdate.setDeleted(true);
                songRepository.save(songUpdate);
                return GenerateResponse.success("Successfully to deleted","Song with id: "+idSong);
            }else{
                songUpdate.setDeleted(false);
                songRepository.save(songUpdate);
                return GenerateResponse.success("Successfully to recovery","Song with id: "+idSong);
            }
        }else {
            throw new NotFoundException(idSong);
        }
    }


}



