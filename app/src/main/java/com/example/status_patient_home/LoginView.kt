package com.example.status_patient_home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginView : AppCompatActivity() {
    //private var connect: Connection? = null
    //private var connectResult = ""
    //Create private variables that are non-nullable and are initialized later
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_view)

        username = findViewById<EditText>(R.id.usernameLogin)
        password = findViewById<EditText>(R.id.passwordLogin)
        loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener {
            val usernameEntered = username.text.toString()
            val passwordEntered = password.text.toString()

            //Hard coded the login crendentials until connection to database is setup
            if(usernameEntered == "j.kinsley@gmail.com" && passwordEntered == "securepwd") {
                //Patient credentials
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else if(usernameEntered == "y.wasin@hospital.org" && passwordEntered == "pass123!@") {
                //Login for the doctor
                intent = Intent(this, DoctorView::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@LoginView, "Incorrect username or password", Toast.LENGTH_SHORT).show()
            }

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





