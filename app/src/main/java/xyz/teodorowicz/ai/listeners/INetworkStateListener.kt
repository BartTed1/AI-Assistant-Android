package xyz.teodorowicz.ai.listeners

interface INetworkStateListener{
    fun registerNetworkCallback()
    fun unregisterNetworkCallback()
}