package dataJpa

import ir.seven.jalas.JalasApplication
import ir.seven.jalas.entities.Meeting
import ir.seven.jalas.entities.User
import ir.seven.jalas.repositories.MeetingRepo
import ir.seven.jalas.repositories.UserRepo
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner

@Profile("test")
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [JalasApplication::class])
class MeetingDataJpaTest {

    @Autowired
    private lateinit var meetingRepository: MeetingRepo

    @Autowired
    private lateinit var userRepo: UserRepo

//    @Autowired
//    private lateinit var testEntityManager: TestEntityManager

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
                title = "title",
                creator = user
        )

        meetingRepository.save(meeting)

//        testEntityManager.persist(meeting)
//        testEntityManager.flush()

        Assert.assertEquals("qwerty", meetingRepository.findById("qwerty").get().mid)
    }
}