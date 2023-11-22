package com.clientless.songmanagement.projection.mainfeature.artist;

import com.clientless.songmanagement.domain.mainfeature.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ArtistUploadResponse {
    private Long id;
    private String idArtist;
    private String artistName;
    //    private String genreType;
    private LocalDate yearActive;
    private Long totalAlbum;
    private String photo_type;
    private byte[] photo;
    //enhance
    private boolean isDeleted;

    private String genre;
    private String genreCode;

}
