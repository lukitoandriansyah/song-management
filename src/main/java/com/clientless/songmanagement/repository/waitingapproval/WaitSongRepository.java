package com.clientless.songmanagement.repository.waitingapproval;

import com.clientless.songmanagement.domain.waitingapproval.WaitSong;
import com.clientless.songmanagement.domain.waitingapprovalinit.InitWaitSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaitSongRepository extends JpaRepository<WaitSong,Long> {
    @Query("SELECT idSong FROM WaitSong order by idSong desc")
    List<String> idWaitSongLatest();

    Optional<WaitSong> findWaitSongByIdSong(String idSong);

    @Query("SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(s.durationSong))) FROM WaitSong s INNER JOIN s.idAlbumMappingForSong waitAlbum WHERE waitAlbum.idAlbum = :idAlbum")
    Time countTotalDurationAlbumByIdAlbum(@Param("idAlbum") String idAlbum);

    @Query("SELECT s FROM WaitSong s INNER JOIN s.idAlbumMappingForSong waitAlbum WHERE waitAlbum.idAlbum = :idAlbum ORDER BY s.idSong DESC")
    List<WaitSong> findAllByIdAlbum(@Param("idAlbum") String idAlbum);
}
