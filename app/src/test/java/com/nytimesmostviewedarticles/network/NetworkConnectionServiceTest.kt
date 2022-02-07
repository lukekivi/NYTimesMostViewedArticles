package com.nytimesmostviewedarticles.network

import android.net.ConnectivityManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Test

class NetworkConnectionServiceTest {
    private lateinit var networkConnectionServiceImpl: NetworkConnectionServiceImpl
    private lateinit var mockConnectivityManager: ConnectivityManager
    private lateinit var mockNetworkConnectionReceiver: NetworkConnectionReceiver

    private val fakeNetworkStatusFlow = MutableStateFlow(NetworkStatus.UNINITIALIZED)

    @Before
    fun setup() {
        mockConnectivityManager = mockk()
        mockNetworkConnectionReceiver = mockk()

        every { mockNetworkConnectionReceiver.networkStatusFlow }.returns(fakeNetworkStatusFlow)

        networkConnectionServiceImpl = NetworkConnectionServiceImpl(mockConnectivityManager, mockNetworkConnectionReceiver)
    }

    @Test
    fun `test`() {
    }
}