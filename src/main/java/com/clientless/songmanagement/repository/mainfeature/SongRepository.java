package com.clientless.songmanagement.repository.mainfeature;

import com.clientless.songmanagement.domain.mainfeature.Album;
import com.clientless.songmanagement.domain.mainfeature.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {
    @Query("SELECT idSong FROM Song order by idSong desc")
    List<String> idSongLatest();

    Optional<Song> findSongByIdSong(String idSong);

    @Query("SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(s.durationSong))) FROM Song s INNER JOIN s.idAlbumMappingForSong album WHERE album.idAlbum = :idAlbum")
    Time countTotalDurationAlbumByIdAlbum(@Param("idAlbum") String idAlbum);

    @Query("SELECT s FROM Song s INNER JOIN s.idAlbumMappingForSong album WHERE album.idAlbum = :idAlbum ORDER BY s.idSong DESC")
    List<Song> findAllByIdAlbum(@Param("idAlbum") String idAlbum);


}
