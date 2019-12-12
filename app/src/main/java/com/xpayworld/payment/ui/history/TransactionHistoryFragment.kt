package com.xpayworld.payment.ui.history


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.jakewharton.rxbinding2.widget.RxTextView
import com.xpayworld.payment.databinding.FragmentTransactionHistoryBinding
import com.xpayworld.payment.ui.base.kt.BaseFragment
import com.xpayworld.payment.util.InjectorUtil
import com.xpayworld.payment.util.transactionResponse
import kotlinx.android.synthetic.main.fragment_preference.recyclerView
import kotlinx.android.synthetic.main.fragment_transaction_history.*
import java.util.concurrent.TimeUnit

class TransactionHistoryFragment : BaseFragment() {

    private val viewModel: TransactionHistoryViewModel by viewModels {
        InjectorUtil.provideTransactionHistoryViewModel(requireContext())
    }

    private var adapter = TransactionHistoryAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTransactionHistoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this@TransactionHistoryFragment
        return binding.root
    }

    override fun initView(view: View, container: ViewGroup?) {
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        adapter.onItemClick = { transResponse ->
            viewModel.callTransactionAPI(transResponse)
        }

        val search =  RxTextView.textChanges(edSearch)
              .debounce(300 ,TimeUnit.MILLISECONDS)
              .map { charSequence ->
                  charSequence.toString()}

        viewModel.callTransLookUpAPI(search)

        viewModel.loadingVisibility.observe(this, Observer {
            if (it) showProgress() else hideProgress()
        })

        viewModel.transResponse.observe(this , Observer {
            recyclerView.visibility = View.VISIBLE
            adapter.updatePostList(it)
            tvStatus.visibility = View.GONE
        })

        viewModel.requestError.observe(this , Observer {
            recyclerView.visibility = View.INVISIBLE
            tvStatus.visibility = View.VISIBLE
            tvStatus.text = it as String
        })

        viewModel.navigateToReceipt.observe(this , Observer {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val txnsStr = gson.toJson(it.first[0])
            val respStr  = gson.toJson(it.second)
            val directions = TransactionHistoryFragmentDirections.actionHistoryFragmentToReceiptFragment(txnsStr,respStr)
            findNavController().navigate(directions)
        })

        viewModel.networkError.observe(this, Observer {
            showNetworkError(title = it)
        })

        viewModel.showError.observe(this , Observer { message ->
            showError(title = message.first,message = message.second)
        })

    }

}
