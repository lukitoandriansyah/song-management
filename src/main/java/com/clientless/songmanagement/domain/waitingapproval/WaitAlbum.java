package com.clientless.songmanagement.domain.waitingapproval;

import com.clientless.songmanagement.domain.mainfeature.Artist;
import com.clientless.songmanagement.domain.mainfeature.Genre;
import com.clientless.songmanagement.domain.waitingapprovalinit.InitWaitSong;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wait_album_data")
public class WaitAlbum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idAlbum;
    private String titleAlbum;
    private LocalDate releasedYear;
    //    private String genreCode;
    private String coverAlbumType;
    @Lob //Save datatype to type Long
    @Column(name = "cover_album", columnDefinition = "LONGBLOB")
    private byte[] coverAlbum;
    //enhance
    private boolean isDeleted;
    private boolean isApproved;
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "genre_code")
    private Genre genreCodeMappingForAlbum;

    @ManyToOne
    @JoinColumn(name = "id_artist")
    private Artist idArtistMappingForAlbum;

    @OneToMany(mappedBy = "idAlbumMappingForSong")
    private List<WaitSong> songs;
}
