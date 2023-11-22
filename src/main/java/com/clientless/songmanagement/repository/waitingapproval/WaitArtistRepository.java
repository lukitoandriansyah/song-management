package com.clientless.songmanagement.repository.waitingapproval;

import com.clientless.songmanagement.domain.waitingapproval.WaitArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitArtistRepository extends JpaRepository<WaitArtist,Long> {
    @Query("SELECT idArtist FROM WaitArtist order by idArtist desc")
    List<String> idArtistLatest();
    Optional<WaitArtist> findByIdArtist(String idArtist);
}
