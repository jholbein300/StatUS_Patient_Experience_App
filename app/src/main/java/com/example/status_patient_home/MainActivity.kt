package com.example.status_patient_home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var logout_button = findViewById<Button>(R.id.logout_button)
        logout_button.setOnClickListener {
            val intent = Intent(this, Login_View::class.java)
            startActivity(intent)
        }
    }

}