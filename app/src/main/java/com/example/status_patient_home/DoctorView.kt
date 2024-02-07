package com.example.status_patient_home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner

class DoctorView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_view)

        var spinnerId = findViewById<Spinner>(R.id.patientSpinner)
    }
}