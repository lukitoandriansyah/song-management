package com.clientless.songmanagement.projection.mainfeature.song;

import com.clientless.songmanagement.domain.mainfeature.Album;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SongUploadResponse {
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
    private byte[] song;
    private String songType;


}
