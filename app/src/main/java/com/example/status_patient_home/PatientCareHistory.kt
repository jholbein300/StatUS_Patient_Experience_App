package com.example.status_patient_home

// import DBGrabber
// import Movement
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PatientCareHistory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_care_history)

        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            //Disables auto login feature
            val pref = getSharedPreferences("login", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
            Toast.makeText(this, "LogOut completed", Toast.LENGTH_LONG).show()
            val intent = Intent(this, LoginView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        val homeButton = findViewById<Button>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, PatientHomeView::class.java)
            startActivity(intent)
        }

    }
}