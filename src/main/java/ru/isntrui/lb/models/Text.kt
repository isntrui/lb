package ru.isntrui.lb.models

import jakarta.annotation.Nullable
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

    @ManyToOne
    private val wave: Wave? = null

    private val body: String? = null

    private val title: String? = null

    @ManyToOne
    private val madeBy: User? = null

    private val madeOn: Date? = null

    private val isApproved: Boolean = false

    @ManyToOne
    @Nullable
    private val approvedBy: User? = null

    @Nullable
    private val approvedOn: Date? = null
}