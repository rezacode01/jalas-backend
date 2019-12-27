package ir.seven.jalas.DTO

import ir.seven.jalas.entities.Comment

data class CommentInfo(
        val user: UserInfo,
        val cid: String,
        val message: String,
        val date: String,
        val replyTo: CommentInfo?
) {
    constructor(entity: Comment, depth: Int = 1): this(
            UserInfo(entity.user),
            entity.commentId,
            entity.message,
            entity.date.toString(),
            if (entity.repliedComment != null && depth > 0)
                CommentInfo(entity.repliedComment!!, depth - 1)
            else
                null
    )
}