package com.example.status_patient_home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var careHistoryButton = findViewById<Button>(R.id.careHistoryButton)
        careHistoryButton.setOnClickListener {
            val intent = Intent(this, PatientCareHistory::class.java)
            startActivity(intent)
        }

        var logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            val intent = Intent(this, Login_View::class.java)
            startActivity(intent)
        }

        var sharedHistoryButton = findViewById<Button>(R.id.sharedHistoryButton)
        sharedHistoryButton.setOnClickListener {
            val intent = Intent(this, DoctorView::class.java)
            startActivity(intent)
        }
    }

}