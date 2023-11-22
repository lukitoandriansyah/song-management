package com.clientless.songmanagement.dto.waitingapprovalinit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InitWaitAlbumDto {
    private String idAlbum;
    private String titleAlbum;
    /*@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")*/
    private String releasedYear;
    private String genreType;/*
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm:ss")*/
    private String idArtist;
    private MultipartFile file;
}
