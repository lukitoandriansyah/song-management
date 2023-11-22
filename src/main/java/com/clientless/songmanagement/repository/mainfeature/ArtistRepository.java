package com.clientless.songmanagement.repository.mainfeature;

import com.clientless.songmanagement.domain.mainfeature.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist,Long> {
    @Query("SELECT idArtist FROM Artist order by idArtist desc")
    List<String> idArtistLatest();
    Optional<Artist> findByIdArtist(String idArtist);
}
