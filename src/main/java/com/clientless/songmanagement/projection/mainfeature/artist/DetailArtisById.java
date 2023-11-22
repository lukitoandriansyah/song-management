package com.clientless.songmanagement.projection.mainfeature.artist;

import com.clientless.songmanagement.projection.mainfeature.album.DetailAlbumById;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DetailArtisById {
    private String idArtist;
    private String artistName;
    private String genreType;
    private LocalDate yearActive;
    private Long totalAlbum;
    private List<DetailAlbumById> albums;
}
