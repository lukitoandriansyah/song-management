package com.clientless.songmanagement.service.impl.waitingapprovalinit;

import com.clientless.songmanagement.domain.mainfeature.Artist;
import com.clientless.songmanagement.domain.mainfeature.Genre;
import com.clientless.songmanagement.domain.waitingapproval.WaitAlbum;
import com.clientless.songmanagement.domain.waitingapprovalinit.InitWaitAlbum;
import com.clientless.songmanagement.domain.waitingapprovalinit.InitWaitSong;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitAlbumDto;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitAlbumEditDto;
import com.clientless.songmanagement.dto.waitingapprovalinit.InitWaitAlbumStatusSubmitDto;
import com.clientless.songmanagement.exception.NotFoundException;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.projection.waitingapprovalinit.album.*;
import com.clientless.songmanagement.projection.waitingapprovalinit.song.ListSongForDetailInitWaitAlbum;
import com.clientless.songmanagement.repository.mainfeature.ArtistRepository;
import com.clientless.songmanagement.repository.mainfeature.GenreRepository;
import com.clientless.songmanagement.repository.waitingapproval.WaitAlbumRepository;
import com.clientless.songmanagement.repository.waitingapprovalinit.InitWaitAlbumRepository;
import com.clientless.songmanagement.repository.waitingapprovalinit.InitWaitSongRepository;
import com.clientless.songmanagement.service.handler.mainfeature.CodeGenreHandler;
import com.clientless.songmanagement.service.inf.waitingapprovalinit.InitWaitAlbumService;
import com.clientless.songmanagement.service.handler.waitingapprovalinit.IdInitWaitAlbumHandler;
import com.clientless.songmanagement.service.handler.waitingapprovalinit.InitWaitReleasedYearHandler;
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
import java.util.Objects;
import java.util.Optional;


//@AllArgsConstructor
@Service
public class InitWaitAlbumServiceImpl implements InitWaitAlbumService {

    @Autowired
    private InitWaitAlbumRepository initWaitAlbumRepository;

    @Autowired
    private WaitAlbumRepository waitAlbumRepository;

    @Autowired
    private InitWaitSongRepository initWaitSongRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ArtistRepository artistRepository;

    Logger logger = LoggerFactory.getLogger(InitWaitAlbumServiceImpl.class);

    @Override
    public ResponseEntity<Response> uploadInitWaitAlbum(InitWaitAlbumDto request) throws IOException {
        InitWaitAlbum initWaitAlbum = new InitWaitAlbum();
        CodeGenreHandler codeGenreHandler = new CodeGenreHandler(genreRepository);
        String genreType = request.getGenreType();
        Optional<Genre> genreCheck = genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist(genreType));

        Optional<Artist> artistCheck = artistRepository.findByIdArtist(request.getIdArtist());


        InitWaitReleasedYearHandler initWaitReleasedYearHandler = new InitWaitReleasedYearHandler();
        IdInitWaitAlbumHandler idInitWaitAlbumHandler = new IdInitWaitAlbumHandler(initWaitAlbumRepository);
        initWaitAlbum.setIdAlbum(idInitWaitAlbumHandler.handlerId());
        initWaitAlbum.setTitleAlbum(request.getTitleAlbum());
        initWaitAlbum.setReleasedYear(initWaitReleasedYearHandler.handlerYear(request.getReleasedYear()));
        initWaitAlbum.setCoverAlbumType(request.getFile().getContentType());
        initWaitAlbum.setCoverAlbum(request.getFile().getBytes());
        initWaitAlbum.setDeleted(false);
        initWaitAlbum.setSubmitted(false);

