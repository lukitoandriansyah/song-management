package com.clientless.songmanagement.service.impl.mainfeature;

import com.clientless.songmanagement.domain.mainfeature.Album;
import com.clientless.songmanagement.domain.mainfeature.Artist;
import com.clientless.songmanagement.domain.mainfeature.Genre;
import com.clientless.songmanagement.domain.mainfeature.Song;
import com.clientless.songmanagement.dto.mainfeature.AlbumDto;
import com.clientless.songmanagement.exception.NotFoundException;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.projection.mainfeature.album.AlbumUploadResponse;
import com.clientless.songmanagement.projection.mainfeature.album.DetailAlbumById;
import com.clientless.songmanagement.projection.mainfeature.song.ListSongForDetailAlbum;
import com.clientless.songmanagement.repository.mainfeature.ArtistRepository;
import com.clientless.songmanagement.repository.mainfeature.GenreRepository;
import com.clientless.songmanagement.repository.mainfeature.SongRepository;
import com.clientless.songmanagement.service.handler.mainfeature.CodeGenreHandler;
import com.clientless.songmanagement.service.handler.mainfeature.IdAlbumHandler;
import com.clientless.songmanagement.service.handler.mainfeature.ReleasedYearHandler;
import com.clientless.songmanagement.projection.mainfeature.album.ListAlbum;
import com.clientless.songmanagement.repository.mainfeature.AlbumRepository;
import com.clientless.songmanagement.service.inf.mainfeature.AlbumService;
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


