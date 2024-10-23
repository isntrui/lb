package ru.isntrui.lb.models

import jakarta.annotation.Nullable
import jakarta.persistence.*
import lombok.Data
import lombok.EqualsAndHashCode

@Entity
@Table(name = "invites")
@Data
@EqualsAndHashCode(of = ["id"])
open class Invite {
    @Id
    @GeneratedValue
    private val id: Long? = null

    @Column(unique = true, nullable = false)
    private var email: String? = null

    private val code: String? = null

    @ManyToOne
    private val madeBy: User? = null


    private val madeOn: Boolean? = null

    private val isUsed: Boolean = false


    @ManyToOne
    @Nullable
    private val usedBy: User? = null
}