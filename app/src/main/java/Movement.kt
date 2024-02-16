// https://levelup.gitconnected.com/how-to-access-database-with-kotlin-6b86f6680cd7
// eliana
import com.zaxxer.hikari.HikariDataSource
import java.sql.DriverManager

// create a model class
data class Movement(val movementId: Int, val uid: String, val roomId: String, val timeEnter: String, val timeLeft: String){
}

fun main(){

    val dataSource = HikariDataSource()

    dataSource.jdbcUrl = "jdbc:sqlserver://statusdbserver.database.windows.net/StatUsDB"

    println(dataSource.maximumPoolSize)

    // set the username
    dataSource.username = "StatUs"

    // set the password
    dataSource.password = "@zur3sux"

    val connection = dataSource.connection
    // returns true if connection was successfully made
    println(connection.isValid(0))

    // query to retrieve all movement ID's with user id of 4 (a.wood)
    val query = connection.prepareStatement(" SELECT m.* FROM movement m WHERE m.u_id = 4;")

    // execute query and store in result
    val result = query.executeQuery()

    val movementUID4 = mutableListOf<Movement>()

    while(result.next()){

        // getting the value of the id column
        val mid = result.getInt("movement_id")

        // getting the value of the name column
        val uid = result.getString("u_id")

        val rid = result.getString("r_id")

        val timeEnter = result.getString("time_entered")

        val timeLeft = result.getString("time_left")

        /*
        constructing a Movement object and
        putting data into the list
         */
        movementUID4.add(Movement(mid, uid, rid, timeEnter, timeLeft))
    }
    println(movementUID4)
}

