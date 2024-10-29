package ru.isntrui.lb.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.isntrui.lb.models.Song;
import ru.isntrui.lb.models.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    @Transactional
    @Modifying
    @Query("update Song s set s.isApproved = :isApproved, s.approvedBy = :user, s.approvedOn=:dateTime where s.id = :songId")
    void approveSong(Long songId, boolean isApproved, LocalDateTime dateTime, User user);
    @Query("select s from Song s")
    @Transactional
    List<Song> getAllSongs();
    List<Song> findByTitleContaining(String title);
    List<Song> findByWaveId(Long waveId);
    List<Song> getAllByMadeBy(User user);

}
