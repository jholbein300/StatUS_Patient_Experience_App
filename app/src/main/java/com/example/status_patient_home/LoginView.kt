package com.example.status_patient_home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class LoginView : AppCompatActivity() {
    //private var connect: Connection? = null
    //private var connectResult = ""
    //Create private variables that are non-nullable and are initialized later
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    //Initialize UserData class to access the user data
    //private val userData = UserData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_view)

        //Initialize UI elements
        username = findViewById<EditText>(R.id.usernameLogin)
        password = findViewById<EditText>(R.id.passwordLogin)
        loginBtn = findViewById<Button>(R.id.loginBtn)

       //Creating the alert dialog for disclaimer
        val altDialogBuiler = AlertDialog.Builder(this@LoginView, R.style.AlertDialogCustom)

        altDialogBuiler.setMessage("Hello. This is a legally binding disclaimer, with some important words about how privacy will be handled. Please approve to use the app.")
        altDialogBuiler.setPositiveButton("Agree"){_,_ ->
        }

        val alertBox: AlertDialog = altDialogBuiler.create()
        alertBox.show()

        //Set click listener for login button
        loginBtn.setOnClickListener {
            //Gets the username and password entered by the user
            val usernameEntered = username.text.toString()
            val passwordEntered = password.text.toString()

            //Hardcoded login
            if(usernameEntered == "j.kinsley@gmail.com" && passwordEntered == "securepwd") {
                //Auto login feature for the patient
                val pref = getSharedPreferences("logIn", Context.MODE_PRIVATE)
                val editor = pref.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.apply()
                Toast.makeText(this, "Login completed", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, PatientHomeView::class.java))
                finish()
            } else if(usernameEntered == "y.wasin@hospital.org" && passwordEntered == "pass123!@") {
                //Doctor successful logged in
                intent = Intent(this, DoctorHomeView::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@LoginView, "Incorrect username or password", Toast.LENGTH_SHORT).show()
            }

            //Emily: Ignore this code
            //Perform database connection asynchronously using Kotlin coroutines
            /*GlobalScope.launch(Dispatchers.IO) {
                try{
                    //val connectionHelper = ConnectionClass()
                    //val connect = connectionHelper.dbConnection()

                    //Establish database connection
                    val conn = getConnection()
                    if(conn != null){
                        //Prepare SQL query to validate user
                        val validateUserQuery = "Select * FROM users WHERE email = ? AND password = ?"
                        val statm = conn.prepareStatement(validateUserQuery)
                        statm.setString(1, usernameEntered)
                        statm.setString(2, passwordEntered)
                        //Executes the query
                        val rs = statm.executeQuery()

                        if (rs.next()) {
                            //User is found
                            val roleId = rs.getInt("roleId")

                            //Redirect based on role
                            when (roleId) {
                                1 -> startActivity(Intent(this@LoginView, PatientHomeView::class.java))
                                2 -> startActivity(Intent(this@LoginView, DoctorHomeView::class.java))
                            }
                        } else {
                            runOnUiThread{
                                //Display error message for incorrect username or password
                                Toast.makeText(this@LoginView, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                            }
                        }

                        //Close resources
                        rs.close()
                        statm.close()
                        conn.close()

                    } else {
                        runOnUiThread{
                            //Display error message for connection failure
                            Toast.makeText(this@LoginView, "Connection Failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (ex: Exception){
                    //Log and display SQL exception
                    Log.e("Error", ex.message ?: "Unknown error")
                    runOnUiThread {
                        Toast.makeText(this@LoginView, "SQL Exception", Toast.LENGTH_SHORT).show()
                    }
                }
            }


            /*8        val users = userData.getUsers()

                    val validateUser = users.find { it.email == usernameEntered && it.password == passwordEntered}

                    //Hard coded the login credentials until connection to database is setup
                    if( validateUser != null) {
                        //Check if for patient Credentials
                        if( validateUser.roleId == 1){
                            //Patient credentials
                            val intent = Intent(this, PatientHomeView::class.java)
                            startActivity(intent)
                        } else if (validateUser.roleId == 2){
                            //Doctor credentials
                            intent = Intent(this, DoctorHomeView::class.java)
                            startActivity(intent)
                        }
                    } else {
                        //Login unsuccessful
                        Toast.makeText(this@LoginView, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                    }
        */
        }
    }

    private fun getConnection(): Connection? {
        var connection: Connection? = null
        //Database connection parameters
        val jdbcUrl =
            "jdbc:sqlserver://statusdbserver.database.windows.net:1433;" +
                    "database=StatUsDB;" +
                    "user=StatUs@statusdbserver;" +
                    "password=@zur3sux;" +
                    "encrypt=true;" +
                    "trustServerCertificate=false;" +
                    "hostNameInCertificate=*.database.windows.net;" +
                    "loginTimeout=30;"
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
            connection = DriverManager.getConnection(jdbcUrl)
        } catch (ex: ClassNotFoundException) {
            Log.e("Class not found", ex.message ?: "Unknown error")
        } catch (ex: SQLException) {
            Log.e("SQL Exception", ex.message ?: "Unknown error")
        }
*/
            // return connection
        }

    }
}


/* Goes next to the OnCreate function
 fun validateUser(v: View) {
       val users = findViewById<TextView>(R.id.usernameLogin)
       val pass = findViewById<TextView>(R.id.passwordLogin)

       try{
           val connectionHelper = ConnectionClass()
           connect = connectionHelper.dbConnection()
           if(connect != null){
               val query = "Select * from user"

           } else {

           }
       } catch (ex: Exception){
           Log.e("Error", ex.message.toString())
       }


   }*/





