package com.clientless.songmanagement.service.impl.mainfeature;

import com.clientless.songmanagement.domain.mainfeature.Genre;
import com.clientless.songmanagement.dto.mainfeature.GenreDto;
import com.clientless.songmanagement.exception.NotFoundException;
import com.clientless.songmanagement.exception.NotFoundExceptionCausesDelete;
import com.clientless.songmanagement.model.Response;
import com.clientless.songmanagement.projection.mainfeature.genre.ListGenre;
import com.clientless.songmanagement.projection.mainfeature.genre.DetailGenreByCode;
import com.clientless.songmanagement.repository.mainfeature.GenreRepository;
import com.clientless.songmanagement.service.inf.mainfeature.GenreService;
import com.clientless.songmanagement.util.GenerateResponse;
import com.clientless.songmanagement.service.handler.mainfeature.CodeGenreHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;
    Logger logger = LoggerFactory.getLogger(GenreServiceImpl.class);


    @Override
    public ResponseEntity<Response> uploadGenre(GenreDto genreDtoRequest) throws JsonProcessingException {
        Genre genre = new Genre();
        CodeGenreHandler codeGenreHandler = new CodeGenreHandler(genreRepository);
        String genreType = genreDtoRequest.getGenreType();
        genre.setGenreType(genreType);
        genre.setGenreCode(codeGenreHandler.generateGenreCode(genreType));
        genre.setDeleted(false);
        genre.setCreatedAt(new Date());
        genre.setUpdatedAt(genre.getCreatedAt());
        genre.setNewPathGenreCode(genre.getGenreCode());
        genreRepository.save(genre);
        return GenerateResponse.created("Successfully to Upload",genre);
    }

    @Override
    public ResponseEntity<Response> listGenre() throws JsonProcessingException {
        try {
            List<ListGenre> genreDtoListList = new ArrayList<>();
            for(Genre s : genreRepository.findAll()){
                if(!s.isDeleted()){
                    ListGenre listGenre = new ListGenre();
                    listGenre.setGenreCode(s.getGenreCode());
                    listGenre.setGenreType(s.getGenreType());
                    listGenre.setDeleted(s.isDeleted());
                    genreDtoListList.add(listGenre);
                }
            }
            return GenerateResponse.success("Successfully get object", genreDtoListList);
        }catch (Exception e){
            logger.error(e.getMessage());
            return GenerateResponse.error("Something Wrong", null);
        }
    }

    @Override
    public ResponseEntity<Response> editGenre(String codeGenre,GenreDto genreDto) throws JsonProcessingException {
        Optional<Genre> optionalGenre = genreRepository.findGenreByOldPathGenreCodeOrNewPathGenreCode(codeGenre, codeGenre);
        if (optionalGenre.isPresent()){
            Genre genre = optionalGenre.get();
            if(!genre.isDeleted()){
                CodeGenreHandler codeGenreHandler = new CodeGenreHandler(genreRepository);
                String genreType = genreDto.getGenreType();
                genre.setGenreType(genreType);
                genre.setGenreCode(codeGenreHandler.generateGenreCode(genreType));
                genre.setOldPathGenreCode(genre.getNewPathGenreCode());
                genre.setNewPathGenreCode(genre.getGenreCode());
                genre.setUpdatedAt(new Date());
                genreRepository.save(genre);
                return GenerateResponse.success("Successfully to edited","Genre with name: "+genreDto.getGenreType());
            }else{
                throw new NotFoundException(codeGenre);
            }
        }else {
            throw new NotFoundException(codeGenre);
        }
    }

    @Override
    public ResponseEntity<Response> getGenreByCode(String codeGenre) throws JsonProcessingException {
        Optional<Genre> optionalGenre = genreRepository.findGenreByGenreCode(codeGenre);
        if (optionalGenre.isPresent()){
            Genre genre = optionalGenre.get();
            if (!genre.isDeleted()){
                DetailGenreByCode detailGenreByCode = new DetailGenreByCode();
                detailGenreByCode.setGenreCode(genre.getGenreCode());
                detailGenreByCode.setGenreType(genre.getGenreType());
                detailGenreByCode.setDeleted(genre.isDeleted());
                detailGenreByCode.setCreatedAt(genre.getCreatedAt());
                detailGenreByCode.setUpdatedAt(genre.getUpdatedAt());
                detailGenreByCode.setDeletedAt(genre.getDeletedAt());
                return GenerateResponse.success("Successfully to get data by code : "+genre.getGenreCode(),detailGenreByCode);
            }else {
                throw new NotFoundExceptionCausesDelete(codeGenre, "Deleted");
            }
        }else {
            throw new NotFoundException(codeGenre);
        }
    }

    @Override
    public ResponseEntity<Response> deleteGenreByCode(String codeGenre) throws JsonProcessingException {
        Optional<Genre> optionalGenre = genreRepository.findGenreByGenreCode(codeGenre);
        if (optionalGenre.isPresent()){
            Genre genre = optionalGenre.get();
            if(!genre.isDeleted()){
                genre.setDeleted(true);
                genre.setDeletedAt(new Date());
                genre.setUpdatedAt(genre.getDeletedAt());
                genreRepository.save(genre);
                return GenerateResponse.success("Successfully to deleted","Genre with code: "+codeGenre);
            }else {
                genre.setDeleted(false);
                genre.setUpdatedAt(new Date());
                genre.setDeletedAt(null);
                genreRepository.save(genre);
                return GenerateResponse.success("Successfully to recovery","Genre with code: "+codeGenre);
            }
//            genreRepository.delete(optionalGenre.get());
//            return GenerateResponse.success("Successfully to deleted","Artist with id: "+idGenre);
        }else {
            throw new NotFoundException(codeGenre);
        }
    }
}
