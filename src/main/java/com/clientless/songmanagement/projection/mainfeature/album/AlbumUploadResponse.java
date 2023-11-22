package com.clientless.songmanagement.projection.mainfeature.album;

import com.clientless.songmanagement.domain.mainfeature.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.sql.Time;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlbumUploadResponse {
    private Long id;
    private String idAlbum;
    private String titleAlbum;
    private LocalDate releasedYear;
    private Time durationAlbum;
    private String coverAlbumType;
    private byte[] coverAlbum;
    private boolean isDeleted;
    private String genre;
    private String genreCode;
    private String artistName;
    private String idArtist;
}
