package com.clientless.songmanagement.projection.waitingapprovalinit.song;

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
public class SongInitWaitUploadResponse {
    private String idSong;
    private String titleSong;
    private String artistName;
    private String titleAlbum;
    private LocalDate releasedYear;
    private String language;
    private Boolean explicit;
    private String createdBy;
    private Time durationSong;
    private boolean isDeleted;
    private boolean isSubmitted;
    private byte[] song;
    private String songType;


}
