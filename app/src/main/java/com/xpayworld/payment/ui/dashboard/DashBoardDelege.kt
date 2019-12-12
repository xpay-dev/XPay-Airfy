package com.xpayworld.payment.ui.dashboard

interface ToolbarDelegate {
    fun showToolbar(visible: Boolean)
    fun setTitle(title : String)
}
interface  IOnFocusListenable{
    fun onWindowFocusChanfed(hasFocus : Boolean)
}
interface DrawerLocker {
    fun drawerEnabled(enabled: Boolean)
}
interface  UserInteraction {
    fun userInteractionListener()
}
