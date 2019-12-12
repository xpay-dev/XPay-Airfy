package com.xpayworld.payment.ui.preference

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xpayworld.payment.R
import com.xpayworld.payment.databinding.ListItemDeviceBinding
import android.graphics.Color


class DeviceAdapter : RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    private  lateinit var deviceList : List<BluetoothDevice>
    var onItemClick: ((BluetoothDevice) -> Unit)? = null
    var selectedPosition = -1

    override fun getItemCount(): Int {
        return if(::deviceList.isInitialized) deviceList.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(selectedPosition==position)
            holder.itemView.setBackgroundColor(Color.parseColor("#000000"))
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"))


        holder.bind(createOnClickListener(deviceList[position]),deviceList[position])

        holder.itemView.setOnClickListener {
            selectedPosition=position
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemDeviceBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item_device, parent, false)
        return ViewHolder(binding)

    }

    private fun createOnClickListener(item : BluetoothDevice) : View.OnClickListener{
        return View.OnClickListener {
        onItemClick?.invoke(item)
        }
    }

    fun updatePostList(deviceList:List<BluetoothDevice>){
        this.deviceList = deviceList
        notifyDataSetChanged()
    }
    class ViewHolder(private val binding :  ListItemDeviceBinding): RecyclerView.ViewHolder(binding.root){

        private  val  viewModel = DeviceViewModel()

        fun bind(listener: View.OnClickListener ,device: BluetoothDevice) {
            viewModel.bind(device)
            binding.clickListener = listener
            binding.viewModel = viewModel
        }
        init {
            itemView.setOnClickListener {

            }
        }
    }

}