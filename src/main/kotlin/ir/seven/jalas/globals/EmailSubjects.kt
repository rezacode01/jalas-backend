package ir.seven.jalas.globals

class EmailSubjects {
    companion object {
        const val MEETING_RESERVED = "Room is reserved for meeting"
    }
}

class EmailBodies {
    companion object {
        const val MEETING_RESERVED = "Hello, " +
                "Your request to reserving room for meeting is checked and a room reserved for you." +
                "Good luck"
    }
}