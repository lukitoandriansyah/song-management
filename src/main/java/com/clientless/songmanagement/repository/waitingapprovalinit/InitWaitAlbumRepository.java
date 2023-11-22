package com.clientless.songmanagement.repository.waitingapprovalinit;

import com.clientless.songmanagement.domain.mainfeature.Album;
import com.clientless.songmanagement.domain.waitingapprovalinit.InitWaitAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InitWaitAlbumRepository extends JpaRepository<InitWaitAlbum, Long> {
    @Query("SELECT idAlbum FROM InitWaitAlbum order by idAlbum desc")
    List<String> idAlbumLatest();

    Optional<InitWaitAlbum> findInitWaitAlbumByIdAlbum(String idAlbum);


    @Query("SELECT COUNT(a) FROM InitWaitAlbum a INNER JOIN a.idArtistMappingForAlbum artist WHERE artist.idArtist = :idArtist and a.isDeleted = false")
    Long countAlbumByIdArtistMappingForAlbum(@Param("idArtist") String idArtist);

    @Query("SELECT a FROM InitWaitAlbum a INNER JOIN a.idArtistMappingForAlbum artist WHERE artist.idArtist = :idArtist ORDER BY a.idAlbum DESC")
    List<InitWaitAlbum> findAllByIdArtist(@Param("idArtist") String idArtist);


}
