package com.clientless.songmanagement.domain.waitingapproval;

import com.clientless.songmanagement.domain.mainfeature.Album;
import com.clientless.songmanagement.domain.mainfeature.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wait_artist_data")
public class WaitArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idArtist;
    private String artistName;
    //    private String genreType;
    private LocalDate yearActive;
    private String photo_type;
    @Lob //Save datatype to type Long
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;
    //enhance
    private boolean isDeleted;
    private boolean isApproved;
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "genre_code")
    private Genre genreCodeMappingForArtist;

    @OneToMany(mappedBy = "idArtistMappingForAlbum")
    private List<WaitAlbum> album;

}
