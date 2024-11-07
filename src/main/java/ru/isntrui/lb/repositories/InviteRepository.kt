package ru.isntrui.lb.repositories

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import ru.isntrui.lb.models.Invite

interface InviteRepository : CrudRepository<Invite, Long> {

    @Query("UPDATE Invite i SET i.isUsed = true WHERE i.code = :code")
    @Modifying
    @Transactional
    fun setUsed(code: String, userId: Long)

    fun findByCode(code: String): Invite?

    fun deleteByCode(code: String)
}