package com.nytimesmostviewedarticles.network

import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "NetworkConnectionCallback"

interface NetworkConnectionReceiver {
    val callback: ConnectivityManager.NetworkCallback
    val networkStatusFlow: StateFlow<NetworkStatus>
}

@Singleton
class NetworkConnectionReceiverImpl @Inject constructor(): NetworkConnectionReceiver {
    override val callback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            Log.d(TAG, "onAvailable()")
            _networkStatusFlow.value = NetworkStatus.CONNECTED
        }

        override fun onUnavailable() {
            Log.d(TAG, "onUnavailable()")
            _networkStatusFlow.value = NetworkStatus.DISCONNECTED
        }

        override fun onLost(network: Network) {
            Log.d(TAG, "onLost()")
            _networkStatusFlow.value = NetworkStatus.DISCONNECTED
        }
    }

    private val _networkStatusFlow = MutableStateFlow(NetworkStatus.UNINITIALIZED)
    override val networkStatusFlow: StateFlow<NetworkStatus> = _networkStatusFlow
}