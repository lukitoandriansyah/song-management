package com.clientless.songmanagement.projection.waitingapprovalinit.album;

import com.clientless.songmanagement.projection.waitingapprovalinit.song.ListSongForDetailInitWaitAlbum;
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

public class DetailInitWaitAlbumById {
    private String titleAlbum;
    private String artistName;
    private String genre;
    private Time durationAlbum;
    private LocalDate releasedYear;
    private boolean isDeleted;
    private List<ListSongForDetailInitWaitAlbum> songs;
    private byte[] coverAlbum;

}
