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

public class DetailWaitArtisById {
    private String idArtist;
    private String artistName;
    private String genreType;
    private LocalDate yearActive;
    private boolean isApproved;
    private String remarks;
}
