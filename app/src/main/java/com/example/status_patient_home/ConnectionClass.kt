package com.example.status_patient_home

import android.os.StrictMode
import android.util.Log
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConnectionClass {
    private val jdbcUrl = "jdbc:sqlserver://statusdbserver.database.windows.net:1433;" +
            "database=StatUsDB;" +
            "user=StatUs@statusdbserver;" +
            "password=@zur3sux;" +
            "encrypt=true;" +
            "trustServerCertificate=false;" +
            "hostNameInCertificate=*.database.windows.net;" +
            "loginTimeout=30;"

    fun dbConnection(): Connection? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var conn: Connection? = null
        try {
            // This line ensures that the driver class is loaded and registered
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            conn = DriverManager.getConnection(jdbcUrl)
        } catch (ex: SQLException) {
            Log.e("Error : ", ex.message.toString())
        } catch (classError: ClassNotFoundException) {
            Log.e("Error : ", classError.message.toString())
        } catch (allError: Exception) {
            Log.e("Error : ", allError.message.toString())
        }
        return conn
    }
}