package com.sasaj.graphics.drawingapp.common

import android.net.ConnectivityManager
import com.sasaj.domain.usecases.NetworkManager

class NetworkManagerImpl(private val connectivityManager: ConnectivityManager) : NetworkManager{
    override fun isConnected(): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}