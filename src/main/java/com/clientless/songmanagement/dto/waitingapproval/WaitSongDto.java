package com.clientless.songmanagement.dto.waitingapproval;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitSongDto {
    private String titleSong;
    private String idAlbum;
    private String language;
    private String explicit;
    private String createdBy;
    private String durationSong;
    private MultipartFile song;
}
