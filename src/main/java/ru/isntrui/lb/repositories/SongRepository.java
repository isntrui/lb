package ru.isntrui.lb.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.isntrui.lb.models.Song;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    @Transactional
    @Modifying
    @Query("update Song s set s.isApproved = ?2 where s.id = ?1")
    void approveSong(Long songId, boolean isApproved);

    List<Song> findByWaveId(Long waveId);


}
