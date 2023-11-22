package com.clientless.songmanagement.service.impl.waitingapproval;

import com.clientless.songmanagement.domain.mainfeature.Album;
import com.clientless.songmanagement.domain.mainfeature.Artist;
import com.clientless.songmanagement.domain.mainfeature.Genre;
import com.clientless.songmanagement.domain.waitingapproval.WaitAlbum;
import com.clientless.songmanagement.domain.waitingapproval.WaitSong;
import com.clientless.songmanagement.dto.waitingapproval.WaitAlbumDto;
import com.clientless.songmanagement.dto.waitingapproval.WaitAlbumEditDto;
import com.clientless.songmanagement.dto.waitingapproval.WaitAlbumStatusApproveDto;
import com.clientless.songmanagement.exception.NotFoundException;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.projection.waitingapproval.album.*;
import com.clientless.songmanagement.projection.waitingapproval.song.ListSongForDetailWaitAlbum;
import com.clientless.songmanagement.projection.waitingapprovalinit.album.AlbumInitWaitEditResponse;
import com.clientless.songmanagement.repository.mainfeature.AlbumRepository;
import com.clientless.songmanagement.repository.mainfeature.ArtistRepository;
import com.clientless.songmanagement.repository.mainfeature.GenreRepository;
import com.clientless.songmanagement.repository.mainfeature.SongRepository;
import com.clientless.songmanagement.repository.waitingapproval.WaitAlbumRepository;
import com.clientless.songmanagement.repository.waitingapproval.WaitSongRepository;
import com.clientless.songmanagement.service.handler.mainfeature.CodeGenreHandler;
import com.clientless.songmanagement.service.inf.waitingapproval.WaitAlbumService;
import com.clientless.songmanagement.service.handler.waitingapproval.IdWaitAlbumHandler;
import com.clientless.songmanagement.service.handler.waitingapproval.WaitReleasedYearHandler;
import com.clientless.songmanagement.util.GenerateResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


//@AllArgsConstructor
@Service
public class WaitAlbumServiceImpl implements WaitAlbumService {

    @Autowired
    private WaitAlbumRepository waitAlbumRepository;
    @Autowired
    private WaitSongRepository waitSongRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private SongRepository songRepository;
    Logger logger = LoggerFactory.getLogger(WaitAlbumServiceImpl.class);

    @Override
    public ResponseEntity<Response> uploadWaitAlbum(WaitAlbumDto request) throws IOException {
        WaitAlbum waitAlbum = new WaitAlbum();
        CodeGenreHandler codeGenreHandler = new CodeGenreHandler(genreRepository);
        String genreType = request.getGenreType();
        Optional<Genre> genreCheck = genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist(genreType));

        Optional<Artist> artistCheck = artistRepository.findByIdArtist(request.getIdArtist());

        WaitReleasedYearHandler waitReleasedYearHandler = new WaitReleasedYearHandler();
        IdWaitAlbumHandler idWaitAlbumHandler = new IdWaitAlbumHandler(waitAlbumRepository);
        waitAlbum.setIdAlbum(idWaitAlbumHandler.handlerId());
        waitAlbum.setTitleAlbum(request.getTitleAlbum());
        waitAlbum.setReleasedYear(waitReleasedYearHandler.handlerYear(request.getReleasedYear()));
        waitAlbum.setCoverAlbumType(request.getFile().getContentType());
        waitAlbum.setCoverAlbum(request.getFile().getBytes());
        waitAlbum.setDeleted(false);
        waitAlbum.setApproved(false);

