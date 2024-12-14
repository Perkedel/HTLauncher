package com.perkedel.htlauncher.func

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun networkType(context: Context):NetworkType{
    // https://stackoverflow.com/a/70510760/9079640
    // https://stackoverflow.com/a/53532456/9079640
    // https://developer.android.com/reference/android/net/NetworkInfo
    // https://www.geeksforgeeks.org/android-jetpack-compose-display-current-internet-connection-type/
    var result:NetworkType = NetworkType.None
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return NetworkType.None
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return NetworkType.None
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.Wifi
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.Mobile
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.Ethernet
            else -> NetworkType.None
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> NetworkType.Wifi
                    ConnectivityManager.TYPE_MOBILE -> NetworkType.Mobile
                    ConnectivityManager.TYPE_ETHERNET -> NetworkType.Ethernet
                    else -> NetworkType.None
                }

            }
        }
    }

    return result
}

enum class NetworkType(
    val type:String = "",
){
    Wifi(type = "Wifi"),
    Mobile(type = "Mobile"),
    Ethernet(type = "Ethernet"),
    None(type = "None"),
}