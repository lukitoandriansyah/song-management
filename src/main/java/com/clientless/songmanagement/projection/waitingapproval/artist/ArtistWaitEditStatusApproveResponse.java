package com.clientless.songmanagement.projection.waitingapproval.artist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArtistWaitEditStatusApproveResponse {
    private Long id;
    private String idArtist;
    private String artistName;
    //    private String genreType;
    private LocalDate yearActive;
    private String photo_type;
    private byte[] photo;
    //enhance
    private boolean isDeleted;
    private boolean isApproved;
    private String remarks;

    private String genre;
    private String genreCode;

}
