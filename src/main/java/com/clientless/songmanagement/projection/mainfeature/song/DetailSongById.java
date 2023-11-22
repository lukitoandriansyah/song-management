package com.clientless.songmanagement.projection.mainfeature.song;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DetailSongById {
    private String idSong;
    private String titleSong;
    private String artistName;
    private String titleAlbum;
    private LocalDate releasedYear;
    private String language;
    private Boolean explicit;
    private String createdBy;
    private Time durationSong;
    private Long totalStreaming;
    private Long totalDownload;
    private boolean isDeleted;
    private String songType;

}
