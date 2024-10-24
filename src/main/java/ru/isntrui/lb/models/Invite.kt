package ru.isntrui.lb.models

import jakarta.annotation.Nullable
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "invites")
open class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public var id: Long? = null

    @Column(unique = true, nullable = false)
    public var email: String? = null

    public var code: String = ""

    // Use LocalDate for better compatibility
    public var madeOn: LocalDate = LocalDate.now()

    public var isUsed: Boolean = false

    @ManyToOne
    @Nullable
    public var usedBy: User? = null
}