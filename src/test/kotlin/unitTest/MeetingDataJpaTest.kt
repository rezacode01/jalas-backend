package unitTest

import ir.seven.jalas.JalasApplication
import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.entities.User
import ir.seven.jalas.repositories.MeetingRepo
import ir.seven.jalas.repositories.UserRepo
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.test.context.junit4.SpringRunner

@Profile("test")
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [JalasApplication::class])
class MeetingDataJpaTest {

    @Autowired
    private lateinit var meetingRepository: MeetingRepo

    @Autowired
    private lateinit var userRepo: UserRepo

    @Test
    fun test() {
        println("first test is run successfully")
        val user = User(
                userId = "GyHKso",
                fullName = "rezacode",
                username = "rezacode@gmail.com"
        )

        userRepo.save(user)

        val meeting = Meeting(
                mid = "qwerty",
                title = "title"
        )

        meetingRepository.save(meeting)

        Assert.assertEquals("qwerty", meetingRepository.findById("qwerty").get().mid)
    }
}