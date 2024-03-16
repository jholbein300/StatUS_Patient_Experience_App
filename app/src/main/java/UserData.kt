package com.example.status_patient_home.Models

import java.sql.DriverManager
import java.sql.SQLException

class UserData{

    data class Users(
        val userId: Int,
        val roleId: Int,
        val email: String,
        val password: String,
        val utId: String
    ) {
    }

    fun getUsers(): MutableList<Users> {
        val userList = mutableListOf<Users>()

        // Create private data variables
        var uid: Int

        // URL from Azure JDBC Connection Strings
        val jdbcUrl =
            "jdbc:sqlserver://statusdbserver.database.windows.net:1433;database=StatUsDB;user=StatUs@statusdbserver;" +
                    "password=@zur3sux;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"

        try{
            DriverManager.getConnection(jdbcUrl).use { connection ->
                println(connection.isValid(0))

                // query to retrieve all movement ID's with user id of 4 (a.wood)
                val query = connection.prepareStatement(" SELECT * FROM users u;")

                // execute query and store in result
                val result = query.executeQuery()

                while (result.next()) {

                    // getting the value of the id column
                    uid = result.getInt("user_id")

                    // getting value of the room ID column
                    val roleId = result.getInt("role_id")

                    // getting value of the timeEntered column
                    val email = result.getString("email")

                    // getting the value of the timeLeft column
                    val password = result.getString("password")

                    val utId = result.getString("ut_id")

                    val currentPerson = Users(uid, roleId, email, password, utId)

                    userList.add(currentPerson)

                    println(currentPerson)
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return userList
    }
}