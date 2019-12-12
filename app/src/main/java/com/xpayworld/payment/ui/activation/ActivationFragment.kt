package com.xpayworld.payment.ui.activation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import com.xpayworld.payment.R
import com.xpayworld.payment.ui.base.kt.BaseFragment
import com.xpayworld.payment.ui.dashboard.DrawerLocker
import kotlinx.android.synthetic.main.fragment_activation_code.*
import androidx.fragment.app.viewModels
import com.xpayworld.payment.util.InjectorUtil
import androidx.navigation.fragment.findNavController
import com.xpayworld.payment.databinding.FragmentActivationCodeBinding


class ActivationFragment : BaseFragment() {

    var edtextList = listOf<EditText>()
    var strCode = ""

    private val viewModel: ActivationViewModel by viewModels {
        InjectorUtil.provideActivationViewModelFactor(requireContext())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentActivationCodeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun initView(view: View, container: ViewGroup?) {

        edtextList = listOf(edtext1, edtext2, edtext3, edtext4)
        edtextList.forEach { it.addTextChangedListener(OnChangedEditText()) }

//        val str =  "C00AEEEE9876543210E0033FC10AFFFF9876543210E00150C408489504FFFFFF7709C2820178A6272C73ACF368578FC0570988248C57DBC0E5520170629262299BC4A47366C2049D6799D4CD93F015E6479FA2BACCEC24F543371715B59C2A95F5391285393BBF2A1B3508285FBC90A0ADFA4F2C36E13FD4EED547399021DA677C97A9FE0587977473A8923630EF5DF38A1AAAF0BBD8C12332EA0232050DCB1624FDC9CDD0F37A0C5EA38F3027EF06158228250207BDE46FBDDE3CF57CF9F2A9131936E8A05A80424878D7F561088979B099F28A1F6025C2CDB602D278111C30818B931671E499BDD66C6E483B25549328EFB863C65D73623D0E308EBE167F316E4613B6F5FE13CD4447B000754C5CB4CF389E77C249EEDF33A79A5EF80A783655B203E5E99C14AD1C011690F281EBE3F195F979BD65657F21A79978848FE331BA3FC6C2CF763F8FF41339AE0BEDB0728C77DD02A7182E3C33B5FD8BB21CE8E12D371BE5A569190EE158C80375CB54239E962C6EE2E9C87D82EEE6AB365DE94A4F896F79D60EEE8665C02E1D25CBF92C3BB80A4DEF2CD47512A86436BC98C70ADDDD9876543210E00309C818D6DAE315DF5A8961B1A46CBD429CA9950267B051889428384F07A000000003101082021C009505808004800099083A1F2013678D1A579A031911209B0268009C01005F24032408315F2A0206049F02060000000012229F03060000000000009F0607A00000000310109F100706010A03A088009F1A0208409F1C0831313232333334349F26084816F438A6F5566A9F2701809F330360F8C89F34034203009F360203259F3704D5D1FAB2DF83620100DF080ACCCC9876543210E0033DCD086A622A9908986E71"
//        val decode = BBDeviceController.decodeTlv(str)

//        decode.elements()
//
//        val dd = HashMap<String,String>()
//
//        dd.putAll(decode)
//    val emv =   EMVCard(dd)
//
//        val emvData = EMVCardData(
//                cardholderName = emv.cardholderName,
//                cardNumber = emv.cardNumber,
//                ksn = emv.ksn,
//                emvICCData = emv.emvICCData ,
//                expiryDate =  emv.expiryDate,
//                expiryMonth =  emv.expiryMonth,
//                encTrack1 =  emv.encTrack1,
//                encTrack2 = emv.encTrack2,
//                posEntryMode = emv.posEntryMode,
//                encTrack3 = emv.encTrack3,
//                appReferredName = emv.appReferredName,
//                epb = emv.epb,
//                epbksn = emv.epbksn,
//                cardXNumber = emv.cardXNumber,
//                expiryYear = emv.expiryYear,
//                trackEncoding = emv.trackEncoding,
//                maskedPan = emv.maskedPan,
//                serialNumber = emv.serialNumber,
//                serviceCode = emv.serviceCode,
//                appId = emv.appId)
//
//
//
//        val db = AppDatabase.getDatabase(context = activity!!.applicationContext)
//
////        GlobalScope.launch {
////            val transRepository = Transaction(
////                    amount = 1002.00,
////                    cardData = "asdasdddd",
////                    currency = "PHPsssdddddd",
////                    transNumber = "sdas",
////                    transDate = "ddd",
////                    merchantName = "dasda",
////                    posEntry = 1,
////                    emvCard = emv
////            )
////
////            val tx = Txn()
////       //     tx.emvCard = emvData
////            tx.isFallback = true
////           db?.transactionDao()?.insertTransaction(transRepository)
////            //  db?.transactionDao()?.insertTxn(tx)
////
////            //println(transRepository.emvCard?.epb)
////            val data = InjectorUtil.getTransactionRepository(requireContext()).getTransaction()
////
////            data?.forEach {
////                Log.e("id", it.id.toString())
////               // Log.e("emvICC", it.emvCard.emvICCData)
////            }
////        }



        btnActivate.setOnClickListener{
             viewModel.callActivationAPI(strCode)
        }

        viewModel.loadingVisibility.observe(this, Observer{
            isShow -> if (isShow == true) showProgress() else hideProgress()
        })
        viewModel.toolbarVisibility.observe(this, Observer{(activity as DrawerLocker).drawerEnabled(it)})

        viewModel.requestError.observe(this, Observer {
           if (it is Boolean) {
               edtextList.forEach { editText -> editText.setBackgroundResource(R.drawable.tv_error)}
               errorGroup.visibility = View.VISIBLE
           }
           else{
               errorGroup.visibility = View.GONE
               edtextList.forEach { editText -> editText.setBackgroundResource(R.drawable.tv_activation)}
           }
        })
        viewModel.networkError.observe(this, Observer {
            showNetworkError()
        })

        viewModel.navigateToEnterPin.observe(this , Observer {
            val direction = ActivationFragmentDirections.actionActiviationFragmentToEnterPinFragment()
            findNavController().navigate(direction)
        })


    }

    internal inner class OnChangedEditText : TextWatcher {
        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, count: Int) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            strCode = ""
            if (edtext1.isFocused && edtext1.text.length == 4) {
                edtext2.requestFocus()
            }
            if (edtext2.isFocused && edtext2.text.length == 4) {
                edtext3.requestFocus()
            }
            if (edtext3.isFocused && edtext3.text.length == 4) {
                edtext4.requestFocus()
            }
            edtextList.forEach { action: EditText ->
                strCode += action.text.toString()
            }

            btnActivate.isEnabled = strCode.length == 16
        }
    }

}




