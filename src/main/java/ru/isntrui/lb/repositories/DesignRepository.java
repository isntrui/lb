package ru.isntrui.lb.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.isntrui.lb.models.Design;
import ru.isntrui.lb.models.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DesignRepository extends JpaRepository<Design, Long> {
    @Transactional
    @Modifying
    @Query("update Design d set d.isApproved = :isApproved, d.approvedOn = :dateTime, d.approvedBy = :user where d.id = :designId")
    void approveDesign(Long designId, boolean isApproved, LocalDateTime dateTime, User user);
    List<Design> findByWaveId(Long waveId);
    List<Design> getAllByCreatedBy(User user);
    List<Design> findByTitleContaining(String title);
}