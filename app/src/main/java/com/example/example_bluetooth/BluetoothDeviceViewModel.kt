package com.example.example_bluetooth

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BluetoothDeviceViewModel : ViewModel() {
    private val _deviceConnected = MutableLiveData<MutableSet<BluetoothDevice>>()
    val devicesConnected: LiveData<MutableSet<BluetoothDevice>> get() = _deviceConnected

    private val _deviceUnConnected = MutableLiveData<MutableList<BluetoothDevice>>()
    val devicesUnConnected: LiveData<MutableList<BluetoothDevice>> get() = _deviceUnConnected

    fun addDeviceConnected(listDevice: MutableSet<BluetoothDevice>) {
        _deviceConnected.value = listDevice
    }

    fun addDeviceUnConnected(listDevice: MutableList<BluetoothDevice>) {
        _deviceUnConnected.value = listDevice
    }

}