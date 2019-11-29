package ir.seven.jalas.entities

import ir.seven.jalas.enums.UserChoiceState
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "slots")
class Slot(
        @Id
        @Column(
                name = "slot_id",
                nullable = false
        )
        var slotId: String = "",

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "mid")
        var meeting: Meeting = Meeting(),

        @Column(
                name = "start_time",
                nullable = false
        )
        var startDate: Date = Date(),

        @Column(
                name = "end_time",
                nullable = false
        )
        var endDate: Date = Date(),

        @OneToMany(mappedBy = "slot")
        @LazyCollection(LazyCollectionOption.FALSE)
        var usersChoices: MutableList<UserChoice> = mutableListOf()
)