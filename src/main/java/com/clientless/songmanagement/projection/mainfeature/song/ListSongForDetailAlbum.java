package com.clientless.songmanagement.projection.mainfeature.song;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListSongForDetailAlbum {
    private String titleSong;
    private Time durationTime;
}
