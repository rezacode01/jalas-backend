package ir.seven.jalas.DTO

data class CreateMeetingRequest(
        val title: String,
        val participants: List<String>,
        val slots: List<CreateSlotRequest>
)