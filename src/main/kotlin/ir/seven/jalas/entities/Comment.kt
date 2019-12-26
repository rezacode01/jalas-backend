package ir.seven.jalas.entities

import net.bytebuddy.utility.RandomString
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "comments")
class Comment(
        @Id
        @Column(
                name = "cid",
                nullable = false
        )
        var commentId: String = RandomString.make(12),

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        var user: User = User(),

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "meeting_id")
        var meeting: Meeting = Meeting(),

        @Column(
                name = "message",
                nullable = false
        )
        var message: String = "",

        @Column(
                name = "creation_time",
                nullable = false
        )
        var date: Date = Date(),

        @JoinColumn(name = "reply_to")
        @ManyToOne(fetch = FetchType.LAZY)
        var repliedComment: Comment? = null

//        @OneToMany(
//                mappedBy = "repliedComment",
//                fetch = FetchType.LAZY,
//                cascade = [CascadeType.ALL]
//        )
//        var children: MutableList<Comment> = mutableListOf()
)