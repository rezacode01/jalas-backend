package ir.seven.jalas.DTO

data class MeetingCommentRequest(
        val message: String,
        val replyTo: String?
)