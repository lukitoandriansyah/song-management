package com.clientless.songmanagement.projection.mainfeature.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListGenre {
    private String genreCode;
    private String genreType;
    //enhance
    private boolean isDeleted;
}
