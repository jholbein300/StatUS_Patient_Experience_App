package com.example.status_patient_home

import android.os.StrictMode
import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

data class User (
    val id: Int,
    val roleName: String,
    val emailAddress: String,
    val userType: String

)



@OptIn(DelicateCoroutinesApi::class)
class ConnectionClass {
    // TLD: placing these in a companion object should allow these methods to be static
    // (callable without instantiating ConnectionClass). If it doesn't work, try removing
    // the companion object wrapper and the const keyword from the vals below.
    companion object {
        private const val ip = "183.611.1.222:1433"//DB IP address and port
        private const val dbName = "StatUsDB"
        private const val username = "username" //username from SQL Server
        private const val password = "password"//password from SQL Server

        private const val jdbcUrl =
            "jdbc:sqlserver://statusdbserver.database.windows.net:1433;" +
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
            var conString: String? = null
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                conString =
                    "jdbc:jtds:sqlserver://$ip;databaseName=$dbName;username=$username;password=$password;"
                conn = DriverManager.getConnection(conString)
            } catch (ex: SQLException) {
                Log.e("Error : ", ex.message.toString())

            } catch (classError: ClassNotFoundException) {
                Log.e("Error : ", classError.message.toString())
            } catch (allError: Exception) {
                Log.e("Error : ", allError.message.toString())
            }
            return conn
        }

        // Login and retrieve user data
        fun tempLogin(email: String, password: String, callback: (User?) -> Unit) {
            var user: User? = null
            // It may be more secure to set parameters using query.setString(...)
            val procString = "EXECUTE dbo.login '$email', '$password';"

            GlobalScope.launch(Dispatchers.IO) {
                DriverManager.getConnection(jdbcUrl).use { connection ->
                    if (connection.isValid(0)) {
                        val query = connection.prepareStatement(procString)
                        val result = query.executeQuery()
                        if (result.next()) {
                            user = User(
                                result.getInt("user_id"),
                                result.getString("role_name"),
                                result.getString("email"),
                                result.getString("ut_id")
                            )
                        }
                    }
                }
            }
            // Returns null if DB connection or user authentication fails.
            callback(user)
        }
    }
}