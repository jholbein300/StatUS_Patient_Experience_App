package com.example.status_patient_home

// for using the main thread for UI updates

// import coroutines for background threads

// import InternetPermission class
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Patient Home View
class PatientHomeView : AppCompatActivity(), InternetPermissionCallback {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_home_view)

        val internetPermission = InternetPermission()
        internetPermission.checkInternetPermissionAndExecute(this, this)

        // username for greeting later make this a DB retrieval
        //val userFName = "John";
        lifecycleScope.launch {
            // this will run in the background 'thread' and be brought to the main thread
            val username = withContext(Dispatchers.IO) {
                val instance = ConnectDBmain.create() ?: throw IllegalStateException("ConnectDBmain.create() returned null")
                instance.getNameAsync(1).toString()
            }

            // network on main thread exception - do not use, even for testing
            //val instance = ConnectDBmain.create()
            //val username = instance.getNameAsync(1)

            // Retrieve the textview
            //val greetingTextView: TextView = findViewById(R.id.greetingTextView);

            // set the text of the TextView
            //greetingTextView.text = "Hello $username!"

            updateTextViewOnUiThread(username)
        }


        // temp working TextView writer using manual username
        //val userFName = "John"
        //val greetingTextView : TextView = findViewById(R.id.greetingTextView);
        //greetingTextView.text = "Hello $userFName!"

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
            val pref = getSharedPreferences("login", MODE_PRIVATE)
            val editor = pref.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
            Toast.makeText(this, "Logout completed", Toast.LENGTH_LONG).show()
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


        val requestCode = 1
        // create instance of the class
        val bluetoothScan = BluetoothScan(this)
        // before starting the Bluetooth scanning operation
        val permission = Manifest.permission.BLUETOOTH_SCAN

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            // Permission has already been granted, proceed with Bluetooth scanning
            // Start Bluetooth scanning operation
            bluetoothScan.scanLeDevice()
        }
        //call the scanLeDevice method
        bluetoothScan.scanLeDevice()
    }

    //eliana 03/02
    private var btPermission = false

    private fun scanBT(view: View) {
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Your device does not support bluetooth.",
                Toast.LENGTH_LONG).show()
        } else {
            // request bluetooth permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                bluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
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
            Toast.makeText(this, "Bluetooth permission denied",
                Toast.LENGTH_SHORT).show()
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

    private val internetPermission = InternetPermission()

    override fun onPermissionGranted() {
        // Permission granted, execute network operation or other tasks
        performNetworkOperation()
    }

    override fun onPermissionDenied() {
        // Permission denied, show toast message or handle accordingly
        Toast.makeText(
            this,
            "Internet permission denied. Cannot perform network operation.",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updateTextViewOnUiThread(username : String) {
        // create instance of the InternetPermission class
        runOnUiThread() {
            // passing context 'this' into the function so it can access Internet permissions
            // through this activity
            val greetingTextView: TextView = findViewById(R.id.greetingTextView)
            greetingTextView.text = "Welcome $username!"
        }
    }


    override fun updateTextView (username : String) {
        updateTextViewOnUiThread(username)
    }
    private fun checkInternetPermission() {
        internetPermission.checkInternetPermissionAndExecute(this, this)
    }
    private fun performNetworkOperation() {
        // Network operation code goes here
        // For example, call the function to update text view on UI thread
        updateTextView("John")
    }
}