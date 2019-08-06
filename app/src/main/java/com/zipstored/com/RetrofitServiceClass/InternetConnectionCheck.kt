package com.zipstored.com

import android.content.Context
import android.net.ConnectivityManager

object InternetConnectionCheck {

    fun isInternetAvailable(context: Context): Boolean {
        val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return conMgr.activeNetworkInfo != null && conMgr.activeNetworkInfo.isAvailable &&
                conMgr.activeNetworkInfo.isConnected

    }
}
