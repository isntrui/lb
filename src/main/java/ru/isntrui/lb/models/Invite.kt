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
    public val id: Long? = null

    @Column(unique = true, nullable = false)
    public var email: String? = null

    public val code: String? = null

    public val madeOn: Boolean? = null

    public val isUsed: Boolean = false


    @ManyToOne
    @Nullable
    public val usedBy: User? = null
}