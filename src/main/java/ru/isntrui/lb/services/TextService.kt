package ru.isntrui.lb.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.isntrui.lb.models.Text
import ru.isntrui.lb.repositories.TextRepository
import java.time.LocalDateTime
import kotlin.jvm.Throws

@Service
class TextService {
    @Autowired
    private lateinit var textRepository: TextRepository
    @Autowired
    private lateinit var userService: UserService
    public fun findByTitleContaining(title: String) : Iterable<Text> = textRepository.findByTitleContaining(title)
    public fun findByMadeById(id: Long) = textRepository.findByMadeById(id)
    public fun findByApprovedById(id: Long) = textRepository.findByApprovedById(id)
    public fun findByWaveId(id: Long) = textRepository.findByWaveId(id)
    public fun getAll() = textRepository.findAll()
    public fun getById(id: Long) = textRepository.findById(id)
    public fun create(text: Text) = textRepository.save(text)
    public fun deleteById(id: Long) = textRepository.deleteById(id)

    @Throws(Exception::class)
    public fun approve(id: Long, approve: Boolean) {
        textRepository.approve(id, approve, userService.currentUser, LocalDateTime.now())
    }
    public fun update(id: Long, updatedText: Text): Text? {
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