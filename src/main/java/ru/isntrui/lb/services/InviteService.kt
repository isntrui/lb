package ru.isntrui.lb.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.isntrui.lb.repositories.InviteRepository

@Service
class InviteService {
    @Autowired
    private lateinit var inviteRepository: InviteRepository

    public fun findByCode(code: String) = inviteRepository.findByCode(code)
    public fun findByMadeById(userId: Long) = inviteRepository.findByMadeById(userId)
    public fun use(code: String, userId: Long) = inviteRepository.setUsed(code, userId)
}