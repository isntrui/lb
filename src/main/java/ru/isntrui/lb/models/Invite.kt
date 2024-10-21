package ru.isntrui.lb.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.Data
import lombok.EqualsAndHashCode

@Entity
@Table(name = "invites")
@Data
@EqualsAndHashCode(of = ["id"])
open class Invite(
    @Id @GeneratedValue private var id: Long = 0,
    @Column(unique = true, nullable = false) private var email: String,
    private var code: String,
    @ManyToOne private var made_by: User,
    private var made_on: Boolean
)