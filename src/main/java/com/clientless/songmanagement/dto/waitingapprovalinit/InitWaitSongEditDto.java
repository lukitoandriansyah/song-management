package com.clientless.songmanagement.dto.waitingapprovalinit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitWaitSongEditDto {
    private String titleSong;
    private String idAlbum;
    private String language;
    private String explicit;
    private String createdBy;
    private String durationSong;
    private MultipartFile song;
}
