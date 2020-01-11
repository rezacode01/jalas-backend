package ir.seven.jalas.entities

import ir.seven.jalas.enums.UserChoiceState
import net.bytebuddy.utility.RandomString
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
        var slotId: String = RandomString.make(10),

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

        @OneToMany(mappedBy = "slot", cascade = [CascadeType.ALL])
        @LazyCollection(LazyCollectionOption.FALSE)
        var usersChoices: MutableList<UserChoice> = mutableListOf()
) {
        @Transient
        fun getTotalChoices(): Int {
                val agreeCount = this.usersChoices.filter { it.state == UserChoiceState.AGREE }.size
                val disAgreeCount = this.usersChoices.filter { it.state == UserChoiceState.DISAGREE }.size
                return agreeCount - disAgreeCount
        }

        @Transient
        fun getTotalAgreeCounts(): Int {
                return this.usersChoices.filter { it.state == UserChoiceState.AGREE }.size
        }

        @Transient
        fun getTotalDisAgreeCounts(): Int {
                return this.usersChoices.filter { it.state == UserChoiceState.DISAGREE }.size
        }

        @Transient
        fun getTotalSoSoCounts(): Int {
                return this.usersChoices.filter { it.state == UserChoiceState.SO_SO }.size
        }
}