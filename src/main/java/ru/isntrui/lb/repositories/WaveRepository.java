package ru.isntrui.lb.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.isntrui.lb.enums.WaveStatus;
import ru.isntrui.lb.models.Wave;

import java.util.Optional;

@Repository
public interface WaveRepository extends JpaRepository<Wave, Long> {
    @Query("SELECT w FROM Wave w ORDER BY w.createdAt DESC")
    Optional<Wave> findLastCreatedWave();

    @Transactional
    @Modifying
    @Query("UPDATE Wave w SET w.status = :status WHERE w.id = :id")
    void updateStatus(Long id, WaveStatus status);

}
