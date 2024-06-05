// https://levelup.gitconnected.com/how-to-access-database-with-kotlin-6b86f6680cd7
// eliana

import java.sql.DriverManager
// create a model class
data class Movement(
    val movementId: Int,
    val uid: String,
    val roomId: String,
    val timeEnter: String,
    val timeLeft: String,
    val movementType: String)
    //movement type added by Brandon
{
}


fun dBGrabber(): Any {

    // Create private data variables
    val mid:Int
    val uid:String

    val rid:String
    val timeEnter:String
    val timeLeft:String
    val mt: String
    // val because it is never changed; hard-coded for testing
    val roomID:Int = 101

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

    // query to retrieve all movement ID's with user id of 4 (a.wood)
    val query = connection.prepareStatement("SELECT m.* FROM movement m WHERE m.r_id = $roomID;")

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
        rid = result.getString("r_id")

        // getting value of the timeEntered column
        timeEnter = result.getString("time_entered")

        // getting the value of the timeLeft column
        timeLeft = result.getString("time_left")
        //mt added by Brandon
        mt = result.getString( "movement_type")
        //
        /*
        constructing a Movement object and putting data into the list
        */

        val currentMovement = Movement(mid, uid, rid, timeEnter, timeLeft, mt)
        //movement type added by brandon
        movementUID4.add(currentMovement)


        println("$mid $uid $rid $timeEnter $timeLeft $mt")
        // or we do
        // println(currentMovement)
        return(currentMovement)

    }
    // return the list of objects -> to be iterated by XML Horizontal Layout View file
    return(movementUID4)
}
