package com.clientless.songmanagement.repository.mainfeature;

import com.clientless.songmanagement.domain.mainfeature.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findGenreByGenreCode(String genreCode);
    Optional<Genre> findGenreByOldPathGenreCodeOrNewPathGenreCode(String oldPathGenreCode, String newPathGenreCode);
}
