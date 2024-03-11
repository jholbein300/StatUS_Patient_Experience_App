package com.example.status_patient_home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import java.sql.ResultSet

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
            validateUser(usernameEntered, passwordEntered)



        }
    }
    private fun validateUser(username: String, password: String) {
        val connectionHelper = ConnectionClass()
        val connect = connectionHelper.dbConnection()
        if (connect != null) {
            try {
                val query = "SELECT * FROM dbo.users WHERE email = ? AND password = ?"
                val stmt = connect.prepareStatement(query)
                stmt.setString(1, username)
                stmt.setString(2, password)
                val resultSet: ResultSet = stmt.executeQuery()
                if (resultSet.next()) {
                    // Credentials are correct
                    navigateToHomeView()
                } else {
                    // Credentials are incorrect
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginView,
                            "Incorrect username or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@LoginView, "Login error: ${e.message}", Toast.LENGTH_LONG)
                        .show()
                }
            } finally {
                connect.close()
            }
        } else {
            runOnUiThread {
                Toast.makeText(this@LoginView, "Unable to connect to database", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    private fun navigateToHomeView() {
        // Navigate based on the user type
        // This example just navigates to PatientHomeView
        val intent = Intent(this, PatientHomeView::class.java)
        startActivity(intent)
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





