package com.xpayworld.payment.ui.transaction.processTransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.xpayworld.payment.ui.dashboard.DrawerLocker

class PinPadFragment : BaseDeviceFragment() {

    private var wisePOSPlusPinPadView: WisePOSPlusPinPadView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        wisePOSPlusPinPadView = WisePOSPlusPinPadView(context!!, pinButtonLayout)
        return wisePOSPlusPinPadView as WisePOSPlusPinPadView
    }


    override fun initView(view: View, container: ViewGroup?) {
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as DrawerLocker).drawerEnabled(true)
    }

    internal fun setStars(inputStars: String) {
        WisePOSPlusPinPadView.setStars(wisePOSPlusPinPadView!!, inputStars)
        wisePOSPlusPinPadView!!.invalidate()
    }

}