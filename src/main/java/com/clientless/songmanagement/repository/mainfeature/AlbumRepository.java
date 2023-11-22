package com.clientless.songmanagement.repository.mainfeature;

import com.clientless.songmanagement.domain.mainfeature.Album;
import com.clientless.songmanagement.domain.mainfeature.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("SELECT idAlbum FROM Album order by idAlbum desc")
    List<String> idAlbumLatest();

    Optional<Album> findAlbumByIdAlbum(String idAlbum);


    @Query("SELECT COUNT(a) FROM Album a INNER JOIN a.idArtistMappingForAlbum artist WHERE artist.idArtist = :idArtist and a.isDeleted = false")
    Long countAlbumByIdArtistMappingForAlbum(@Param("idArtist") String idArtist);

    @Query("SELECT a FROM Album a INNER JOIN a.idArtistMappingForAlbum artist WHERE artist.idArtist = :idArtist ORDER BY a.idAlbum DESC")
    List<Album> findAllByIdArtist(@Param("idArtist") String idArtist);


}
