package com.shihs.tripmood.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.shihs.tripmood.TripMoodApplication

object Util {

    @SuppressLint("MissingPermission")
    fun isInternetConnected(): Boolean {
        val cm = TripMoodApplication.instance
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun getString(resourceId: Int): String {
        return TripMoodApplication.instance.getString(resourceId)
    }

    fun getColor(resourceId: Int): Int {
        return TripMoodApplication.instance.getColor(resourceId)
    }
}
