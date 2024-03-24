// https://levelup.gitconnected.com/how-to-access-database-with-kotlin-6b86f6680cd7
// eliana

// grab the encounter, then using m_id we get the movement information.
// using the u_id from the encounter we can fill it with what employee was there, role + name
// add column for patient user_id and employee user_id for each encounter

import java.sql.DriverManager

// create a model class
data class Encounter(
    val movementId: Int,
    val uid: String,
    val roomId: String,
    val timeEnter: String,
    val timeLeft: String)
{
}


fun grabEncounter(uid:String): Any {

    // Create private data variables
    val mid:Int
    //val uid:String
    val rid:String
    val timeEnter:String
    val timeLeft:String

    lateinit var date : Array<String>

    // val because it is never changed; hard-coded for testing
    //val roomID:Int = 101

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

    val connection = DriverManager.getConnection(jdbcUrl)

    // returns true if connection was successfully made
    println(connection.isValid(0))

    // query to retrieve all movement ID's with user id as inputted by the app
    val query = connection.prepareStatement("SELECT e.* FROM encounter e WHERE e.u_id = $uid;")

    // execute query and store in result
    val result = query.executeQuery()

    // create a list of movements -> to be later used in timeline view
    val movementUID4 = mutableListOf<Movement>()

    while(result.next()){

        // getting the value of the id column
        mid = result.getInt("movement_id")

        // getting the value of the name column
        // uid = result.getString("u_id")

        // getting value of the room ID column
        rid = result.getString("r_id")

        // getting value of the timeEntered column
        timeEnter = result.getString("time_entered")

        // getting the value of the timeLeft column
        timeLeft = result.getString("time_left")

        /*
        constructing a Movement object and putting data into the list
        */

        val currentMovement = Movement(mid, uid, rid, timeEnter, timeLeft)

        movementUID4.add(currentMovement)


        println("$mid $uid $rid $timeEnter $timeLeft")
        // or we do
        // println(currentMovement)
        return(currentMovement)

    }
    // return the list of objects -> to be iterated by XML Horizontal Layout View file
    return(movementUID4)
}
