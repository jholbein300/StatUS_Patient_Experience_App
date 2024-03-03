package com.example.status_patient_home

// import DBGrabber
// import Movement
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class PatientCareHistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_care_history)

        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginView::class.java)
            startActivity(intent)
        }

        val homeButton = findViewById<Button>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, PatientHomeView::class.java)
            startActivity(intent)
        }

    }
}