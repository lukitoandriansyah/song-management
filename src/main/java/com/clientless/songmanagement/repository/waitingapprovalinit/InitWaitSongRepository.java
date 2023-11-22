package com.clientless.songmanagement.repository.waitingapprovalinit;

import com.clientless.songmanagement.domain.mainfeature.Song;
import com.clientless.songmanagement.domain.waitingapprovalinit.InitWaitSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Repository
public interface InitWaitSongRepository extends JpaRepository<InitWaitSong,Long> {
    @Query("SELECT idSong FROM InitWaitSong order by idSong desc")
    List<String> idInitWaitSongLatest();

    Optional<InitWaitSong> findInitWaitSongByIdSong(String idSong);

    @Query("SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(s.durationSong))) FROM InitWaitSong s INNER JOIN s.idAlbumMappingForSong initWaitAlbum WHERE initWaitAlbum.idAlbum = :idAlbum")
    Time countTotalDurationAlbumByIdAlbum(@Param("idAlbum") String idAlbum);

    @Query("SELECT s FROM InitWaitSong s INNER JOIN s.idAlbumMappingForSong initWaitAlbum WHERE initWaitAlbum.idAlbum = :idAlbum ORDER BY s.idSong DESC")
    List<InitWaitSong> findAllByIdAlbum(@Param("idAlbum") String idAlbum);
}
