package com.clientless.songmanagement.projection.waitingapproval.album;

import com.clientless.songmanagement.projection.waitingapproval.song.ListSongForDetailWaitAlbum;
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

public class DetailWaitAlbumById {
    private String titleAlbum;
    private String artistName;
    private String genre;
    private Time durationAlbum;
    private LocalDate releasedYear;
    private boolean isDeleted;
    private List<ListSongForDetailWaitAlbum> songs;
    private byte[] coverAlbum;

}
