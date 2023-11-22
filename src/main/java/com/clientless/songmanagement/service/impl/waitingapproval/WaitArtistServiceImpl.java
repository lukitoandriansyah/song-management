package com.clientless.songmanagement.service.impl.waitingapproval;

import com.clientless.songmanagement.domain.mainfeature.Artist;
import com.clientless.songmanagement.domain.mainfeature.Genre;
import com.clientless.songmanagement.domain.waitingapproval.WaitArtist;
import com.clientless.songmanagement.dto.waitingapproval.WaitArtistDto;
import com.clientless.songmanagement.dto.waitingapproval.WaitArtistStatusApproveDto;
import com.clientless.songmanagement.exception.NotFoundException;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.projection.waitingapproval.artist.*;
import com.clientless.songmanagement.repository.mainfeature.ArtistRepository;
import com.clientless.songmanagement.repository.mainfeature.GenreRepository;
import com.clientless.songmanagement.repository.waitingapproval.WaitArtistRepository;
import com.clientless.songmanagement.service.handler.mainfeature.CodeGenreHandler;
import com.clientless.songmanagement.service.inf.waitingapproval.WaitArtistService;
import com.clientless.songmanagement.service.handler.waitingapproval.IdWaitArtistHandler;
import com.clientless.songmanagement.service.handler.waitingapproval.WaitArtistYearActiveHandler;
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

@Service
public class WaitArtistServiceImpl implements WaitArtistService {
    @Autowired
    private WaitArtistRepository waitArtistRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ArtistRepository artistRepository;

    Logger logger = LoggerFactory.getLogger(WaitArtistServiceImpl.class);

    @Override
    public byte[] viewImageById(String idArtist) {
        Optional<WaitArtist> exist = waitArtistRepository.findByIdArtist(idArtist);
        if (exist.isPresent()){
            return exist.get().getPhoto();

        }
        throw new NotFoundException(idArtist);
    }

    @Override
    public ResponseEntity<Response> listWaitArtist() throws IOException {
        try {
            List<ListWaitArtist> waitArtistList = new ArrayList<>();
            for(WaitArtist s : waitArtistRepository.findAll()){
                ListWaitArtist listWaitArtist = new ListWaitArtist();
                listWaitArtist.setIdArtist(s.getIdArtist());
                listWaitArtist.setArtistName(s.getArtistName());
                listWaitArtist.setGenreType(s.getGenreCodeMappingForArtist().getGenreType());
                listWaitArtist.setYearActive(s.getYearActive());
                listWaitArtist.setApproved(s.isApproved());
                listWaitArtist.setRemarks(s.getRemarks());
                waitArtistList.add(listWaitArtist);
            }

            return GenerateResponse.success("Successfully get object", waitArtistList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return GenerateResponse.error("Something Wrong", null);
        }
    }

    @Override
    public ResponseEntity<Response> deleteWaitArtistByIdArtist(String idArtist) throws JsonProcessingException {
        Optional<WaitArtist> optionalListArtist = waitArtistRepository.findByIdArtist(idArtist);
        if (optionalListArtist.isPresent()){
            WaitArtist waitArtist = optionalListArtist.get();
            if (!waitArtist.isDeleted()){
                waitArtist.setDeleted(true);
                waitArtistRepository.save(waitArtist);
                return GenerateResponse.success("Successfully to deleted","Artist with id: "+idArtist);
            }else {
                waitArtist.setDeleted(false);
                waitArtistRepository.save(waitArtist);
                return GenerateResponse.success("Successfully to Recovery","Artist with id: "+idArtist);

            }
        }else {
            throw new NotFoundException(idArtist);
        }
    }

    @Override
    public ResponseEntity<Response> addWaitArtist(WaitArtistDto request) throws IOException {
        WaitArtist waitArtist = new WaitArtist();
        CodeGenreHandler codeGenreHandler = new CodeGenreHandler(genreRepository);
        String genreType = request.getGenreType();
        Optional<Genre> genreCheck = genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist(genreType));

        WaitArtistYearActiveHandler waitArtistYearActiveHandler = new WaitArtistYearActiveHandler();
        IdWaitArtistHandler idWaitArtistHandler = new IdWaitArtistHandler(waitArtistRepository);
        waitArtist.setIdArtist(idWaitArtistHandler.handlerId());
        waitArtist.setArtistName(request.getArtistName());
        waitArtist.setYearActive(waitArtistYearActiveHandler.handlerYear(request.getYearActive()));
        waitArtist.setPhoto_type(request.getFile().getContentType());
        waitArtist.setPhoto(request.getFile().getBytes());
        waitArtist.setDeleted(false);
        waitArtist.setApproved(false);

        if(genreCheck.isPresent()){
            waitArtist.setGenreCodeMappingForArtist(genreCheck.get());
        }else {
            genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist("Other")).ifPresent(waitArtist::setGenreCodeMappingForArtist);
        }

