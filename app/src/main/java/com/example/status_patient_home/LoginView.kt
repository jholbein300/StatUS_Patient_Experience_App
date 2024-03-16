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
    val role = "1"
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

        //Using sharedPreference to check user is already logged in
        val pref = getSharedPreferences("login", Context.MODE_PRIVATE)
        val isLoggedIn = pref.getBoolean("isLoggedIn", false)
        val roleCheck = pref.getString("role", role)
        if(isLoggedIn) {
            //Check to see if the person is a doctor role 1 or patient role 2
            if(roleCheck.equals("1")){
                intent = Intent(this, DoctorHomeView::class.java)
                startActivity(intent)
                finish()
            } else {
                intent = Intent(this, PatientHomeView::class.java)
                startActivity(intent)
                finish()
            }
        }

        //Set click listener for login button
        loginBtn.setOnClickListener {

                val usernameEntered = username.text.toString()
                val passwordEntered = password.text.toString()

                //Hardcoded login
                if(usernameEntered == "j.kinsley@gmail.com" && passwordEntered == "securepwd") {
                    //Auto login feature for the patient
                    val editor = pref.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.putString("role", "2")
                    editor.apply()
                    Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, PatientHomeView::class.java))
                    finish()
                } else if(usernameEntered == "y.wasin@hospital.org" && passwordEntered == "pass123!@") {
                    //Doctor successful logged in
                    //Keeps the Doctor Logged in
                    val editor = pref.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.putString("role", "1")
                    editor.apply()
                    Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                    intent = Intent(this, DoctorHomeView::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginView, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                }
        }

    }
}

