package ru.isntrui.lb.models

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "invites")
open class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(unique = true, nullable = false)
    var email: String? = null

    @Column(nullable = false)
    var code: String = ""

    @Column(name = "made_on", nullable = false)
    var madeOn: LocalDate = LocalDate.now()

    @Column(nullable = false)
    var isUsed: Boolean = false

    @ManyToOne(fetch = FetchType.LAZY)
    var usedBy: User? = null
}
