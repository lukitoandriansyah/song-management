package com.clientless.songmanagement.service.impl.mainfeature;

import com.clientless.songmanagement.domain.mainfeature.Album;
import com.clientless.songmanagement.domain.mainfeature.Artist;
import com.clientless.songmanagement.domain.mainfeature.Genre;
import com.clientless.songmanagement.domain.mainfeature.Song;
import com.clientless.songmanagement.dto.mainfeature.ArtistDto;
import com.clientless.songmanagement.exception.NotFoundException;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.projection.mainfeature.album.DetailAlbumById;
import com.clientless.songmanagement.projection.mainfeature.artist.ArtistUploadResponse;
import com.clientless.songmanagement.projection.mainfeature.artist.DetailArtisById;
import com.clientless.songmanagement.projection.mainfeature.artist.ListArtist;
import com.clientless.songmanagement.projection.mainfeature.song.ListSongForDetailAlbum;
import com.clientless.songmanagement.repository.mainfeature.AlbumRepository;
import com.clientless.songmanagement.repository.mainfeature.ArtistRepository;
import com.clientless.songmanagement.repository.mainfeature.GenreRepository;
import com.clientless.songmanagement.repository.mainfeature.SongRepository;
import com.clientless.songmanagement.service.handler.mainfeature.CodeGenreHandler;
import com.clientless.songmanagement.service.inf.mainfeature.ArtistService;
import com.clientless.songmanagement.service.handler.mainfeature.ArtistYearActiveHandler;
import com.clientless.songmanagement.service.handler.mainfeature.IdArtistHandler;
import com.clientless.songmanagement.util.GenerateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService {
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private SongRepository songRepository;


    Logger logger = LoggerFactory.getLogger(ArtistServiceImpl.class);

    @Override
    public byte[] viewImageById(String idArtist) {
        Optional<Artist> exist = artistRepository.findByIdArtist(idArtist);
        if (exist.isPresent()){
            Artist artist = exist.get();
            if (!artist.isDeleted()){
                return exist.get().getPhoto();
            }
        }
        throw new NotFoundException(idArtist);
    }

    @Override
    public ResponseEntity<Response> viewArtistById(String idArtist) throws JsonProcessingException {
        Optional<Artist> exist = artistRepository.findByIdArtist(idArtist);
        if (exist.isPresent()){
            Artist artist = exist.get();
            DetailArtisById detailArtisById = new DetailArtisById();
            detailArtisById.setIdArtist(artist.getIdArtist());
            detailArtisById.setArtistName(artist.getArtistName());
            detailArtisById.setGenreType(artist.getGenreCodeMappingForArtist().getGenreType());
            detailArtisById.setYearActive(artist.getYearActive());
            detailArtisById.setTotalAlbum(albumRepository.countAlbumByIdArtistMappingForAlbum(detailArtisById.getIdArtist()));

            List<DetailAlbumById> detailAlbumByIdList = new ArrayList<>();
            for(Album album: albumRepository.findAllByIdArtist(detailArtisById.getIdArtist())){
                DetailAlbumById detailAlbumById = new DetailAlbumById();
                detailAlbumById.setTitleAlbum(album.getTitleAlbum());
                detailAlbumById.setArtistName(album.getIdArtistMappingForAlbum().getArtistName());
                detailAlbumById.setGenre(album.getGenreCodeMappingForAlbum().getGenreType());
                detailAlbumById.setReleasedYear(album.getReleasedYear());
                detailAlbumById.setDurationAlbum(songRepository.countTotalDurationAlbumByIdAlbum(album.getIdAlbum()));
                detailAlbumById.setDeleted(album.isDeleted());
                detailAlbumById.setCoverAlbum(album.getCoverAlbum());

                List<ListSongForDetailAlbum> listSongForDetailAlbums = new ArrayList<>();
                for (Song song:songRepository.findAllByIdAlbum(album.getIdAlbum())){
                    ListSongForDetailAlbum listSongForDetailAlbum = new ListSongForDetailAlbum();
                    listSongForDetailAlbum.setTitleSong(song.getTitleSong());
                    listSongForDetailAlbum.setDurationTime(song.getDurationSong());
                    listSongForDetailAlbums.add(listSongForDetailAlbum);
                }
                detailAlbumById.setSongs(listSongForDetailAlbums);
                detailAlbumByIdList.add(detailAlbumById);

            }
            detailArtisById.setAlbums(detailAlbumByIdList);
            if (!artist.isDeleted()){
                return GenerateResponse.success("Successfully to get Object ", detailArtisById);
            }

        }
        throw new NotFoundException(idArtist);
    }

    @Override
    public ResponseEntity<Response> listArtist() throws IOException {
        try {
            List<ListArtist> artistList = new ArrayList<>();
            for(Artist s : artistRepository.findAll()){
                if(!s.isDeleted()){
                    ListArtist listArtist = new ListArtist();
                    listArtist.setIdArtist(s.getIdArtist());
                    listArtist.setArtistName(s.getArtistName());
                    listArtist.setGenreType(s.getGenreCodeMappingForArtist().getGenreType());
                    listArtist.setYearActive(s.getYearActive());
                    listArtist.setTotalAlbum(albumRepository.countAlbumByIdArtistMappingForAlbum(s.getIdArtist()));
                    artistList.add(listArtist);
                }
            }
            return GenerateResponse.success("Successfully get object", artistList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return GenerateResponse.error("Something Wrong", null);
        }
    }

    @Override
    public ResponseEntity<Response> deleteArtistByIdArtist(String idArtist) throws JsonProcessingException {
        Optional<Artist> optionalArtist = artistRepository.findByIdArtist(idArtist);
        if (optionalArtist.isPresent()){
            Artist artist = optionalArtist.get();
            if(!artist.isDeleted()){
                artist.setDeleted(true);
                artistRepository.save(artist);
                return GenerateResponse.success("Successfully to deleted","Artist with id: "+idArtist);
            }else {
                artist.setDeleted(false);
                artistRepository.save(artist);
                return GenerateResponse.success("Successfully to recovery","Artist with id: "+idArtist);
            }
//            artistRepository.delete(optionalArtist.get());
//            return GenerateResponse.success("Successfully to deleted","Artist with id: "+idArtist);
        }else {
            throw new NotFoundException(idArtist);
        }
    }

    @Override
    public ResponseEntity<Response> addArtist(ArtistDto request) throws IOException {
        Artist artist = new Artist();
        CodeGenreHandler codeGenreHandler = new CodeGenreHandler(genreRepository);
        String genreType = request.getGenreType();
        Optional<Genre> genreCheck = genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist(genreType));


        ArtistYearActiveHandler artistYearActiveHandler = new ArtistYearActiveHandler();
        IdArtistHandler idArtistHandler = new IdArtistHandler(artistRepository);
        artist.setIdArtist(idArtistHandler.handlerId());

        artist.setArtistName(request.getArtistName());
        artist.setYearActive(artistYearActiveHandler.handlerYear(request.getYearActive()));
//        artist.setGenreType(request.getGenreType());
        artist.setPhoto_type(request.getFile().getContentType());
        artist.setPhoto(request.getFile().getBytes());
        artist.setDeleted(false);

        if(genreCheck.isPresent()){
            artist.setGenreCodeMappingForArtist(genreCheck.get());
        }else {
            genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist("Other")).ifPresent(artist::setGenreCodeMappingForArtist);
        }


        artistRepository.save(artist);

        ArtistUploadResponse artistUploadResponse = getArtistUploadResponse(artist);
        artistUploadResponse.setTotalAlbum(albumRepository.countAlbumByIdArtistMappingForAlbum(artist.getIdArtist()));

        return GenerateResponse.created("Successfully to Upload",artistUploadResponse);
    }

    private static ArtistUploadResponse getArtistUploadResponse(Artist artist) {
        ArtistUploadResponse artistUploadResponse = new ArtistUploadResponse();
        artistUploadResponse.setId(artist.getId());
        artistUploadResponse.setIdArtist(artist.getIdArtist());
        artistUploadResponse.setArtistName(artist.getArtistName());
        artistUploadResponse.setPhoto(artist.getPhoto());
        artistUploadResponse.setPhoto_type(artist.getPhoto_type());
        artistUploadResponse.setYearActive(artist.getYearActive());
        artistUploadResponse.setDeleted(artist.isDeleted());
        artistUploadResponse.setGenre(artist.getGenreCodeMappingForArtist().getGenreType());
        artistUploadResponse.setGenreCode(artist.getGenreCodeMappingForArtist().getGenreCode());
        return artistUploadResponse;
    }
}
