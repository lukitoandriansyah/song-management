package com.clientless.songmanagement.projection.waitingapprovalinit.song;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListSongForDetailInitWaitAlbum {
    private String titleSong;
    private Time durationTime;
}
