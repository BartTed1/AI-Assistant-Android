package xyz.teodorowicz.assistant.listeners

import android.content.Context
import android.net.ConnectivityManager

interface INetworkStateListener{
    fun registerNetworkCallback()
    fun unregisterNetworkCallback()
}