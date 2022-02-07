package com.nytimesmostviewedarticles.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "NetworkConnectionService"

interface NetworkConnectionService {
    val networkStatusFlow: StateFlow<NetworkStatus>

    /**
     * Register callback in order to begin receiving updates for network connection.
     */
    fun registerCallback()

    /**
     * Unregister callback when network updates are no longer needed.
     */
    fun unRegisterCallback()
}

@Singleton
class NetworkConnectionServiceImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val networkReceiver: NetworkConnectionReceiver
): NetworkConnectionService {

    override val networkStatusFlow: StateFlow<NetworkStatus> = networkReceiver.networkStatusFlow


    override fun registerCallback() {
        Log.d(TAG, "registerCallback()")
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkReceiver.callback)
    }

    override fun unRegisterCallback() {
        Log.d(TAG, "unregisterCallback()")
        connectivityManager.unregisterNetworkCallback(networkReceiver.callback)
    }
}

enum class NetworkStatus {
    DISCONNECTED, CONNECTED, UNINITIALIZED
}