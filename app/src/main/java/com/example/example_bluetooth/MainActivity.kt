package com.example.example_bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.example_bluetooth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothManager: BluetoothManager
    private var enableBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Bluetooth is enable", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Bluetooth is not enable", Toast.LENGTH_SHORT).show()
            }
        }
    private var resultBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                isBluetoothEnable()
            } else {
                Toast.makeText(this, "Người dùng từ chối quyền", Toast.LENGTH_SHORT).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        resultBluetoothLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)

        isBluetoothEnable()

        enableBluetooth()
    }

    private fun enableBluetooth() {
        binding.btnConnect.setOnClickListener {
            if (!bluetoothAdapter.isEnabled) {
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enableBluetoothLauncher.launch(enableIntent)
            }
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                resultBluetoothLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                isBluetoothEnable()
            }
        } else {
            isBluetoothEnable()
        }
    }

    private fun isBluetoothEnable() {
        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter.isEnabled) {
            Toast.makeText(this, "Bluetooth is enable", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Bluetooth is not enable", Toast.LENGTH_SHORT).show()
        }
    }
}