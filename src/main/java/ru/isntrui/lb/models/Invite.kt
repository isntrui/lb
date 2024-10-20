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
class Invite(
    @Id @GeneratedValue var id: Long = 0,
    @Column(unique = true, nullable = false) var email: String,
    var code: String,
    @ManyToOne var made_by: User,
    var made_on: Boolean
)