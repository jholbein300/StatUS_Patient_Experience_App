package com.example.status_patient_home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.SearchView

class DoctorView : AppCompatActivity() {
    private lateinit var patientRecyclerView: RecyclerView
    private lateinit var careArrayList: ArrayList<CareHistoryData>
    //private lateinit var heading : Array<String>
    //private lateinit var date : Array<String>
    //private lateinit var description : Array<String>
    private lateinit var spinner: Spinner
    private lateinit var searchView: SearchView
    private lateinit var items: Array<String>
    lateinit var headingSmith : Array<String>
    lateinit var dateSmith : Array<String>
    lateinit var descriptionSmith : Array<String>
    lateinit var headingYoung : Array<String>
    lateinit var dateYoung : Array<String>
    lateinit var descriptionYoung : Array<String>
    private var selectedPatientName: String = ""
    private lateinit var adapter: ArrayAdapter<String>


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_view)

        careArrayList = arrayListOf()

        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
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
            val intent = Intent(this, DoctorHomeView::class.java)
            startActivity(intent)
        }
        fun updateRecyclerViewForPatient(selectedPatient: String) {
            // Clear existing data
            careArrayList.clear()

            // Decide which dataset to load based on the selected patient
            if (selectedPatient == "A. Young") {
                // Assuming you will set up arrays or data retrieval for A. Young
                // Just an example, replace with actual data setup
                careArrayList.clear() // Clear existing data
                // Populate careArrayList with data specific to A. Young
                getUserdata()
            } else if (selectedPatient == "T. Smith") {
                careArrayList.clear() // Ensure list is clear before adding new data
                // Add data for T. Smith or keep existing setup
                getUserdata()

            }
            // Notify the adapter that data has changed
            patientRecyclerView.adapter?.notifyDataSetChanged()
        }

        val patientSpinner = findViewById<Spinner>(R.id.patientSpinner)
        patientSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedPatientName = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        searchView = findViewById(R.id.patientSearch)

        //access the array resouce
        items = resources.getStringArray(R.array.patientName_array)

        //Array adapter
        adapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items)
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)

        //Setting up the search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterItems(newText.orEmpty())
                return true
            }
        })

        val enterButton = findViewById<Button>(R.id.enterBtn)
        enterButton.setOnClickListener {
            updateRecyclerViewForPatient(selectedPatientName);
        }

        //Access the array resource


        headingSmith = arrayOf(
            "Nurse 1 Entered the Room1",
            "Doctor talked with patient",
            "Nurse administered patient's medication",
            "Doctor 1 Entered the Room1",
            "Nurse 2 Entered the Room1",
            "Doctor 1 Entered the Room1",
            "Doctor 2 Entered the Room1"
        )

        dateSmith = arrayOf(
            "10:00 AM, 12-Feb-2016",
            "12:00 AM, 12-Feb-2016",
            "2:00 PM, 12-Feb-2016",
            "2:25 PM, 12-Feb-2016",
            "4:00 PM, 12-Feb-2016",
            "5:00 PM, 12-Feb-2016",
            "5:00 PM, 12-Feb-2016"
        )

        descriptionSmith = arrayOf(
            "Patient given breakfast",
            "Doctor talked with patient",
            "Nurse administered patient's medication",
            "Doctor 1 Entered the Room1",
            "Nurse checks on patient",
            "Doctor 1 Entered the Room1",
            "Doctor 2 Entered the Room1"
        )
        //Adding in list elements for the timeline
        headingYoung = arrayOf(
            "This is to show it changes for A. Young",
            "Doctor talked with patient",
            "Nurse administered patient's medication",
            "Doctor 1 Entered the Room1",
            "Nurse 2 Entered the Room1",
            "Doctor 1 Entered the Room1",
            "Doctor 2 Entered the Room1"
        )

        dateYoung = arrayOf(
            "test 10:00 AM, 12-Feb-2016",
            "12:00 AM, 12-Feb-2016",
            "2:00 PM, 12-Feb-2016",
            "2:25 PM, 12-Feb-2016",
            "4:00 PM, 12-Feb-2016",
            "5:00 PM, 12-Feb-2016",
            "5:00 PM, 12-Feb-2016"
        )

        descriptionYoung = arrayOf(
            "test given breakfast",
            "Doctor talked with patient",
            "Nurse administered patient's medication",
            "Doctor 1 Entered the Room1",
            "Nurse checks on patient",
            "Doctor 1 Entered the Room1",
            "Doctor 2 Entered the Room1"
        )


        //Connects the timeline
        patientRecyclerView = findViewById(R.id.timeline_view)
        patientRecyclerView.layoutManager = LinearLayoutManager(this)
        patientRecyclerView.setHasFixedSize(true)


    }
    private fun getUserdata(){
        if(selectedPatientName == "T. Smith"){
            for(i in headingSmith.indices){
                val data = CareHistoryData(headingSmith[i], dateSmith[i], descriptionSmith[i])
                careArrayList.add(data)
            }
        }
        else if(selectedPatientName == "A. Young"){
            for(i in headingYoung.indices){
                val data = CareHistoryData(headingYoung[i], dateYoung[i], descriptionYoung[i])
                careArrayList.add(data)
            }
        }

        patientRecyclerView.adapter = TimelineAdapter(careArrayList)
    }
    private fun filterItems(query: String) {
        val filterItem = items.filter { it.contains(query, ignoreCase = true) }
        adapter.clear()
        adapter.addAll(filterItem)
        adapter.notifyDataSetChanged()
    }
}