package integration

import com.fasterxml.jackson.databind.ObjectMapper
import ir.seven.jalas.DTO.CreateMeetingRequest
import ir.seven.jalas.DTO.CreateSlotRequest
import ir.seven.jalas.JalasApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [JalasApplication::class])
@AutoConfigureMockMvc
class MeetingTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun createMeetingTest() {
        val meetingRequest = CreateMeetingRequest(
                "test meeting",
                listOf("rezacode01@gmail.com"),
                listOf(CreateSlotRequest(1576346400, 1576353600))
        )
        val mapper = ObjectMapper()
        val content = mapper.writeValueAsString(meetingRequest)

        mockMvc.perform(post("/meetings")
                .param("userId", "dwidbw")
                .content(content)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }
}