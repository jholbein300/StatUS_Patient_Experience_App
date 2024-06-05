package com.example.status_patient_home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DoctorHomeView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_home_view)

        // Eliana

        // username for greeting later make this a DB retrieval
        val userFName = "Yasmin";

        // Retrieve the textview
        val greetingTextView: TextView = findViewById(R.id.greetingTextView);

        // set the text of the TextView
        greetingTextView.text = "Hello $userFName!"

        val patientCareButton = findViewById<Button>(R.id.patientCareHistory)
        patientCareButton.setOnClickListener() {
            val intent = Intent(this, DoctorView::class.java)
            startActivity(intent)
        }

        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            val pref = getSharedPreferences("login", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
            Toast.makeText(this, "Logout completed", Toast.LENGTH_LONG).show()
            val intent = Intent(this, LoginView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
        //added by Brandon
        val docActivityButton = findViewById<Button>(R.id.docActivityButton)
        docActivityButton.setOnClickListener {

            val intent = Intent(this, setActivity2::class.java)
            startActivity(intent)
        }
    }
}