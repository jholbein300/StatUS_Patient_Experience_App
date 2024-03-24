// https://levelup.gitconnected.com/how-to-access-database-with-kotlin-6b86f6680cd7
// eliana

import java.sql.DriverManager

// create a model class
data class Encounter(
    val encounterId: Int,
    val roomId: String,
    val timeEnter: String,
    val timeLeft: String,
    val timeLeft1: String
)
{
}

fun encounterGrab(uid:String): Any {

    // Create private data variables

    // puid and euid are patient and employee, respectively
    // adding those to DB soon
    val puid:Int = uid.toInt() ;
    var euid:Int = 0;

    val eid:Int;
    val rid:String;
    val timeEnter:String;
    val timeLeft:String;

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

    // query to retrieve all encounters with patient user id
    val encQuery = connection.prepareStatement("SELECT e.* FROM encounter e WHERE e.pu_id = $puid;")

    // execute query and store in result
    val encResult = encQuery.executeQuery()

    // create a list of movements -> to be later used in timeline view
    val encounters = mutableListOf<Encounter>()

    while(encResult.next()){

        // getting the value of the id column
        eid = encResult.getInt("encounter_id")



        // getting value of the room ID column
        rid = encResult.getString("r_id")

        // getting value of the timeEntered column
        timeEnter = encResult.getString("time_entered")

        // getting the value of the timeLeft column
        timeLeft = encResult.getString("time_left")

        /*
        constructing an Encounter object and putting data into the list
        */

        val currentEncounter = Encounter(eid, uid, rid, timeEnter, timeLeft)

        encounters.add(currentEncounter)

        println("$eid $uid $rid $timeEnter $timeLeft")
        // or we do
        // println(currentMovement)
        return(currentEncounter)

    }

    // query to retrieve user name from user table given the euid from encounter table
    val euidQuery = connection.prepareStatement("SELECT u.f_name FROM user u WHERE u.uid = $euid;")

    val euidResult = euidQuery.executeQuery()

    //getting first name of nurse or doctor who cared for you
    val empfName = encResult.getInt("f_name")

    println("Employee $empfName")
    // return the list of objects -> to be iterated by XML Horizontal Layout View file
    return(encounters)
}
