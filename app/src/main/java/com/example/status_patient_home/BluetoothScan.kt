package com.example.status_patient_home

import BeaconScanPermissionsActivity.Companion.TAG
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class BluetoothScan(private val context: Context) {

    private var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
    private var scanning = false
    private val handler = Handler()

    // Define your ScanCallback
    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val deviceName = if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            } else {

            }

            device.name
            val deviceAddress = device.address
            val rssi = result.rssi

            // Process the scan result, such as displaying device information
            Log.d(TAG, "Found BLE device: $deviceName ($deviceAddress), RSSI: $rssi")
        }

        override fun onScanFailed(errorCode: Int) {
            // Handle scan failure here
            Log.e(TAG, "BLE scan failed with error code: $errorCode")
        }
    }

    // Stops scanning after 10 seconds.
    private val scanPeriod: Long = 10000

    @RequiresApi(Build.VERSION_CODES.S)
    fun scanLeDevice() {
        if (!scanning) {
            handler.postDelayed({
                scanning = false
                if (checkBluetoothPermission()) {
                    bluetoothLeScanner?.stopScan(leScanCallback)
                }
            }, scanPeriod)
            scanning = true
            if (checkBluetoothPermission()) {
                bluetoothLeScanner?.startScan(leScanCallback)
            }
        } else {
            scanning = false
            bluetoothLeScanner?.stopScan(leScanCallback)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkBluetoothPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    }
}
