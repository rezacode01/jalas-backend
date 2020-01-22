package ir.seven.jalas.dto

data class MeetingCommentRequest(
        val message: String,
        val replyTo: String?
)