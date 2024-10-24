package ru.isntrui.lb.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.isntrui.lb.models.Invite
import ru.isntrui.lb.repositories.InviteRepository

@Service
open class InviteService {

    @Autowired
    private lateinit var inviteRepository: InviteRepository

    // Find an invite by its code
    open fun findByCode(code: String): Invite? {
        return inviteRepository.findByCode(code)
    }

    // Set the invite as used by a specific user
    @Transactional
    open fun use(code: String, userId: Long) {
        val invite = inviteRepository.findByCode(code) ?: throw IllegalArgumentException("Invalid invite code")

        if (invite.isUsed) {
            throw IllegalStateException("Invite has already been used")
        }

        // Set the invite as used and associate it with the userId
        inviteRepository.setUsed(code, userId)
    }
}
