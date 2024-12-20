package ru.isntrui.lb.repositories

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.isntrui.lb.models.Text
import ru.isntrui.lb.models.User
import java.time.LocalDateTime

@Repository
interface TextRepository : JpaRepository<Text, Long> {
    fun findByTitleContaining(title: String): List<Text>
    fun findByMadeById(id: Long): List<Text>
    fun findByApprovedById(id: Long): List<Text>
    fun findByWaveId(id: Long): List<Text>

    @Modifying
    @Transactional
    @Query("UPDATE Text t SET t.isApproved = :isApproved, t.approvedBy = :user, t.approvedOn= :ldt WHERE t.id = :id")
    fun approve(id: Long, isApproved: Boolean, user: User, ldt: LocalDateTime)
}
