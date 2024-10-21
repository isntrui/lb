package ru.isntrui.lb.models

import jakarta.persistence.*
import lombok.Data
import lombok.EqualsAndHashCode
import java.sql.Date

@Entity
@Table(name = "texts")
@Data
@EqualsAndHashCode(of = ["id"])
open class Text {
    @Id
    @GeneratedValue
    private val id: Long? = null

    private val wave: String? = null
    private val body: String? = null
    private val title: String? = null

    @ManyToOne
    private val madeBy: User? = null

    private val madeOn: Date? = null

    private val isApproved: Boolean? = null

    @ManyToOne
    private val approvedBy: User? = null

    private val approvedOn: Date? = null
}