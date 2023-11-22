package com.clientless.songmanagement.service.handler.mainfeature;

import com.clientless.songmanagement.domain.mainfeature.Genre;
import com.clientless.songmanagement.repository.mainfeature.GenreRepository;

import java.util.Optional;


public class CodeGenreHandler {

    private final GenreRepository genreRepository;

    public CodeGenreHandler(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }


    public String generateGenreCode(String genreName) {
        String baseCode = genreName.replaceAll("[^a-zA-Z]", "").toUpperCase();
        String basePrefixCode = ("GD MSP").replaceAll(" ","").toUpperCase();
        Optional<Genre> optionalGenre = genreRepository.findGenreByGenreCode(basePrefixCode + baseCode);
        if(optionalGenre.isPresent()){
            return generateGenreCodeRecursive(basePrefixCode+baseCode, 1);
        }else {
            return basePrefixCode + baseCode;
        }
    }

    public String generateGenreCodeRecursive(String codeGenre, Integer counter){
        Integer counters = counter;
        Optional<Genre> optionalGenre = genreRepository.findGenreByGenreCode(codeGenre + counters);
        if(optionalGenre.isPresent()){
            ++counters;
            return generateGenreCodeRecursive(codeGenre, counters);
        }
        return codeGenre+(counters);

    }

    public String generateGenreCodeToCheckIfExist(String genreName){
        String baseCode = genreName.replaceAll("[^a-zA-Z]", "").toUpperCase();
        String basePrefixCode = ("GD MSP").replaceAll(" ","").toUpperCase();
        return basePrefixCode + baseCode;

    }

}




