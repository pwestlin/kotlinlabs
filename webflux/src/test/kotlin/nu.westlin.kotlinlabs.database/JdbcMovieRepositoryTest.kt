package nu.westlin.kotlinlabs.database

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@JdbcTest
class JdbcMovieRepositoryTest : AbstractTransactionalJUnit4SpringContextTests() {

    val repository = JdbcMovieRepository(this.jdbcTemplate)

    @Test
    fun `create movie`() {
        val movie = repository.create("Foobar")

        assertThat(movie.id).isGreaterThanOrEqualTo(0)
        assertThat(movie.title).isEqualTo("Foobar")
        assertThat(repository.get(movie.id)).isEqualTo(movie)
    }
}