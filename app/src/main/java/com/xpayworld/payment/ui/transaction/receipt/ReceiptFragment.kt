package com.xpayworld.payment.ui.transaction.receipt


import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.xpayworld.payment.R
import com.xpayworld.payment.ui.base.kt.BaseFragment
import kotlinx.android.synthetic.main.fragment_receipt.*
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.xpayworld.payment.databinding.FragmentReceiptBinding
import com.xpayworld.payment.network.PosWsResponse
import com.xpayworld.payment.network.TransactionResponse
import com.xpayworld.payment.ui.dashboard.DashboardActivity
import com.xpayworld.payment.ui.dashboard.DrawerLocker
import com.xpayworld.payment.ui.transaction.processTransaction.BaseDeviceFragment
import com.xpayworld.payment.ui.transaction.receipt.ReceiptFragmentArgs.fromBundle
import com.xpayworld.payment.util.InjectorUtil
import com.xpayworld.payment.util.merchantDetails

class ReceiptFragment: BaseDeviceFragment()  {

    private val viewModel: ReceiptViewModel by viewModels {
        InjectorUtil.provideReceiptViewModelFactory(requireContext())
    }

    val transaction by lazy {
        fromBundle(arguments!!).transaction
    }

    val mResponse : String by lazy {
        fromBundle(arguments!!).status
    }

    var txn : TransactionResponse ? = null
    var response : PosWsResponse? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentReceiptBinding.inflate(inflater, container, false)
        val gson = Gson()
         txn =  gson.fromJson(transaction, TransactionResponse::class.java)

        if (mResponse != ""){
            response = gson.fromJson(mResponse,PosWsResponse::class.java)
            binding.response = response!!
        }

        binding.txns = txn
        binding.lifecycleOwner = this@ReceiptFragment
        return binding.root
    }

    override fun initView(view: View , container: ViewGroup?) {

        lblMerchantName.text = merchantDetails?.merchantName
        val address = merchantDetails?.address+ ","+ merchantDetails?.city +","+ merchantDetails?.country
        lblMerchantAddress.text = address

        setHasOptionsMenu(true)
        val receipt = viewModel.getReceiptSerial(requireContext(),txn =  txn!! , posStatus =  response!!)
        onReceiptData.value = receipt
        btnDone.setOnClickListener {

            bbDeviceController?.startPrint(1,60)

            clReceipt.startAnimation(AnimationUtils.loadAnimation(context!!, R.anim.receipt_out))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater = activity?.menuInflater
        inflater?.inflate(R.menu.menu_receipt, menu)
        super.onCreateOptionsMenu(menu, inflater!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.receipt_item_done -> {
                val direction = ReceiptFragmentDirections.actionReceiptFragmentToTransactionFragment("")
                 view!!.findNavController().navigate(direction)
                (activity as DrawerLocker).drawerEnabled(true)
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
         (activity as DrawerLocker).drawerEnabled(false)
    }
}
