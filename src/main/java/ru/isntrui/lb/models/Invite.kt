package ru.isntrui.lb.models

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "invites")
open class Invite(
    @Column(unique = true, nullable = false)
    @Email
    open var email: String? = null,

    @Column(nullable = false)
    open var code: String = ""
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(name = "made_on", nullable = false)
    @CreationTimestamp
    open lateinit var madeOn: LocalDateTime

    @Column(nullable = false)
    open var isUsed: Boolean = false

    constructor() : this(null, "")
}