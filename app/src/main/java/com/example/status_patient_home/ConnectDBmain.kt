package com.example.status_patient_home

import java.sql.Connection
import java.sql.DriverManager

class ConnectDBmain {

    companion object {
        private val jdbcUrl =
            "jdbc:sqlserver://statusdbserver.database.windows.net:1433;" +
                    "database=StatUsDB;" +
                    "user=StatUs@statusdbserver;" +
                    "password=@zur3sux;" +
                    "encrypt=true;" +
                    "trustServerCertificate=false;" +
                    "hostNameInCertificate=*.database.windows.net;" +
                    "loginTimeout=30;"

        val connection: Connection? = DriverManager.getConnection(jdbcUrl)

        fun create(): ConnectDBmain {
            // factory method to create instances of ConnectDBmain class
            return ConnectDBmain()
        }

    }

    // function to retrieve name from DB, inputs int and returns string type
    fun getNameAsync(userID: Int): String {

        // gets first name from DB for greeting textViews
        val query =
            connection?.prepareStatement("SELECT u.first_name FROM users u WHERE u.user_id = $userID;")

        val result = query?.executeQuery()
        // error checking the result
        val name = result?.let{
            if (it.next()) {
                it.getString("first_name")
            } else {
                "user not found"
            }
        } ?: "Error executing query"

        // closes the DB connection before returning the name
        connection?.close()
        return name
    }

}

