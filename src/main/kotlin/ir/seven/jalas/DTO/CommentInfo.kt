package ir.seven.jalas.DTO

import ir.seven.jalas.entities.Comment

data class CommentInfo(
        val user: UserInfo,
        val cid: String,
        val message: String,
        val date: String,
        val replyTo: CommentInfo?
) {
    constructor(entity: Comment): this(
            UserInfo(entity.user),
            entity.commentId,
            entity.message,
            entity.date.toString(),
            if (entity.repliedComment != null)
                CommentInfo(entity.repliedComment!!)
            else
                null
    )
}