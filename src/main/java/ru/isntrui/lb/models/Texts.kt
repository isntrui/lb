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
open class Texts (
    @Id
    @GeneratedValue
    private var id: Long,
    private var wave: String,
    private var body: String,
    private var title: String,
    @ManyToOne private var made_by: User,
    private var made_on: Date,

    private var is_approved: Boolean,
    @ManyToOne private var approved_by: User?,
    private var approved_on: Date?

)