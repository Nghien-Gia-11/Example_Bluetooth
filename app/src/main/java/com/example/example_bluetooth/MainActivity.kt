package com.example.example_bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.example_bluetooth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val viewModel: BluetoothDeviceViewModel by viewModels()
    private var listDevice = mutableListOf<BluetoothDevice>()

    private val enableBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                scanDevices()
            } else {
                Toast.makeText(this, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                enableBluetooth()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.btnConnect.setOnClickListener {
            checkPermissionsAndEnableBluetooth()
        }
        viewModel.devices.observe(this) { devices ->
            (binding.rvDevice.adapter as BluetoothDeviceAdapter).updateDevice(devices)
            Log.e("Device", devices.size.toString())
        }
    }

    private fun setupRecyclerView() {
        binding.rvDevice.apply {
            adapter = BluetoothDeviceAdapter(mutableListOf())
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                if (BluetoothDevice.ACTION_FOUND == intent?.action) {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        val deviceAddress = it.address
                        val isDeviceAlreadyAdded = listDevice.any { device -> device.address == deviceAddress }

                        if (!isDeviceAlreadyAdded) {
                            listDevice.add(it)
                            viewModel.addDevice(listDevice)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("BroadcastReceiver", "Error receiving broadcast", e)
            }

        }
    }

    @SuppressLint("MissingPermission")
    private fun scanDevices() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }
        bluetoothAdapter.startDiscovery()
    }

    private fun checkPermissionsAndEnableBluetooth() {
        requestPermissionsLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val permissions = arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
            )
            if (permissions.any {
                    ContextCompat.checkSelfPermission(
                        this,
                        it
                    ) != PackageManager.PERMISSION_GRANTED
                }) {
                requestPermissionsLauncher.launch(permissions)
            } else {
                enableBluetooth()
            }
        } else {
            enableBluetooth()
        }
    }

    private fun enableBluetooth() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter.isEnabled) {
            scanDevices()
        } else {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBluetoothLauncher.launch(enableIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
