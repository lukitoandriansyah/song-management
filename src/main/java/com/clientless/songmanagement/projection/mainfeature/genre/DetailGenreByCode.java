package com.clientless.songmanagement.projection.mainfeature.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetailGenreByCode {
    private String genreCode;
    private String genreType;
    private boolean isDeleted;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
