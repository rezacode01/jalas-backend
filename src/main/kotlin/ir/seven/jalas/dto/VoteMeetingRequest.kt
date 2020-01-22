package ir.seven.jalas.dto

import ir.seven.jalas.enums.UserChoiceState

data class VoteMeetingRequest(
     val vote: UserChoiceState
)