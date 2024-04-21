package com.example.status_patient_home

import android.os.StrictMode
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
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
        private const val ip = "statusdbserver.database.windows.net:1433"//DB IP address and port
        private const val dbName = "StatUsDB"
        private const val username = "StatUs@statusdbserver" //username from SQL Server
        private const val password = "@zur3sux"//password from SQL Server



        private const val jdbcUrl =
            "jdbc:sqlserver://statusdbserver.database.windows.net:1433;" +
                    "database=StatUsDB;" +
                    "user=StatUs@statusdbserver;" +
                    "password=@zur3sux;" +
                    "encrypt=true;" +
                    "trustServerCertificate=false;" +
                    "hostNameInCertificate=*.database.windows.net;" +
                    "loginTimeout=30;" +
                    "sslProtocol = TLSv1.2;"

        fun dbConnection(): Connection? {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            var conn: Connection? = null
            var conString: String? = null
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                conString =
                    "jdbc:jtds:sqlserver://" + ip + "/" + dbName;
                conn = DriverManager.getConnection(conString, username, password);

            } catch (ex: SQLException) {
                Log.e("SQL Error : ", ex.message.toString())


            } catch (classError: ClassNotFoundException) {
                Log.e("Class Error : ", classError.message.toString())
            } catch (allError: Exception) {
                Log.e("Error : ", "Unexpected error occurred", allError)
            }
            return conn
        }
        private val connection: Connection? = DriverManager.getConnection(jdbcUrl)

        fun login(email: String, password: String): String{
            // gets first name from DB for greeting textViews

            val query =
                ConnectDBmain.connection?.prepareStatement("SELECT u.role_id FROM users u WHERE" +
                        " u.email = $email AND u.password = $password;")


            val result = query?.executeQuery() //Query the database to find the role number
            val role = result?.let{
                if (it.next()) {
                    it.getString("role_id")
                } else {
                    "user not found"
                }
            } ?: "Error executing query"

            connection?.close()
        return role //return the role number to decide which screen to navigate to
        }
        // Login and retrieve user data using stored procedure
        fun tempLogin(email: String, password: String): User? {
            var user: User? = null
            try {
                DriverManager.getConnection(jdbcUrl).use { connection ->
                    if (connection.isValid(0)) {
                        Log.d("LoginDebug", "Connection established")
                        val call = connection.prepareCall("{call dbo.login(?, ?)}") //Edited Tim's class to use stored procedure, seeing if it would
                                                                                        //get rid of the errors
                        call.setString(1, email)
                        call.setString(2, password)
                        val result = call.executeQuery()

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