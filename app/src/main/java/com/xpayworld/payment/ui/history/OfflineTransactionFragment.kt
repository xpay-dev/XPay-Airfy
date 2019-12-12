package com.xpayworld.payment.ui.history

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.xpayworld.payment.R
import com.xpayworld.payment.databinding.FragmentTransactionHistoryBinding
import com.xpayworld.payment.network.TransactionResponse
import com.xpayworld.payment.ui.base.kt.BaseFragment

import com.xpayworld.payment.util.InjectorUtil
import kotlinx.android.synthetic.main.fragment_transaction_history.*


class OfflineTransactionFragment : BaseFragment(){

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
        binding.lifecycleOwner = this@OfflineTransactionFragment
        return binding.root
    }


    override fun initView(view: View, container: ViewGroup?) {
        setHasOptionsMenu(true)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        viewModel.callOfflineTransaction()

        viewModel.transResponse.observe(this , Observer {
            recyclerView.visibility = View.VISIBLE
            adapter.updatePostList(it)
            tvStatus.visibility = View.GONE
        })

        viewModel.loadingVisibility.observe(this, Observer {
            if (it) showProgress() else hideProgress()
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater = activity?.menuInflater
        inflater?.inflate(R.menu.menu_offline, menu)
        super.onCreateOptionsMenu(menu, inflater!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.offline_upload -> {
                viewModel.callforBatchUpload()
            }
        }
        return false
    }
}