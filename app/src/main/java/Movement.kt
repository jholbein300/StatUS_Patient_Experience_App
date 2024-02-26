import java.sql.DriverManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// create a model class
data class Movement(
    val movementId: Int,
    val uid: String,
    val roomId: String,
    val timeEnter: String,
    val timeLeft: String
)

fun DBGrabber(callback: (List<Movement>) -> Unit) {
    // Create private data variables
    val mid: Int
    val uid: String
    val rid: String
    val timeEnter: String
    val timeLeft: String

    // val because it is never changed; hard-coded for testing
    val roomID: Int = 101

    // URL from Azure JDBC Connection Strings
    val jdbcUrl =
        "jdbc:sqlserver://statusdbserver.database.windows.net:1433;" +
                "database=StatUsDB;" +
                "user=StatUs@statusdbserver;" +
                "password=@zur3sux;" +
                "encrypt=true;" +
                "trustServerCertificate=false;" +
                "hostNameInCertificate=*.database.windows.net;" +
                "loginTimeout=30;"

    // Launch a coroutine in IO context to perform the database operation
    GlobalScope.launch(Dispatchers.IO) {
        val movements = mutableListOf<Movement>()
        DriverManager.getConnection(jdbcUrl).use { connection ->
            if (connection.isValid(0)) {
                val query = connection.prepareStatement("SELECT m.* FROM movement m WHERE m.r_id = ?;")
                query.setInt(1, roomID)
                val result = query.executeQuery()
                while (result.next()) {
                    val movement = Movement(
                        result.getInt("movement_id"),
                        result.getString("u_id"),
                        result.getString("r_id"),
                        result.getString("time_entered"),
                        result.getString("time_left")
                    )
                    movements.add(movement)
                }
            }
        }
        // Once the database operation is complete, invoke the callback with the list of movements
        callback(movements)
    }
}
