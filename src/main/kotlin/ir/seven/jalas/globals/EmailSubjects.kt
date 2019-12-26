package ir.seven.jalas.globals

class EmailSubjects {
    companion object {
        const val MEETING_RESERVED = "Room is reserved for meeting"
        const val MEETING_ADD_NEW_SLOT = "A new slot added to meeting"
        const val MEETING_NEW_VOTE = "A new vote committed to meeting"
    }
}

class EmailBodies {
    companion object {
        const val MEETING_RESERVED = "Hello, " +
                "Your request to reserving room for meeting is checked and a room reserved for you." +
                "Good luck"
    }
}