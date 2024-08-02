package com.example.example_bluetooth

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BluetoothDeviceViewModel : ViewModel() {
    private val _device = MutableLiveData<MutableList<BluetoothDevice>>()
    val device : LiveData<MutableList<BluetoothDevice>> get() = _device

    fun setDevice(device : BluetoothDevice){
        _device.value?.add(device)
    }

}