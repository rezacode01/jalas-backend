import ir.seven.jalas.JalasApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [JalasApplication::class])
class JalasTestConfiguration {

    fun contextLoads() {
    }
}