//@AllArgsConstructor
@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private SongRepository songRepository;
    Logger logger = LoggerFactory.getLogger(AlbumServiceImpl.class);

    @Override
    public ResponseEntity<Response> uploadAlbum(AlbumDto request) throws IOException {
        Album album = new Album();
        CodeGenreHandler codeGenreHandler = new CodeGenreHandler(genreRepository);
        String genreType = request.getGenreType();
        Optional<Genre> genreCheck = genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist(genreType));

        Optional<Artist> artistCheck = artistRepository.findByIdArtist(request.getIdArtist());

        ReleasedYearHandler releasedYearHandler = new ReleasedYearHandler();
        IdAlbumHandler idAlbumHandler = new IdAlbumHandler(albumRepository);
        album.setIdAlbum(idAlbumHandler.handlerId());
        album.setTitleAlbum(request.getTitleAlbum());
        album.setReleasedYear(releasedYearHandler.handlerYear(request.getReleasedYear()));
        album.setCoverAlbumType(request.getFile().getContentType());
        album.setCoverAlbum(request.getFile().getBytes());
        album.setDeleted(false);


        if(genreCheck.isPresent()){
            album.setGenreCodeMappingForAlbum(genreCheck.get());
        }else {
            genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist("Other")).ifPresent(genreOther -> album.setGenreCodeMappingForAlbum(genreOther));
        }

        if(artistCheck.isPresent()){
            album.setIdArtistMappingForAlbum(artistCheck.get());
        }else {
            throw new NotFoundException("Artist Not Found For ID Artist: " + request.getIdArtist());
        }

        albumRepository.save(album);

        AlbumUploadResponse albumUploadResponse = getAlbumUploadResponse(album);

        return GenerateResponse.created("Successfully to Upload",albumUploadResponse);

    }

    private static AlbumUploadResponse getAlbumUploadResponse(Album album) {
        AlbumUploadResponse albumUploadResponse = new AlbumUploadResponse();
        albumUploadResponse.setId(album.getId());
        albumUploadResponse.setIdAlbum(album.getIdAlbum());
        albumUploadResponse.setGenre(album.getGenreCodeMappingForAlbum().getGenreCode());
        albumUploadResponse.setGenreCode(album.getGenreCodeMappingForAlbum().getGenreCode());
        albumUploadResponse.setIdArtist(album.getIdArtistMappingForAlbum().getIdArtist());
        albumUploadResponse.setArtistName(album.getIdArtistMappingForAlbum().getArtistName());
        albumUploadResponse.setCoverAlbum(album.getCoverAlbum());
        albumUploadResponse.setTitleAlbum(album.getTitleAlbum());
        albumUploadResponse.setCoverAlbumType(album.getCoverAlbumType());
        return albumUploadResponse;
    }

    @Override
    public byte[] viewImageById(String idAlbum) {
        Optional<Album> exist = albumRepository.findAlbumByIdAlbum(idAlbum);
        if (exist.isPresent()){
            Album album = exist.get();
            if (!album.isDeleted()){
                return exist.get().getCoverAlbum();
            }else {
                throw new NotFoundException(idAlbum);
            }
        }
        throw new NotFoundException(idAlbum);
    }

    @Override
    public ResponseEntity<Response> viewAlbumById(String idAlbum) throws JsonProcessingException {
        Optional<Album> exist = albumRepository.findAlbumByIdAlbum(idAlbum);
        if (exist.isPresent()){
            Album album = exist.get();
            DetailAlbumById detailAlbumById=new DetailAlbumById();
            detailAlbumById.setTitleAlbum(album.getTitleAlbum());
            detailAlbumById.setArtistName(album.getIdArtistMappingForAlbum().getArtistName());
            detailAlbumById.setDurationAlbum(songRepository.countTotalDurationAlbumByIdAlbum(idAlbum));
            detailAlbumById.setGenre(album.getGenreCodeMappingForAlbum().getGenreType());
            detailAlbumById.setReleasedYear(album.getReleasedYear());
            detailAlbumById.setDeleted(album.isDeleted());
            detailAlbumById.setCoverAlbum(album.getCoverAlbum());

            List<ListSongForDetailAlbum> listSongForDetailAlbums = new ArrayList<>();
            for (Song s : songRepository.findAllByIdAlbum(idAlbum)) {
                ListSongForDetailAlbum listSongForDetailAlbum = new ListSongForDetailAlbum();
                listSongForDetailAlbum.setTitleSong(s.getTitleSong());
                listSongForDetailAlbum.setDurationTime(s.getDurationSong());
                listSongForDetailAlbums.add(listSongForDetailAlbum);
            }
            detailAlbumById.setSongs(listSongForDetailAlbums);
            if (!album.isDeleted()){
                return GenerateResponse.success("Successfully to get object: ", detailAlbumById);
            }else {
                throw new NotFoundException(idAlbum);
            }
        }
        throw new NotFoundException(idAlbum);
    }

    @Override
    public ResponseEntity<Response> listAlbum() throws JsonProcessingException {
        try {
            List<ListAlbum> albumList = new ArrayList<>();
            for(Album s : albumRepository.findAll()){
                if (!s.isDeleted()){
                    ListAlbum listAlbum = new ListAlbum();
                    listAlbum.setIdAlbum(s.getIdAlbum());
                    listAlbum.setTitleAlbum(s.getTitleAlbum());
                    listAlbum.setReleasedYear(s.getReleasedYear());
                    listAlbum.setDurationTime(songRepository.countTotalDurationAlbumByIdAlbum(s.getIdAlbum()));
                    listAlbum.setGenreType(s.getGenreCodeMappingForAlbum().getGenreType());
                    listAlbum.setArtistName(s.getIdArtistMappingForAlbum().getArtistName());
                    albumList.add(listAlbum);
                }
            }

            return GenerateResponse.success("Successfully get object", albumList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return GenerateResponse.error("Something Wrong", null);
        }
    }

    @Override
    public ResponseEntity<Response> deleteAlbumByIdAlbum(String idAlbum) throws JsonProcessingException {
        Optional<Album> optionalAlbum = albumRepository.findAlbumByIdAlbum(idAlbum);
        if (optionalAlbum.isPresent()){
            Album albumUpdate = optionalAlbum.get();
            if (!albumUpdate.isDeleted()){
                albumUpdate.setDeleted(true);
                albumRepository.save(albumUpdate);
                return GenerateResponse.success("Successfully to deleted","Album with id: "+idAlbum);
            }else{
                albumUpdate.setDeleted(false);
                albumRepository.save(albumUpdate);
                return GenerateResponse.success("Successfully to recovery","Album with id: "+idAlbum);
            }
        }else {
            throw new NotFoundException(idAlbum);
        }
    }
}
