package nu.westlin.kotlin

import nu.westlin.java.User

// Skapa en extension function på User (som är en Java-klass) som konverterar till en UserView
fun User.toUserView() = UserView(username, password, "$firstname $lastname")

data class UserView(
    val username: String, val password: String,
    val name: String
)