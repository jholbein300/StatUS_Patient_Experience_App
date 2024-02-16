// https://levelup.gitconnected.com/how-to-access-database-with-kotlin-6b86f6680cd7
// eliana
import com.zaxxer.hikari.HikariDataSource
import java.sql.DriverManager

// create a model class
data class Movement(val movementId: Int, val uid: String, val roomId: String, val timeEnter: String, val timeLeft: String){
}

fun main(){

    // Create private data variables
    var mid:Int;
    var uid:String;

    // URL from Azure JDBC Connection Strings
    val jdbcUrl =
        "jdbc:sqlserver://statusdbserver.database.windows.net:1433;database=StatUsDB;user=StatUs@statusdbserver;password=@zur3sux;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"

    val connection = DriverManager
        .getConnection(jdbcUrl)

    // returns true if connection was successfully made
    println(connection.isValid(0))

    // query to retrieve all movement ID's with user id of 4 (a.wood)
    val query = connection.prepareStatement(" SELECT m.* FROM movement m WHERE m.r_id = 101;")

    // execute query and store in result
    val result = query.executeQuery()

    // create a list of movements -> to be later used in timeline view
    val movementUID4 = mutableListOf<Movement>()

    while(result.next()){

        // getting the value of the id column
        mid = result.getInt("movement_id")

        // getting the value of the name column
        uid = result.getString("u_id")

        // getting value of the room ID column
        val rid = result.getString("r_id")

        // getting value of the timeEntered column
        val timeEnter = result.getString("time_entered")

        // getting the value of the timeLeft column
        val timeLeft = result.getString("time_left")

        /*
        constructing a Movement object and
        putting data into the list
         */

        val currentMovement = Movement(mid, uid, rid, timeEnter, timeLeft)

        movementUID4.add(currentMovement)

        println(Movement(mid, uid, rid, timeEnter, timeLeft))
    }
}

