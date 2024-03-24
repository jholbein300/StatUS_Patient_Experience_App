package com.example.status_patient_home

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


//Patient Home View
class PatientHomeView : AppCompatActivity() {

    // for ListView on home-screen for testing only
    private lateinit var deviceListView: ListView
    private lateinit var deviceListAdapter: ArrayAdapter<String>
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device = result.device
            val deviceName = if (ActivityCompat.checkSelfPermission(
                    this@PatientHomeView,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Handle permission request
                return
            } else {
                // Permission already granted, continue with logic
                // For example, you can initialize the deviceName here
                ""
            }
            device.name ?: "Unknown"
            val deviceAddress = device.address
            val deviceInfo = "$deviceName - $deviceAddress"
            deviceListAdapter.add(deviceInfo)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Toast.makeText(
                this@PatientHomeView,
                "Scan failed with error code: $errorCode",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // sample array for BLE table shown
    val sampleData = arrayOf(
        "BCPro_190653",
        "BCPro_190652",
        "Device 3",
        "Device 4",
        "Device 5"
    )

    private val currentRoomID = "Room 101"
    val currentRoomTextView = findViewById<TextView>(R.id.currentRoomTextView)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_home_view)
        // Initialize views
        deviceListView = findViewById(R.id.deviceListView)

        // Initialize Bluetooth adapter
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        // Initialize adapter for ListView
        deviceListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sampleData)
        deviceListView.adapter = deviceListAdapter

        // Check Bluetooth permissions and start scanning
        checkBluetoothPermissionsAndScan()

        // testing with sample text variable
        //currentRoomTextView.text = currentRoomID

        ////////////////////////
        // BUTTON CLICK LISTENERS

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
    private fun scanBT(view: View) {
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Your device does not support bluetooth.", Toast.LENGTH_LONG)
                .show()
        } else {
            // request bluetooth permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // READ DEVICE NAME AND ESTABLISH A CONNECTION
                bluetoothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                // BLUETOOTH DEVICE DISCOVERY
                bluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)
            }
        }
        val scanner = bluetoothAdapter?.bluetoothLeScanner
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
    }

    private fun checkBluetoothPermissionsAndScan() {
        // Request Bluetooth permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            bluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)
        }
    }

    // bluetooth permission handling
    private val bluetoothPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Start BLE scanning
            Toast.makeText(this, "Bluetooth permission enabled", Toast.LENGTH_SHORT).show()
            startBleScan()
        } else {
            Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show()
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
    val foundDevices: MutableList<BluetoothDevice> = mutableListOf()

    private fun startBleScan() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION
            )
            return
        }

        val scanner = bluetoothAdapter.bluetoothLeScanner

        // startScan is part of Bluetooth LE API
        // scan results are delivered though callback
        scanner.startScan(scanCallback)
    }

    companion object {
        const val REQUEST_BLUETOOTH_CONNECT_PERMISSION = 1
        private const val REQUEST_PERMISSION = 101
    }

    override fun onDestroy() {
        super.onDestroy()
        val scanner = bluetoothAdapter.bluetoothLeScanner
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        scanner.stopScan(scanCallback)
    }

    private fun btScan() {
        val bluetoothManager: BluetoothManager =
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        // start bluetooth discovery
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        bluetoothAdapter?.startDiscovery()

        // Check Bluetooth adapter and permissions
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Bluetooth is not available or permission is not granted
            // Handle this condition appropriately (e.g., request permission)
            return
        }

        // Register BroadcastReceiver for device discovery
        val mReceiver = object : BroadcastReceiver() {
            @RequiresApi(Build.VERSION_CODES.S)
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                if (BluetoothDevice.ACTION_FOUND == action) {
                    val device =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        // Check if name is null (requires permission)
                        val deviceName = if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // Request the permission if not granted
                            ActivityCompat.requestPermissions(
                                context as Activity,
                                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                                REQUEST_BLUETOOTH_CONNECT_PERMISSION
                            )
                            // Return if permission not granted yet
                            return
                        } else {
                            // Permission already granted, continue
                            if (ActivityCompat.checkSelfPermission(
                                    this@PatientHomeView,
                                    Manifest.permission.BLUETOOTH_CONNECT
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return
                            }
                            if (device.name != null) device.name else "Unknown"
                        }
                        // Add the device to the list
                        foundDevices.add(device)
                        // Print the found device to the terminal
                        println("Found Device: $deviceName - ${device.address}")

                        // currentRoomTextView.text = deviceName
                        // add found device to the adapter
                        deviceListAdapter.add("${device.name ?: "Unknown"} - ${device.address}")
                        // notify the adapter that the data set has changed
                        deviceListAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}

// Create a custom adapter class that extends BaseAdapter
class DeviceListAdapter(private val context: Context, private val devices: ArrayList<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return devices.size
    }

    override fun getItem(position: Int): Any {
        return devices[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_patient_home_view, parent, false)

        // Set the text of the TextView in your list item layout
        val deviceNameTextView = view.findViewById<TextView>(R.id.deviceListView)
        deviceNameTextView.text = devices[position]

        return view
    }
}
