package ru.isntrui.lb.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.isntrui.lb.models.Text
import ru.isntrui.lb.repositories.TextRepository

@Service
class TextService {
    @Autowired
    private lateinit var textRepository: TextRepository

    public fun findByTitleContaining(title: String) = textRepository.findByTitleContaining(title)
    public fun findByMadeById(id: Long) = textRepository.findByMadeById(id)
    public fun findByApprovedById(id: Long) = textRepository.findByApprovedById(id)
    public fun findByWaveId(id: Long) = textRepository.findByWaveId(id)
    public fun getAll() = textRepository.findAll()
    public fun getById(id: Long) = textRepository.findById(id)
    public fun create(text: Text) = textRepository.save(text)
    public fun deleteById(id: Long) = textRepository.deleteById(id)
    public fun approve(id: Long, userId: Long) {
        textRepository.approve(id, true, userId)
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
            existingText.madeOn = updatedText.madeOn
            existingText.wave = updatedText.wave

            textRepository.save(existingText)
        } else {
            null
        }
    }
    public fun disapprove(id: Long, userId: Long) {
        textRepository.approve(id, false, userId)
    }
}