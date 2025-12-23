package com.example.budgetly.utils.internet_controller
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InternetControllerImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager
) : InternetController {
    override fun observe(): Flow<InternetController.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(InternetController.Status.Available) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(InternetController.Status.Losing) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(InternetController.Status.Lost) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(InternetController.Status.Unavailable) }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }

    //check stability
    override val isInternetStable: Boolean
        get() {
            try {
                val isSable =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                return isSable ?: true
            } catch (_: Exception) {
            }
            return false
        }


    //Use this in your usecase
    //check conectivity and also to provide internet
    override val isInternetAvailable: Boolean
        get() {
            return isInternetStable && isInternetConnected
        }

//    fun isWifiActive(): Boolean {
//        val ni = connectivityManager.activeNetworkInfo
//        return ni != null && ni.type == ConnectivityManager.TYPE_WIFI
//    }

    //check conectivity
    override val isInternetConnected: Boolean
        get() {
            try {
                val network = connectivityManager.activeNetwork
                if (network != null) {
                    val nc = connectivityManager.getNetworkCapabilities(network)
                    if (nc != null && (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                    ) {
                        return true
                    }
                }
            } catch (_: Exception) {
            }
            return false
        }
}