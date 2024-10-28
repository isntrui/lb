package ru.isntrui.lb.models

import jakarta.persistence.*
import lombok.Data
import lombok.EqualsAndHashCode
import java.sql.Date

@Entity
@Table(name = "texts")
@Data
@EqualsAndHashCode(of = ["id"])
open class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @ManyToOne(optional = false)
    open lateinit var wave: Wave

    @Column(nullable = false)
    open lateinit var body: String

    @Column(nullable = false)
    open lateinit var title: String

    @ManyToOne(optional = false)
    open lateinit var madeBy: User

    @Column(nullable = false)
    open lateinit var madeOn: Date

    @Column(nullable = false)
    open var isApproved: Boolean = false

    @ManyToOne
    open var approvedBy: User? = null

    open var approvedOn: Date? = null
}
