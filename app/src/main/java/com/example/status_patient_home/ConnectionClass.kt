package com.example.status_patient_home

import android.os.StrictMode
import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

data class User(
    val id: Int,
    val roleName: String,
    val userType: String,
    val emailAddress: String,
    val firstName: String,
    val lastName: String

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

        // Login and retrieve user data using stored procedure
        fun tempLogin(email: String, password: String): User? {
            var user: User? = null
            try {
                DriverManager.getConnection(jdbcUrl).use { connection ->
                    if (connection.isValid(0)) {
                        val query = connection.prepareStatement("EXECUTE dbo.login ?, ?;")
                        query.setString(1, email)
                        query.setString(2, password)
                        val result = query.executeQuery()

                        if (result.next()) {
                            user = User(
                                result.getInt("user_id"),
                                result.getString("role_name"),
                                result.getString("ut_id"),
                                result.getString("email"),
                                result.getString("first_name"),
                                result.getString("last_name")
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                throw e
            }
            return user
        }
    }
}