package nu.westlin.kotlinlabs.database

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import java.sql.ResultSet

@SpringBootApplication
class DatabaseApplication

fun main(args: Array<String>) {
    runApplication<DatabaseApplication>(*args)
}


@RestController
@RequestMapping("/")
class MovieController(private val movieRepository: MovieRepository) {

    @GetMapping("movie/{id}")
    fun get(@PathVariable id: Int) = movieRepository.get(id)

    @GetMapping("movies")
    fun getAll() = movieRepository.getAll()

    @PostMapping("movie")
    fun create(title: String): Movie {
        return movieRepository.create(title)
    }

}

@Repository
class MovieRepository(private val jdbcTemplate: JdbcTemplate) {
    fun getAll(): List<Movie> {
        return jdbcTemplate.query("select id, title from movies"
        ) { rs: ResultSet, _: Int ->
            Movie(rs.getInt("id"), rs.getString("title"))
        }
    }

    fun get(id: Int): Movie? {
        return jdbcTemplate.queryForObject(
            "select id, title from movies where id = ?", RowMapper { rs, _ -> Movie(rs.getInt("id"), rs.getString("title")) }, id)
    }

    fun get(title: String): Movie? {
        return jdbcTemplate.queryForObject(
            "select id, title from movies where title = ?", RowMapper { rs, _ -> Movie(rs.getInt("id"), rs.getString("title")) }, title)
    }

    fun create(title: String): Movie {
        jdbcTemplate.update("insert into movies(title) values(?)", title)
        return get(title) ?: throw RuntimeException("Could not find movie ${title} which just was created")
    }

}

data class Movie(val id: Int, val title: String)