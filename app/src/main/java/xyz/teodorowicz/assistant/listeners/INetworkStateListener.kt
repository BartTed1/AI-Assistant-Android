package xyz.teodorowicz.assistant.listeners

interface INetworkStateListener{
    fun registerNetworkCallback()
    fun unregisterNetworkCallback()
}