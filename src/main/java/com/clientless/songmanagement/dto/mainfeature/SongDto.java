package com.clientless.songmanagement.dto.mainfeature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDto {
    private String titleSong;
    private String idAlbum;
    private String language;
    private String explicit;
    private String createdBy;
    private String durationSong;
    private MultipartFile song;
    private Long totalStreaming;
    private Long totalDownload;

}
