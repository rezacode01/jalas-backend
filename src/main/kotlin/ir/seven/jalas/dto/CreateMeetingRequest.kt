package ir.seven.jalas.dto

data class CreateMeetingRequest(
        val title: String,
        val participants: List<String>,
        val slots: List<CreateSlotRequest>
)