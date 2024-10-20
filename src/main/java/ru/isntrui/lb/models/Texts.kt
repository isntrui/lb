package ru.isntrui.lb.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.Data
import lombok.EqualsAndHashCode
import java.sql.Date

@Table(name = "texts")
@Data
@Entity
@EqualsAndHashCode(of=["id"])
class Texts (
    @Id
    @GeneratedValue
    var id: Long,
    var wave: String,
    var body: String,
    var title: String,
    @ManyToOne var made_by: User,
    var made_on: Date,

    var is_approved: Boolean,
    @ManyToOne var approved_by: User?,
    var approved_on: Date?

)