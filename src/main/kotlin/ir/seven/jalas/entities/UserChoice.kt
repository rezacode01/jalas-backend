package ir.seven.jalas.entities

import ir.seven.jalas.enums.UserChoiceState
import net.bytebuddy.utility.RandomString
import javax.persistence.*

@Entity
@Table(name = "user_choices")
class UserChoice(

    @Id
    @Column(
            name = "id",
            nullable = false
    )
    var id: String = RandomString.make(6),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User = User(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id")
    var slot: Slot = Slot(),

    @Column(
            name = "state",
            nullable = false
    )
    var state: UserChoiceState = UserChoiceState.SO_SO
)
