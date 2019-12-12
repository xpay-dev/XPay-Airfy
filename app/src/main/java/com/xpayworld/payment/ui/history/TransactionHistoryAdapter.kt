package com.xpayworld.payment.ui.history

import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.xpayworld.payment.R
import com.xpayworld.payment.databinding.ListItemHistoryBinding
import com.xpayworld.payment.network.TransactionResponse
import com.xpayworld.payment.network.transLookUp.TransResponse

class TransactionHistoryAdapter : RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>() {

    private lateinit var txnResponse: List<TransactionResponse>
    var onItemClick: ((TransactionResponse) -> Unit)? = null
    override fun getItemCount(): Int {
        return if (::txnResponse.isInitialized) txnResponse.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(createOnClickListener(txnResponse[position]),txnResponse[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item_history, parent, false))
    }

    fun updatePostList(txnList: List<TransactionResponse>) {
        this.txnResponse = txnList
        notifyDataSetChanged()
    }


    private fun createOnClickListener(item : TransactionResponse) : View.OnClickListener{
        return View.OnClickListener {
            onItemClick?.invoke(item)
        }
    }

    class ViewHolder(private val binding: ListItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        private val mViewModel = HistoryViewModel()

        fun bind(listener: View.OnClickListener ,txn: TransactionResponse) {
                mViewModel.bind(txn)
                binding.clickListener = listener
                binding.viewModel = mViewModel
        }
    }
}