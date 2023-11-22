package com.clientless.songmanagement.projection.waitingapproval.song;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListWaitSong {
    private String idSong;
    private String titleSong;
    private String artistName;
    private String albumName;
    private String year;
    private String genreType;
    private Time durationTime;
}