        if(genreCheck.isPresent()){
            waitAlbum.setGenreCodeMappingForAlbum(genreCheck.get());
        }else {
            genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist("Other")).ifPresent(genreOther -> waitAlbum.setGenreCodeMappingForAlbum(genreOther));
        }

        if(artistCheck.isPresent()){
            waitAlbum.setIdArtistMappingForAlbum(artistCheck.get());
        }else {
            throw new NotFoundException("Artist Not Found For ID Artist: " + request.getIdArtist());
        }

        waitAlbumRepository.save(waitAlbum);

        AlbumWaitUploadResponse albumWaitUploadResponse = getAlbumWaitUploadResponse(waitAlbum);


        return GenerateResponse.created("Successfully to Upload",albumWaitUploadResponse);
    }

    private static AlbumWaitUploadResponse getAlbumWaitUploadResponse(WaitAlbum waitAlbum) {
        AlbumWaitUploadResponse albumWaitUploadResponse = new AlbumWaitUploadResponse();
        albumWaitUploadResponse.setId(waitAlbum.getId());
        albumWaitUploadResponse.setIdAlbum(waitAlbum.getIdAlbum());
        albumWaitUploadResponse.setGenre(waitAlbum.getGenreCodeMappingForAlbum().getGenreCode());
        albumWaitUploadResponse.setGenreCode(waitAlbum.getGenreCodeMappingForAlbum().getGenreCode());
        albumWaitUploadResponse.setIdArtist(waitAlbum.getIdArtistMappingForAlbum().getIdArtist());
        albumWaitUploadResponse.setArtistName(waitAlbum.getIdArtistMappingForAlbum().getArtistName());
        albumWaitUploadResponse.setCoverAlbum(waitAlbum.getCoverAlbum());
        albumWaitUploadResponse.setTitleAlbum(waitAlbum.getTitleAlbum());
        albumWaitUploadResponse.setCoverAlbumType(waitAlbum.getCoverAlbumType());
        return albumWaitUploadResponse;
    }

    @Override
    public byte[] viewImageById(String idAlbum) {
        Optional<WaitAlbum> exist = waitAlbumRepository.findWaitAlbumByIdAlbum(idAlbum);
        if (exist.isPresent()){
            return exist.get().getCoverAlbum();

        }
        throw new NotFoundException(idAlbum);
    }

    @Override
    public ResponseEntity<Response> listWaitAlbum() throws JsonProcessingException {
        try {
            List<ListWaitAlbum> waitAlbumList = new ArrayList<>();
            for(WaitAlbum s : waitAlbumRepository.findAll()){
                ListWaitAlbum listWaitAlbum = new ListWaitAlbum();
                listWaitAlbum.setIdAlbum(s.getIdAlbum());
                listWaitAlbum.setTitleAlbum(s.getTitleAlbum());
                listWaitAlbum.setReleasedYear(s.getReleasedYear());
                listWaitAlbum.setGenreType(s.getGenreCodeMappingForAlbum().getGenreType());
                listWaitAlbum.setArtistName(s.getIdArtistMappingForAlbum().getArtistName());
                listWaitAlbum.setDurationTime(waitSongRepository.countTotalDurationAlbumByIdAlbum(listWaitAlbum.getIdAlbum()));
                waitAlbumList.add(listWaitAlbum);
            }

            return GenerateResponse.success("Successfully get object", waitAlbumList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return GenerateResponse.error("Something Wrong", null);
        }
    }

    @Override
    public ResponseEntity<Response> deleteWaitAlbumByIdAlbum(String idAlbum) throws JsonProcessingException {
        Optional<WaitAlbum> optionalWaitAlbum = waitAlbumRepository.findWaitAlbumByIdAlbum(idAlbum);
        if (optionalWaitAlbum.isPresent()){
            WaitAlbum waitAlbum = optionalWaitAlbum.get();
            if(!waitAlbum.isDeleted()){
                waitAlbum.setDeleted(true);
                waitAlbumRepository.save(waitAlbum);
                return GenerateResponse.success("Successfully to deleted","Album with id: "+idAlbum);
            }else {
                waitAlbum.setDeleted(false);
                waitAlbumRepository.save(waitAlbum);
                return GenerateResponse.success("Successfully to Recovery","Album with id: "+idAlbum);
            }
        }else {
            throw new NotFoundException(idAlbum);
        }
    }

    @Override
    public ResponseEntity<Response> viewWaitAlbumById(String idAlbum) throws JsonProcessingException {
        Optional<WaitAlbum> exist = waitAlbumRepository.findWaitAlbumByIdAlbum(idAlbum);
        if (exist.isPresent()){
            WaitAlbum waitAlbum = exist.get();
            DetailWaitAlbumById detailWaitAlbumById = new DetailWaitAlbumById();
            detailWaitAlbumById.setTitleAlbum(waitAlbum.getTitleAlbum());
            detailWaitAlbumById.setArtistName(waitAlbum.getIdArtistMappingForAlbum().getArtistName());
            detailWaitAlbumById.setDurationAlbum(waitSongRepository.countTotalDurationAlbumByIdAlbum(idAlbum));
            detailWaitAlbumById.setGenre(waitAlbum.getGenreCodeMappingForAlbum().getGenreType());
            detailWaitAlbumById.setReleasedYear(waitAlbum.getReleasedYear());
            detailWaitAlbumById.setDeleted(waitAlbum.isDeleted());
            detailWaitAlbumById.setCoverAlbum(waitAlbum.getCoverAlbum());

            List<ListSongForDetailWaitAlbum> listSongForDetailWaitAlbums = new ArrayList<>();
            for (WaitSong s : waitSongRepository.findAllByIdAlbum(idAlbum)) {
                ListSongForDetailWaitAlbum listSongForDetailWaitAlbum = new ListSongForDetailWaitAlbum();
                listSongForDetailWaitAlbum.setTitleSong(s.getTitleSong());
                listSongForDetailWaitAlbum.setDurationTime(s.getDurationSong());
                listSongForDetailWaitAlbums.add(listSongForDetailWaitAlbum);
            }
            detailWaitAlbumById.setSongs(listSongForDetailWaitAlbums);
            if (!waitAlbum.isDeleted()){
                return GenerateResponse.success("Successfully to get object: ", detailWaitAlbumById);
            }else {
                throw new NotFoundException(idAlbum);
            }
        }
        throw new NotFoundException(idAlbum);
    }

    @Override
    public ResponseEntity<Response> editStatusApproveAlbumById(String idAlbum, WaitAlbumStatusApproveDto waitAlbumStatusApproveDto) throws JsonProcessingException {
        Optional<WaitAlbum> check = waitAlbumRepository.findWaitAlbumByIdAlbum(idAlbum);
        if(check.isPresent()){
            WaitAlbum waitAlbum = check.get();
            CodeGenreHandler codeGenreHandler = new CodeGenreHandler(genreRepository);
            String genreType = waitAlbum.getGenreCodeMappingForAlbum().getGenreType();
            Optional<Genre> genreCheck = genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist(genreType));

            Optional<Artist> artistCheck = artistRepository.findByIdArtist(waitAlbum.getIdArtistMappingForAlbum().getIdArtist());

            if(Objects.equals(waitAlbumStatusApproveDto.getIsApproved(), "Approved")){
                waitAlbum.setApproved(true);
                waitAlbumRepository.save(waitAlbum);

                Album album = new Album();
                album.setIdAlbum(waitAlbum.getIdAlbum());
                album.setTitleAlbum(waitAlbum.getTitleAlbum());
                album.setReleasedYear(waitAlbum.getReleasedYear());
                album.setDeleted(waitAlbum.isDeleted());
                album.setCoverAlbum(waitAlbum.getCoverAlbum());
                album.setCoverAlbumType(waitAlbum.getCoverAlbumType());
                if(genreCheck.isPresent()){
                    album.setGenreCodeMappingForAlbum(genreCheck.get());
                }else {
                    genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist("Other")).ifPresent(genreOther -> album.setGenreCodeMappingForAlbum(genreOther));
                }

                if(artistCheck.isPresent()){
                    album.setIdArtistMappingForAlbum(artistCheck.get());
                }else {
                    throw new NotFoundException("Artist Not Found For ID Artist: " + waitAlbum.getIdArtistMappingForAlbum().getIdArtist());
                }
                albumRepository.save(album);
                AlbumWaitEditStatusApproveResponse albumWaitEditStatusApproveResponse = getAlbumWaitEditStatusApproveResponse(waitAlbum);
                return GenerateResponse.created("Successfully to Edit Status Approve",albumWaitEditStatusApproveResponse);

            }else {
                waitAlbum.setApproved(false);
                waitAlbum.setRemarks(waitAlbumStatusApproveDto.getRemarks());
                waitAlbumRepository.save(waitAlbum);
                AlbumWaitEditStatusApproveResponse albumWaitEditStatusApproveResponse = getAlbumWaitEditStatusApproveResponse(waitAlbum);
                return GenerateResponse.created("Successfully to Edit Status Reject",albumWaitEditStatusApproveResponse);



            }
        }


        throw new NotFoundException(idAlbum);
    }


    private static AlbumWaitEditStatusApproveResponse getAlbumWaitEditStatusApproveResponse(WaitAlbum waitAlbum) {
        AlbumWaitEditStatusApproveResponse albumWaitEditStatusApproveResponse = new AlbumWaitEditStatusApproveResponse();
        albumWaitEditStatusApproveResponse.setId(waitAlbum.getId());
        albumWaitEditStatusApproveResponse.setIdAlbum(waitAlbum.getIdAlbum());
        albumWaitEditStatusApproveResponse.setGenre(waitAlbum.getGenreCodeMappingForAlbum().getGenreCode());
        albumWaitEditStatusApproveResponse.setGenreCode(waitAlbum.getGenreCodeMappingForAlbum().getGenreCode());
        albumWaitEditStatusApproveResponse.setIdArtist(waitAlbum.getIdArtistMappingForAlbum().getIdArtist());
        albumWaitEditStatusApproveResponse.setArtistName(waitAlbum.getIdArtistMappingForAlbum().getArtistName());
        albumWaitEditStatusApproveResponse.setCoverAlbum(waitAlbum.getCoverAlbum());
        albumWaitEditStatusApproveResponse.setTitleAlbum(waitAlbum.getTitleAlbum());
        albumWaitEditStatusApproveResponse.setCoverAlbumType(waitAlbum.getCoverAlbumType());
        return albumWaitEditStatusApproveResponse;
    }

    @Override
    public ResponseEntity<Response> editAlbumById(String idAlbum, WaitAlbumEditDto waitAlbumEditDto) throws IOException {
        Optional<WaitAlbum> check = waitAlbumRepository.findWaitAlbumByIdAlbum(idAlbum);
        if(check.isPresent()){
            WaitAlbum waitAlbum = check.get();
            if(waitAlbumEditDto.getGenreType()!=null||waitAlbumEditDto.getGenreType()!=""){
                CodeGenreHandler codeGenreHandler = new CodeGenreHandler(genreRepository);
                String genreType = waitAlbumEditDto.getGenreType();
                Optional<Genre> genreCheck = genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist(genreType));
                if(genreCheck.isPresent()){
                    waitAlbum.setGenreCodeMappingForAlbum(genreCheck.get());
                }else {
                    genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist("Other")).ifPresent(genreOther -> waitAlbum.setGenreCodeMappingForAlbum(genreOther));
                }
            }

            if(waitAlbumEditDto.getIdArtist()!=null||waitAlbumEditDto.getIdArtist()!=""){
                Optional<Artist> artistCheck = artistRepository.findByIdArtist(waitAlbumEditDto.getIdArtist());
                if(artistCheck.isPresent()){
                    waitAlbum.setIdArtistMappingForAlbum(artistCheck.get());
                }else {
                    throw new NotFoundException("Artist Not Found For ID Artist: " + waitAlbumEditDto.getIdArtist());
                }


            }

            if(waitAlbumEditDto.getReleasedYear()!=null||waitAlbumEditDto.getReleasedYear()!=""){
                WaitReleasedYearHandler waitReleasedYearHandler = new WaitReleasedYearHandler();
                waitAlbum.setReleasedYear(waitReleasedYearHandler.handlerYear(waitAlbumEditDto.getReleasedYear()));

            }

            if(waitAlbumEditDto.getTitleAlbum()!=null||waitAlbumEditDto.getTitleAlbum()!=""){
                waitAlbum.setTitleAlbum(waitAlbumEditDto.getTitleAlbum());

            }

            if(waitAlbumEditDto.getFile()!=null){
                waitAlbum.setCoverAlbum(waitAlbumEditDto.getFile().getBytes());
                waitAlbum.setCoverAlbumType(waitAlbumEditDto.getFile().getContentType());
            }


            waitAlbumRepository.save(waitAlbum);
            AlbumWaitEditResponse albumWaitEditResponse = getAlbumWaitEditResponse(waitAlbum);
            return GenerateResponse.created("Successfully to Edit Data",albumWaitEditResponse);


        }
        throw new NotFoundException(idAlbum);
    }

    private static AlbumWaitEditResponse getAlbumWaitEditResponse(WaitAlbum waitAlbum) {
        AlbumWaitEditResponse albumWaitEditResponse = new AlbumWaitEditResponse();
        albumWaitEditResponse.setId(waitAlbum.getId());
        albumWaitEditResponse.setIdAlbum(waitAlbum.getIdAlbum());
        albumWaitEditResponse.setGenre(waitAlbum.getGenreCodeMappingForAlbum().getGenreCode());
        albumWaitEditResponse.setGenreCode(waitAlbum.getGenreCodeMappingForAlbum().getGenreCode());
        albumWaitEditResponse.setIdArtist(waitAlbum.getIdArtistMappingForAlbum().getIdArtist());
        albumWaitEditResponse.setArtistName(waitAlbum.getIdArtistMappingForAlbum().getArtistName());
        albumWaitEditResponse.setCoverAlbum(waitAlbum.getCoverAlbum());
        albumWaitEditResponse.setTitleAlbum(waitAlbum.getTitleAlbum());
        albumWaitEditResponse.setCoverAlbumType(waitAlbum.getCoverAlbumType());
        return albumWaitEditResponse;
    }

}
