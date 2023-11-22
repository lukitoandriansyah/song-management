package com.clientless.songmanagement.projection.mainfeature.album;

import com.clientless.songmanagement.projection.mainfeature.song.ListSongForDetailAlbum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DetailAlbumById {
    private String titleAlbum;
    private String artistName;
    private String genre;
    private Time durationAlbum;
    private LocalDate releasedYear;
    private boolean isDeleted;
    private List<ListSongForDetailAlbum> songs;
    private byte[] coverAlbum;

}
