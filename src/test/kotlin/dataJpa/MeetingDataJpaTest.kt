package dataJpa

import ir.seven.jalas.JalasApplication
import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.entities.User
import ir.seven.jalas.repositories.MeetingRepo
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [JalasApplication::class])
class MeetingDataJpaTest {

    @Autowired
    private lateinit var meetingRepository: MeetingRepo

//    @Autowired
//    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun test() {
        val user = User(
                userId = "GyHKso",
                fullName = "rezacode",
                username = "rezacode01@gmail.com"
        )

        val meeting = Meeting(
                mid = "qwerty",
                title = "title",
                creator = user
        )

        meetingRepository.save(meeting)

//        testEntityManager.persist(meeting)
//        testEntityManager.flush()

        Assert.assertEquals("qwerty", meetingRepository.findById("qwerty").get().mid)
    }
}