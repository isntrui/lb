package ru.isntrui.lb.repositories

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.isntrui.lb.models.Invite

@Repository
interface InviteRepository : JpaRepository<Invite, Long> {
    public fun findByCode(code: String): Invite?

    @Query("SELECT i FROM Invite i")
    fun getAllInvites(): List<Invite>;

    @Transactional
    @Modifying
    public fun deleteByCode(code: String)

    @Modifying
    @Query("UPDATE Invite i SET i.isUsed = true, i.usedBy.id = :userId WHERE i.code = :code")
    fun setUsed(@Param("code") code: String, @Param("userId") userId: Long)
}