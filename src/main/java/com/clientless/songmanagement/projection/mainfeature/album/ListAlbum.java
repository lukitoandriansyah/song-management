package com.clientless.songmanagement.projection.mainfeature.album;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListAlbum {
    private String idAlbum;
    private String titleAlbum;
    private LocalDate releasedYear;
    private String genreType;
    private String artistName;
    private Time durationTime;
}
