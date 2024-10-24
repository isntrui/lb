package ru.isntrui.lb.repositories

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.isntrui.lb.models.Invite

@Repository
interface InviteRepository : JpaRepository<Invite, Long> {
    public fun findByCode(code: String): Invite?

    @Transactional
    @Modifying
    public fun deleteByCode(code: String)

    @Transactional
    @Modifying
    @Query("UPDATE Invite i SET i.isUsed = true WHERE i.code = :code and i.usedBy = :usedBy")
    public fun setUsed(code: String, usedBy: Long)


}