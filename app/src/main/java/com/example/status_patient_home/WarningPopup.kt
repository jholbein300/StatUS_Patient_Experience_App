package com.example.status_patient_home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed

class WarningPopup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warning_popup)

        val agreeButton = findViewById<Button>(R.id.agree_button)

        agreeButton.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(2000) {
                val pref = getSharedPreferences("logIn", Context.MODE_PRIVATE)
                val isLoggedIn = pref.getBoolean("isLoggedIn", false)

                //Decide whether to start LoginPage or HomePage as per user LogIn
                val targetActivity = if(isLoggedIn) PatientHomeView::class.java else LoginView::class.java
                startActivity(Intent(this, targetActivity))
                finish()
            }
        }
    }
}