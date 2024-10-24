package ru.isntrui.lb.models

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "invites")
open class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(unique = true, nullable = false)
    open var email: String? = null

    @Column(nullable = false)
    open var code: String = ""

    @Column(name = "made_on", nullable = false)
    open var madeOn: LocalDate = LocalDate.now()

    @Column(nullable = false)
    open var isUsed: Boolean = false

    @ManyToOne(fetch = FetchType.LAZY)
    open var usedBy: User? = null
}
