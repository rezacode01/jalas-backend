package ir.seven.jalas.services

import ir.seven.jalas.dto.CommentInfo
import ir.seven.jalas.dto.MeetingCommentRequest

interface CommentService {
    fun getComments(meetingId: String): List<CommentInfo>
    fun createComment(meetingId: String, username: String, request: MeetingCommentRequest): CommentInfo
    fun editComment(meetingId: String, commentId: String, request: MeetingCommentRequest): CommentInfo
    fun deleteComment(meetingId: String, commentId: String)

    fun isCommentCreator(username: String, commentId: String): Boolean
}