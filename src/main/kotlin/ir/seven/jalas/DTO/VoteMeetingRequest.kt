package ir.seven.jalas.DTO

import ir.seven.jalas.enums.UserChoiceState

data class VoteMeetingRequest(
     val vote: UserChoiceState
)