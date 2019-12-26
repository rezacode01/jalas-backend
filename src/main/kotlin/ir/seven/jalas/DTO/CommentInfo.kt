package ir.seven.jalas.DTO

import ir.seven.jalas.entities.Comment
import java.util.*

data class CommentInfo(
        val cid: String,
        val message: String,
        val date: String,
        val replyTo: CommentInfo?
) {
    constructor(entity: Comment): this(
            entity.commentId,
            entity.message,
            entity.date.toString(),
            if (entity.repliedComment != null)
                CommentInfo(entity.repliedComment!!)
            else
                null
    )
}