        waitArtistRepository.save(waitArtist);

        ArtistWaitUploadResponse artistWaitUploadResponse = getArtistWaitUploadResponse(waitArtist);

        return GenerateResponse.created("Successfully to Upload",artistWaitUploadResponse);
    }

    private static ArtistWaitUploadResponse getArtistWaitUploadResponse(WaitArtist waitArtist) {
        ArtistWaitUploadResponse artistWaitUploadResponse = new ArtistWaitUploadResponse();
        artistWaitUploadResponse.setId(waitArtist.getId());
        artistWaitUploadResponse.setIdArtist(waitArtist.getIdArtist());
        artistWaitUploadResponse.setArtistName(waitArtist.getArtistName());
        artistWaitUploadResponse.setPhoto(waitArtist.getPhoto());
        artistWaitUploadResponse.setPhoto_type(waitArtist.getPhoto_type());
        artistWaitUploadResponse.setYearActive(waitArtist.getYearActive());
        artistWaitUploadResponse.setDeleted(waitArtist.isDeleted());
        artistWaitUploadResponse.setApproved(waitArtist.isApproved());
        artistWaitUploadResponse.setGenre(waitArtist.getGenreCodeMappingForArtist().getGenreType());
        artistWaitUploadResponse.setGenreCode(waitArtist.getGenreCodeMappingForArtist().getGenreCode());
        return artistWaitUploadResponse;
    }

    @Override
    public ResponseEntity<Response> viewArtistById(String idArtist) throws JsonProcessingException {
        Optional<WaitArtist> exist = waitArtistRepository.findByIdArtist(idArtist);
        if (exist.isPresent()){
            WaitArtist waitArtist = exist.get();
            DetailWaitArtisById detailWaitArtisById = getDetailWaitArtisById(waitArtist);

            if (!waitArtist.isDeleted()){
                return GenerateResponse.success("Successfully to get Object ", detailWaitArtisById);
            }

        }
        throw new NotFoundException(idArtist);
    }

    private static DetailWaitArtisById getDetailWaitArtisById(WaitArtist waitArtist) {
        DetailWaitArtisById detailWaitArtisById = new DetailWaitArtisById();
        detailWaitArtisById.setIdArtist(waitArtist.getIdArtist());
        detailWaitArtisById.setArtistName(waitArtist.getArtistName());
        detailWaitArtisById.setGenreType(waitArtist.getGenreCodeMappingForArtist().getGenreType());
        detailWaitArtisById.setYearActive(waitArtist.getYearActive());
        detailWaitArtisById.setApproved(waitArtist.isApproved());
        detailWaitArtisById.setRemarks(waitArtist.getRemarks());
        return detailWaitArtisById;
    }

    @Override
    public ResponseEntity<Response> editStatusApproveArtistById(String idArtist, WaitArtistStatusApproveDto request) throws JsonProcessingException {
        Optional<WaitArtist> check = waitArtistRepository.findByIdArtist(idArtist);
        if(check.isPresent()){
            WaitArtist waitArtist = check.get();
            if(Objects.equals(request.getIsApproved(), "Rejected")){
                waitArtist.setApproved(false);
                waitArtist.setRemarks(request.getRemarks());
                waitArtist.setDeleted(true);
                waitArtistRepository.save(waitArtist);
                ArtistWaitEditStatusApproveResponse artistWaitEditStatusApproveResponse = getArtistWaiEditStatusApproveResponse(waitArtist);

                return GenerateResponse.created("Successfully to Change Status Rejected", artistWaitEditStatusApproveResponse);


            }else if(Objects.equals(request.getIsApproved(), "Approved")){
                waitArtist.setApproved(true);
                waitArtistRepository.save(waitArtist);
                Artist artist = new Artist();
                artist.setIdArtist(waitArtist.getIdArtist());
                artist.setArtistName(waitArtist.getArtistName());
                artist.setYearActive(waitArtist.getYearActive());
                artist.setPhoto_type(waitArtist.getPhoto_type());
                artist.setPhoto(waitArtist.getPhoto());
                artist.setDeleted(false);
                artist.setGenreCodeMappingForArtist(waitArtist.getGenreCodeMappingForArtist());


                artistRepository.save(artist);

                ArtistWaitEditStatusApproveResponse artistWaitEditStatusApproveResponse = getArtistWaiEditStatusApproveResponse(waitArtist);

                return GenerateResponse.created("Successfully to Change Status Approve", artistWaitEditStatusApproveResponse);

            }
        }            throw new NotFoundException(idArtist);

    }

    private static ArtistWaitEditStatusApproveResponse getArtistWaiEditStatusApproveResponse(WaitArtist waitArtist) {
        ArtistWaitEditStatusApproveResponse artistWaitUploadResponse = new ArtistWaitEditStatusApproveResponse();
        artistWaitUploadResponse.setId(waitArtist.getId());
        artistWaitUploadResponse.setIdArtist(waitArtist.getIdArtist());
        artistWaitUploadResponse.setArtistName(waitArtist.getArtistName());
        artistWaitUploadResponse.setPhoto(waitArtist.getPhoto());
        artistWaitUploadResponse.setPhoto_type(waitArtist.getPhoto_type());
        artistWaitUploadResponse.setYearActive(waitArtist.getYearActive());
        artistWaitUploadResponse.setDeleted(waitArtist.isDeleted());
        artistWaitUploadResponse.setApproved(waitArtist.isApproved());
        artistWaitUploadResponse.setRemarks(waitArtist.getRemarks());
        artistWaitUploadResponse.setGenre(waitArtist.getGenreCodeMappingForArtist().getGenreType());
        artistWaitUploadResponse.setGenreCode(waitArtist.getGenreCodeMappingForArtist().getGenreCode());
        return artistWaitUploadResponse;
    }

    @Override
    public ResponseEntity<Response> editArtistById(String idArtist, WaitArtistDto request) throws IOException {
        Optional<WaitArtist> check = waitArtistRepository.findByIdArtist(idArtist);
        if(check.isPresent()){
            WaitArtist waitArtist = check.get();
            if(!waitArtist.isApproved() && !waitArtist.isDeleted()){
                if(request.getGenreType() != null || request.getGenreType()!=""){
                    CodeGenreHandler codeGenreHandler = new CodeGenreHandler(genreRepository);
                    String genreType = request.getGenreType();
                    Optional<Genre> genreCheck = genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist(genreType));
                    if(genreCheck.isPresent()){
                        waitArtist.setGenreCodeMappingForArtist(genreCheck.get());
                    }else {
                        genreRepository.findGenreByGenreCode(codeGenreHandler.generateGenreCodeToCheckIfExist("Other")).ifPresent(waitArtist::setGenreCodeMappingForArtist);
                    }
                }if (request.getYearActive()!=null || request.getYearActive()!=""){
                    WaitArtistYearActiveHandler waitArtistYearActiveHandler = new WaitArtistYearActiveHandler();
                    waitArtist.setYearActive(waitArtistYearActiveHandler.handlerYear(request.getYearActive()));
                }if (request.getArtistName()!=null||request.getArtistName()!=""){
                    waitArtist.setArtistName(request.getArtistName());
                }if (request.getFile()!=null){
                    waitArtist.setPhoto_type(request.getFile().getContentType());
                    waitArtist.setPhoto(request.getFile().getBytes());
                }

                waitArtistRepository.save(waitArtist);

                ArtistWaitEditResponse artistWaitEditResponse = getArtistWaitEditResponse(waitArtist);

                return GenerateResponse.created("Successfully to Edit",artistWaitEditResponse);


            }else {
                ArtistWaitEditResponse artistWaiEditStatusApproveResponse = getArtistWaitEditResponse(waitArtist);

                return GenerateResponse.created("Failed to Edit",artistWaiEditStatusApproveResponse);


            }
        }            throw new NotFoundException(idArtist);

    }

    private static ArtistWaitEditResponse getArtistWaitEditResponse(WaitArtist waitArtist) {
        ArtistWaitEditResponse artistWaitEditResponse = new ArtistWaitEditResponse();
        artistWaitEditResponse.setId(waitArtist.getId());
        artistWaitEditResponse.setIdArtist(waitArtist.getIdArtist());
        artistWaitEditResponse.setArtistName(waitArtist.getArtistName());
        artistWaitEditResponse.setPhoto(waitArtist.getPhoto());
        artistWaitEditResponse.setPhoto_type(waitArtist.getPhoto_type());
        artistWaitEditResponse.setYearActive(waitArtist.getYearActive());
        artistWaitEditResponse.setDeleted(waitArtist.isDeleted());
        artistWaitEditResponse.setApproved(waitArtist.isApproved());
        artistWaitEditResponse.setRemarks(waitArtist.getRemarks());
        artistWaitEditResponse.setGenre(waitArtist.getGenreCodeMappingForArtist().getGenreType());
        artistWaitEditResponse.setGenreCode(waitArtist.getGenreCodeMappingForArtist().getGenreCode());
        return artistWaitEditResponse;
    }




}
