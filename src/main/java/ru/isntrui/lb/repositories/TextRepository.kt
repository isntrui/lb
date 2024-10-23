package ru.isntrui.lb.repositories

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.isntrui.lb.models.Text

@Repository
interface TextRepository : JpaRepository<Text, Long> {
    public fun findByTitleContaining(title: String): List<Text>
    public fun findByMadeById(id: Long): List<Text>
    public fun findByIsApproved(isApproved: Boolean): List<Text>
    public fun findByApprovedById(id: Long): List<Text>
    public fun findByWaveId(id: Long): List<Text>

    @Transactional
    @Modifying
    @Query("UPDATE Text t SET t.isApproved = :isApproved WHERE t.id = :id and t.approvedBy = :userId")
    public fun approve(id: Long, isApproved: Boolean, userId: Long);
}