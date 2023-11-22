package com.clientless.songmanagement.repository.waitingapproval;

import com.clientless.songmanagement.domain.waitingapproval.WaitAlbum;
import com.clientless.songmanagement.domain.waitingapprovalinit.InitWaitAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitAlbumRepository extends JpaRepository<WaitAlbum, Long> {
    @Query("SELECT idAlbum FROM WaitAlbum order by idAlbum desc")
    List<String> idAlbumLatest();

    Optional<WaitAlbum> findWaitAlbumByIdAlbum(String idAlbum);


    @Query("SELECT COUNT(a) FROM WaitAlbum a INNER JOIN a.idArtistMappingForAlbum artist WHERE artist.idArtist = :idArtist and a.isDeleted = false")
    Long countAlbumByIdArtistMappingForAlbum(@Param("idArtist") String idArtist);

    @Query("SELECT a FROM WaitAlbum a INNER JOIN a.idArtistMappingForAlbum artist WHERE artist.idArtist = :idArtist ORDER BY a.idAlbum DESC")
    List<WaitAlbum> findAllByIdArtist(@Param("idArtist") String idArtist);

}
