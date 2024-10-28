package ru.isntrui.lb.models

import jakarta.persistence.*
import lombok.Data
import lombok.EqualsAndHashCode
import org.hibernate.annotations.CreationTimestamp
import java.sql.Date
import java.time.LocalDateTime

@Entity
@Table(name = "texts")
@Data
@EqualsAndHashCode(of = ["id"])
open class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @ManyToOne(optional = false)
    open var wave: Wave? = null

    @Column(nullable = false)
    open lateinit var body: String

    @Column(nullable = false)
    open lateinit var title: String

    @ManyToOne(optional = false)
    open lateinit var madeBy: User

    @Column(nullable = false)

    @CreationTimestamp
    open lateinit var madeOn: LocalDateTime

    @Column(nullable = false)
    open var isApproved: Boolean = false

    @ManyToOne
    open var approvedBy: User? = null

    open var approvedOn: Date? = null
}
