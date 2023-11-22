package com.clientless.songmanagement.dto.mainfeature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtistDto {
    private String idArtist;
    private String artistName;
    private String genreType;
    private String yearActive;
    private MultipartFile file;
}
