package com.clientless.songmanagement.domain.waitingapproval;

import com.clientless.songmanagement.domain.waitingapprovalinit.InitWaitAlbum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wait_song_data")
public class WaitSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idSong;
    private String titleSong;
    //    private String titleAlbum;
    private String language;
    private Boolean explicit;
    //    private String genreType;
    private String createdBy;
    private Time durationSong;
    //    @Lob //Save datatype to type Long
//    @Column(name = "cover_song", columnDefinition = "LONGBLOB")
//    private byte[] coverSong;
//    private String coverType;
    @Lob //Save datatype to type Long
    @Column(columnDefinition = "LONGBLOB")
    private byte[] song;
    private String songType;
    //    private String artistName;
    //enhance
    private boolean isDeleted;
    private boolean isApproved;
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "id_album")
    private WaitAlbum idAlbumMappingForSong;


}
