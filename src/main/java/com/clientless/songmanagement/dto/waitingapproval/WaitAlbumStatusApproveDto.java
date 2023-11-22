package com.clientless.songmanagement.dto.waitingapproval;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WaitAlbumStatusApproveDto {
    private String isApproved;
    private String remarks;
}
