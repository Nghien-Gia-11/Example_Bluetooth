package com.example.example_bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.example_bluetooth.databinding.LayoutItemBluetoothDeviceBinding

class BluetoothDeviceUnConnectedAdapter(private var listDevice: MutableList<BluetoothDevice>) : RecyclerView.Adapter<BluetoothDeviceUnConnectedAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutItemBluetoothDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = LayoutItemBluetoothDeviceBinding.inflate(view, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listDevice.size

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            holder.binding.deviceName.text = listDevice[position].name
            holder.binding.deviceAddress.text = listDevice[position].address
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDevice(listItem: List<BluetoothDevice>){
        listDevice.clear()
        listDevice.addAll(listItem)
        notifyDataSetChanged()
    }


}