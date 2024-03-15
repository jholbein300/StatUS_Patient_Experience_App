package com.example.status_patient_home

// import DBGrabber
// import Movement
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PatientCareHistory : AppCompatActivity() {
    private lateinit var patientRecyclerView: RecyclerView
    private lateinit var careArrayList: ArrayList<CareHistoryData>
    lateinit var heading : Array<String>
    lateinit var date : Array<String>
    lateinit var description : Array<String>

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

        //Adding in list elements for the timeline
        heading = arrayOf(
            "Nurse 1 Entered the Room1",
            "Doctor talked with patient",
            "Nurse administered patient's medication",
            "Doctor 1 Entered the Room1",
            "Nurse 2 Entered the Room1",
            "Doctor 1 Entered the Room1",
            "Doctor 2 Entered the Room1"
        )

        date = arrayOf(
            "10:00 AM, 12-Feb-2016",
            "12:00 AM, 12-Feb-2016",
            "2:00 PM, 12-Feb-2016",
            "2:25 PM, 12-Feb-2016",
            "4:00 PM, 12-Feb-2016",
            "5:00 PM, 12-Feb-2016",
            "5:00 PM, 12-Feb-2016"
        )

        description = arrayOf(
            "Patient given breakfast",
            "Doctor talked with patient",
            "Nurse administered patient's medication",
            "Doctor 1 Entered the Room1",
            "Nurse checks on patient",
            "Doctor 1 Entered the Room1",
            "Doctor 2 Entered the Room1"
        )

        patientRecyclerView = findViewById(R.id.timeline_view)
        patientRecyclerView.layoutManager = LinearLayoutManager(this)
        patientRecyclerView.setHasFixedSize(true)

        //Initializing the list
        careArrayList = arrayListOf<CareHistoryData>()
        getUserdata()
    }

    private fun getUserdata(){
        for(i in heading.indices){
            val data = CareHistoryData(heading[i], date[i], description[i])
            careArrayList.add(data)
        }
        patientRecyclerView.adapter = TimelineAdapter(careArrayList)
    }

}