package com.example.status_patient_home

import android.os.StrictMode
import android.util.Log
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.math.log

class ConnectionClass {
    private val ip = ""//DB IP address and port
    private val dbName = "StatUs"
    private val username = "username" //username from SQL Server
    private val password = "password"//password from SQL Server

    fun dbConnection() : Connection? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var conn : Connection? = null
        var conString : String? = null
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            conString = "jdbc:jtds:sqlserver://$ip;databaseName=$dbName;username=$username;password=$password;"
            conn = DriverManager.getConnection(conString)
        } catch (ex : SQLException){
            Log.e("Error : ", ex.message.toString())
            
        } catch (classError : ClassNotFoundException) {
            Log.e( "Error : ", classError.message.toString())
        } catch (allError : Exception){
            Log.e("Error : ", allError.message.toString())
        }
        return conn
    }
}