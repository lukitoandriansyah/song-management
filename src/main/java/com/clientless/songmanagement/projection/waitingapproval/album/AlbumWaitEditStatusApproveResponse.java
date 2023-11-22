package com.clientless.songmanagement.projection.waitingapproval.album;

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
public class AlbumWaitEditStatusApproveResponse {
    private Long id;
    private String idAlbum;
    private String titleAlbum;
    private LocalDate releasedYear;
    private Time durationAlbum;
    private String coverAlbumType;
    private byte[] coverAlbum;
    private boolean isDeleted;
    private boolean isApproved;
    private String remarks;
    private String genre;
    private String genreCode;
    private String artistName;
    private String idArtist;
}
