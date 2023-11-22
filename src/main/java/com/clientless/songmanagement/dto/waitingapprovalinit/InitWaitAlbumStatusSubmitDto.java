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
public class InitWaitAlbumStatusSubmitDto {
    private String isSubmitted;
}
