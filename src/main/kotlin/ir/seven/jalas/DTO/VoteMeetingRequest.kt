package ir.seven.jalas.DTO

import ir.seven.jalas.enums.UserChoiceState

data class VoteMeetingRequest(
     val username: String,
     val vote: UserChoiceState
)