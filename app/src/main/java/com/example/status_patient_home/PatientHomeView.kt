package com.example.status_patient_home

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


//Patient Home View
class PatientHomeView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_home_view)

        // button click listeners

        // care history button
        val careHistoryButton = findViewById<Button>(R.id.careHistoryButton)
        careHistoryButton.setOnClickListener {
            val intent = Intent(this, PatientCareHistory::class.java)
            startActivity(intent)
        }

        // logout button
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            //Disables auto login
            val pref = getSharedPreferences("login", Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()

            val intent = Intent(this, LoginView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        // check bluetooth connection button
        val checkBTbutton = findViewById<Button>(R.id.checkbtbutton)
        checkBTbutton.setOnClickListener {
            // Call function to check Bluetooth
            scanBT(it) // Passing the view that was clicked
        }
    }

    //eliana 03/02
    private var btPermission = false

    fun scanBT(view: View) {
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Your device does not support bluetooth.", Toast.LENGTH_LONG).show()
        } else {
            // request bluetooth permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                bluetoothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                bluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)
            }
        }
    }

    // bluetooth permission handling
    private val bluetoothPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){isGranted:Boolean ->
        if(isGranted){
            // bluetooth permission granted
            val bluetoothManager:BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter:BluetoothAdapter? = bluetoothManager.adapter
            btPermission = true

            // btScan()
            if(bluetoothAdapter?.isEnabled == false){
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBtIntent)
            } else {
                btScan()
            }
        }else{
            Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show()
            btPermission = false
        }
    }

    private val btActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            btScan()
        }
    }

    // handle bluetooth scan
    private fun btScan(){
        // implement scanning here
        Toast.makeText(this, "Bluetooth Connected Successfully", Toast.LENGTH_LONG).show()
    }

}