        if(genreCheck.isPresent()){
            initWaitAlbum.setGenreCodeMappingForAlbum(genreCheck.get());
        }else {
            genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist("Other")).ifPresent(genreOther -> initWaitAlbum.setGenreCodeMappingForAlbum(genreOther));
        }

        if(artistCheck.isPresent()){
            initWaitAlbum.setIdArtistMappingForAlbum(artistCheck.get());
        }else {
            throw new NotFoundException("Artist Not Found For ID Artist: " + request.getIdArtist());
        }

        initWaitAlbumRepository.save(initWaitAlbum);
        AlbumInitWaitUploadResponse albumInitWaitUploadResponse = getAlbumInitWaitUploadResponse(initWaitAlbum);

        return GenerateResponse.created("Successfully to Upload",albumInitWaitUploadResponse);
    }

    private static AlbumInitWaitUploadResponse getAlbumInitWaitUploadResponse(InitWaitAlbum initWaitAlbum) {
        AlbumInitWaitUploadResponse albumInitWaitUploadResponse = new AlbumInitWaitUploadResponse();
        albumInitWaitUploadResponse.setId(initWaitAlbum.getId());
        albumInitWaitUploadResponse.setIdAlbum(initWaitAlbum.getIdAlbum());
        albumInitWaitUploadResponse.setGenre(initWaitAlbum.getGenreCodeMappingForAlbum().getGenreCode());
        albumInitWaitUploadResponse.setGenreCode(initWaitAlbum.getGenreCodeMappingForAlbum().getGenreCode());
        albumInitWaitUploadResponse.setIdArtist(initWaitAlbum.getIdArtistMappingForAlbum().getIdArtist());
        albumInitWaitUploadResponse.setArtistName(initWaitAlbum.getIdArtistMappingForAlbum().getArtistName());
        albumInitWaitUploadResponse.setCoverAlbum(initWaitAlbum.getCoverAlbum());
        albumInitWaitUploadResponse.setTitleAlbum(initWaitAlbum.getTitleAlbum());
        albumInitWaitUploadResponse.setCoverAlbumType(initWaitAlbum.getCoverAlbumType());
        return albumInitWaitUploadResponse;
    }

    @Override
    public byte[] viewImageById(String idAlbum) {
        Optional<InitWaitAlbum> exist = initWaitAlbumRepository.findInitWaitAlbumByIdAlbum(idAlbum);
        if (exist.isPresent()){
            return exist.get().getCoverAlbum();

        }
        throw new NotFoundException(idAlbum);
    }

    @Override
    public ResponseEntity<Response> listInitWaitAlbum() throws JsonProcessingException {
        try {
            List<ListInitWaitAlbum> initWaitAlbumList = new ArrayList<>();
            for(InitWaitAlbum s : initWaitAlbumRepository.findAll()){
                ListInitWaitAlbum listInitWaitAlbum = new ListInitWaitAlbum();
                listInitWaitAlbum.setIdAlbum(s.getIdAlbum());
                listInitWaitAlbum.setTitleAlbum(s.getTitleAlbum());
                listInitWaitAlbum.setReleasedYear(s.getReleasedYear());
                listInitWaitAlbum.setGenreType(s.getGenreCodeMappingForAlbum().getGenreType());
                listInitWaitAlbum.setDurationTime(initWaitSongRepository.countTotalDurationAlbumByIdAlbum(listInitWaitAlbum.getIdAlbum()));
                listInitWaitAlbum.setArtistName(s.getIdArtistMappingForAlbum().getArtistName());
                initWaitAlbumList.add(listInitWaitAlbum);
            }

            return GenerateResponse.success("Successfully get object", initWaitAlbumList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return GenerateResponse.error("Something Wrong", null);
        }
    }

    @Override
    public ResponseEntity<Response> deleteInitWaitAlbumByIdAlbum(String idAlbum) throws JsonProcessingException {
        Optional<InitWaitAlbum> optionalInitWaitAlbum = initWaitAlbumRepository.findInitWaitAlbumByIdAlbum(idAlbum);
        if (optionalInitWaitAlbum.isPresent()){
            InitWaitAlbum initWaitAlbum = optionalInitWaitAlbum.get();
            if(!initWaitAlbum.isDeleted()){
                initWaitAlbum.setDeleted(true);
                initWaitAlbumRepository.save(initWaitAlbum);
                return GenerateResponse.success("Successfully to deleted","Album with id: "+idAlbum);
            }else {
                initWaitAlbum.setDeleted(false);
                initWaitAlbumRepository.save(initWaitAlbum);
                return GenerateResponse.success("Successfully to Recovery","Album with id: "+idAlbum);
            }
        }else {
            throw new NotFoundException(idAlbum);
        }
    }

    @Override
    public ResponseEntity<Response> viewInitWaitAlbumById(String idAlbum) throws JsonProcessingException {
        Optional<InitWaitAlbum> exist = initWaitAlbumRepository.findInitWaitAlbumByIdAlbum(idAlbum);
        if (exist.isPresent()){
            InitWaitAlbum initWaitAlbum = exist.get();
            DetailInitWaitAlbumById detailInitWaitAlbumById = new DetailInitWaitAlbumById();
            detailInitWaitAlbumById.setTitleAlbum(initWaitAlbum.getTitleAlbum());
            detailInitWaitAlbumById.setArtistName(initWaitAlbum.getIdArtistMappingForAlbum().getArtistName());
            detailInitWaitAlbumById.setDurationAlbum(initWaitSongRepository.countTotalDurationAlbumByIdAlbum(idAlbum));
            detailInitWaitAlbumById.setGenre(initWaitAlbum.getGenreCodeMappingForAlbum().getGenreType());
            detailInitWaitAlbumById.setReleasedYear(initWaitAlbum.getReleasedYear());
            detailInitWaitAlbumById.setDeleted(initWaitAlbum.isDeleted());
            detailInitWaitAlbumById.setCoverAlbum(initWaitAlbum.getCoverAlbum());

            List<ListSongForDetailInitWaitAlbum> listSongForDetailInitWaitAlbums = new ArrayList<>();
            for (InitWaitSong s : initWaitSongRepository.findAllByIdAlbum(idAlbum)) {
                ListSongForDetailInitWaitAlbum listSongForDetailInitWaitAlbum = new ListSongForDetailInitWaitAlbum();
                listSongForDetailInitWaitAlbum.setTitleSong(s.getTitleSong());
                listSongForDetailInitWaitAlbum.setDurationTime(s.getDurationSong());
                listSongForDetailInitWaitAlbums.add(listSongForDetailInitWaitAlbum);
            }
            detailInitWaitAlbumById.setSongs(listSongForDetailInitWaitAlbums);
            if (!initWaitAlbum.isDeleted()){
                return GenerateResponse.success("Successfully to get object: ", detailInitWaitAlbumById);
            }else {
                throw new NotFoundException(idAlbum);
            }
        }
        throw new NotFoundException(idAlbum);
    }

    @Override
    public ResponseEntity<Response> editStatusSubmitAlbumById(String idAlbum, InitWaitAlbumStatusSubmitDto initWaitAlbumStatusSubmitDto) throws JsonProcessingException {
        Optional<InitWaitAlbum> check = initWaitAlbumRepository.findInitWaitAlbumByIdAlbum(idAlbum);
        if(check.isPresent()){
            InitWaitAlbum initWaitAlbum = check.get();
            if(Objects.equals(initWaitAlbumStatusSubmitDto.getIsSubmitted(), "Submitted")){
                initWaitAlbum.setSubmitted(true);
                initWaitAlbumRepository.save(initWaitAlbum);

                WaitAlbum waitAlbum = new WaitAlbum();

                waitAlbum.setIdAlbum(initWaitAlbum.getIdAlbum());
                waitAlbum.setTitleAlbum(initWaitAlbum.getTitleAlbum());
                waitAlbum.setReleasedYear(initWaitAlbum.getReleasedYear());
                waitAlbum.setCoverAlbumType(initWaitAlbum.getCoverAlbumType());
                waitAlbum.setCoverAlbum(initWaitAlbum.getCoverAlbum());
                waitAlbum.setDeleted(false);
                waitAlbum.setApproved(false);
                waitAlbum.setGenreCodeMappingForAlbum(initWaitAlbum.getGenreCodeMappingForAlbum());
                waitAlbum.setIdArtistMappingForAlbum(initWaitAlbum.getIdArtistMappingForAlbum());
                waitAlbumRepository.save(waitAlbum);
                AlbumInitWaitEditStatusSubmitResponse albumInitWaitEditStatusSubmitResponse = getAlbumInitWaitEditStatusSubmitResponse(initWaitAlbum);
                return GenerateResponse.created("Successfully to Change Status Submit", albumInitWaitEditStatusSubmitResponse);

            }else {
                AlbumInitWaitEditStatusSubmitResponse albumInitWaitEditStatusSubmitResponse = getAlbumInitWaitEditStatusSubmitResponse(initWaitAlbum);
                return GenerateResponse.created("Failed to Change Status Submit", albumInitWaitEditStatusSubmitResponse);
            }
        }
        throw new NotFoundException(idAlbum);
    }

    private static AlbumInitWaitEditStatusSubmitResponse getAlbumInitWaitEditStatusSubmitResponse(InitWaitAlbum initWaitAlbum){
        AlbumInitWaitEditStatusSubmitResponse albumInitWaitEditStatusSubmitResponse = new AlbumInitWaitEditStatusSubmitResponse();
        albumInitWaitEditStatusSubmitResponse.setIdAlbum(initWaitAlbum.getIdAlbum());
        albumInitWaitEditStatusSubmitResponse.setTitleAlbum(initWaitAlbum.getTitleAlbum());
        albumInitWaitEditStatusSubmitResponse.setSubmitted(initWaitAlbum.isSubmitted());
        albumInitWaitEditStatusSubmitResponse.setCoverAlbum(initWaitAlbum.getCoverAlbum());
        albumInitWaitEditStatusSubmitResponse.setCoverAlbumType(initWaitAlbum.getCoverAlbumType());
        albumInitWaitEditStatusSubmitResponse.setGenreCode(initWaitAlbum.getGenreCodeMappingForAlbum().getGenreCode());
        albumInitWaitEditStatusSubmitResponse.setReleasedYear(initWaitAlbum.getReleasedYear());
        albumInitWaitEditStatusSubmitResponse.setDeleted(initWaitAlbum.isDeleted());
        albumInitWaitEditStatusSubmitResponse.setArtistName(initWaitAlbum.getIdArtistMappingForAlbum().getArtistName());
        albumInitWaitEditStatusSubmitResponse.setGenre(initWaitAlbum.getGenreCodeMappingForAlbum().getGenreType());
        return  albumInitWaitEditStatusSubmitResponse;
    }

    @Override
    public ResponseEntity<Response> editAlbumById(String idAlbum, InitWaitAlbumEditDto initWaitAlbumEditDto) throws IOException {
        Optional<InitWaitAlbum> check = initWaitAlbumRepository.findInitWaitAlbumByIdAlbum(idAlbum);
        if(check.isPresent()){
            InitWaitAlbum initWaitAlbum = check.get();
            if(initWaitAlbumEditDto.getGenreType()!=null||initWaitAlbumEditDto.getGenreType()!=""){
                CodeGenreHandler codeGenreHandler = new CodeGenreHandler(genreRepository);
                String genreType = initWaitAlbumEditDto.getGenreType();
                Optional<Genre> genreCheck = genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist(genreType));
                if(genreCheck.isPresent()){
                    initWaitAlbum.setGenreCodeMappingForAlbum(genreCheck.get());
                }else {
                    genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist("Other")).ifPresent(genreOther -> initWaitAlbum.setGenreCodeMappingForAlbum(genreOther));
                }

            }
            if(initWaitAlbumEditDto.getTitleAlbum()!=null||initWaitAlbumEditDto.getTitleAlbum()!=""){
                initWaitAlbum.setTitleAlbum(initWaitAlbumEditDto.getTitleAlbum());

            }
            if(initWaitAlbumEditDto.getReleasedYear()!=null || initWaitAlbumEditDto.getReleasedYear()!=""){
                InitWaitReleasedYearHandler initWaitReleasedYearHandler = new InitWaitReleasedYearHandler();
                initWaitAlbum.setReleasedYear(initWaitReleasedYearHandler.handlerYear(initWaitAlbumEditDto.getReleasedYear()));

            }
            if(initWaitAlbumEditDto.getFile()!=null){
                initWaitAlbum.setCoverAlbumType(initWaitAlbumEditDto.getFile().getContentType());
                initWaitAlbum.setCoverAlbum(initWaitAlbumEditDto.getFile().getBytes());


            }
            if(initWaitAlbumEditDto.getIdArtist()!=null||initWaitAlbumEditDto.getIdArtist()!=""){
                Optional<Artist> artistCheck = artistRepository.findByIdArtist(initWaitAlbumEditDto.getIdArtist());
                if(artistCheck.isPresent()){
                    initWaitAlbum.setIdArtistMappingForAlbum(artistCheck.get());
                }else {
                    throw new NotFoundException("Artist Not Found For ID Artist: " + initWaitAlbumEditDto.getIdArtist());
                }
            }

            initWaitAlbumRepository.save(initWaitAlbum);
            AlbumInitWaitEditResponse albumInitWaitEditResponse = getAlbumInitWaitEditResponse(initWaitAlbum);
            return GenerateResponse.created("Successfully to Change Data Album", albumInitWaitEditResponse);

        }
        throw new NotFoundException(idAlbum);
    }

    private static AlbumInitWaitEditResponse getAlbumInitWaitEditResponse(InitWaitAlbum initWaitAlbum) {
        AlbumInitWaitEditResponse albumInitWaitEditResponse = new AlbumInitWaitEditResponse();
        albumInitWaitEditResponse.setIdAlbum(initWaitAlbum.getIdAlbum());
        albumInitWaitEditResponse.setTitleAlbum(initWaitAlbum.getTitleAlbum());
        albumInitWaitEditResponse.setSubmitted(initWaitAlbum.isSubmitted());
        albumInitWaitEditResponse.setCoverAlbum(initWaitAlbum.getCoverAlbum());
        albumInitWaitEditResponse.setCoverAlbumType(initWaitAlbum.getCoverAlbumType());
        albumInitWaitEditResponse.setGenreCode(initWaitAlbum.getGenreCodeMappingForAlbum().getGenreCode());
        albumInitWaitEditResponse.setReleasedYear(initWaitAlbum.getReleasedYear());
        albumInitWaitEditResponse.setDeleted(initWaitAlbum.isDeleted());
        albumInitWaitEditResponse.setArtistName(initWaitAlbum.getIdArtistMappingForAlbum().getArtistName());
        albumInitWaitEditResponse.setGenre(initWaitAlbum.getGenreCodeMappingForAlbum().getGenreType());
        return albumInitWaitEditResponse;
    }


}
