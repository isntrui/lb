package ru.isntrui.lb.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.isntrui.lb.models.Text
import ru.isntrui.lb.repositories.TextRepository
import java.time.LocalDateTime

@Service
class TextService {
    @Autowired
    private lateinit var textRepository: TextRepository
    @Autowired
    private lateinit var userService: UserService
    fun findByTitleContaining(title: String): Iterable<Text> = textRepository.findByTitleContaining(title)
    fun findByMadeById(id: Long) = textRepository.findByMadeById(id)
    fun findByApprovedById(id: Long) = textRepository.findByApprovedById(id)
    fun findByWaveId(id: Long) = textRepository.findByWaveId(id)
    fun getAll() = textRepository.findAll()
    fun getById(id: Long) = textRepository.findById(id)
    fun create(text: Text) = textRepository.save(text)
    fun deleteById(id: Long) = textRepository.deleteById(id)

    @Throws(Exception::class)
    fun approve(id: Long, approve: Boolean) {
        textRepository.approve(id, approve, userService.currentUser, LocalDateTime.now())
    }

    fun update(id: Long, updatedText: Text): Text? {
        val existingTextOpt = textRepository.findById(id)

        return if (existingTextOpt.isPresent) {
            val existingText = existingTextOpt.get()

            existingText.title = updatedText.title
            existingText.body = updatedText.body
            existingText.isApproved = updatedText.isApproved
            existingText.approvedBy = updatedText.approvedBy
            existingText.approvedOn = updatedText.approvedOn
            existingText.madeBy = updatedText.madeBy
            existingText.wave = updatedText.wave

            textRepository.save(existingText)
        } else {
            null
        }
    }
}