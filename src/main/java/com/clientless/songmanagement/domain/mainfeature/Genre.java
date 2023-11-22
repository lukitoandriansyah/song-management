package com.clientless.songmanagement.domain.mainfeature;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "genre_data")

public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String genreCode;

    private String genreType;
    //enhance

    private boolean isDeleted;

    @Column(unique = true)
    private String oldPathGenreCode;

    @Column(unique = true)
    private String newPathGenreCode;

    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;


    @OneToMany(mappedBy = "genreCodeMappingForAlbum")
    private List<Album> album;
    @OneToMany(mappedBy = "genreCodeMappingForArtist")
    private List<Artist> artists;
}
