package se.lantmateriet.taco.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import se.lantmateriet.taco.java.User

class UserTest {

    @Test
    fun `create view of user`() {
        val user = User("petves", "secret", "Peter", "Westlin")

        val userView = user.toUserView()
        with(userView) {
            assertThat(username).isEqualTo(user.username)
            assertThat(password).isEqualTo(user.password)
            assertThat(name).isEqualTo("${user.firstname} ${user.lastname}")
        }
    }
}