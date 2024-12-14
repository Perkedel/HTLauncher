package com.perkedel.htlauncher.func

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.CellInfo
import android.telephony.CellInfoCdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoNr
import android.telephony.CellInfoTdscdma
import android.telephony.CellInfoWcdma
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.CellInfoCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.util.concurrent.Executor
import kotlin.concurrent.thread

fun signalStrength(
    context: Context
): SignalStrength {
    // https://wiki.teltonika-networks.com/view/Mobile_Signal_Strength_Recommendations
    // https://stackoverflow.com/questions/19805880/get-signal-strength-in-android
    // https://www.youtube.com/watch?v=hojwvo99584
    //https://groups.google.com/g/android-platform/c/Xutq8NU417M?pli=1
    // https://developer.android.com/reference/android/telephony/CellSignalStrength#equals(java.lang.Object)
    // https://developer.android.com/reference/android/telephony/CellInfo
    // https://www.youtube.com/watch?v=hojwvo99584

    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)

    var signalResult: Int = 0
    var signalDbm: Int = 0
    var signalAsu: Int = 0
    var signalBest: Int = 0
    var signalWorst:Int = -100
    var signalType: SignalType = SignalType.NONE
    val signalExecutor:java.util.concurrent.Executor = Executor {
        fun execute(runnable: Runnable){
            Thread(runnable).start()
        }
    }
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//        signalResult = telephonyManager.signalStrength?.level ?: 0 // Poor 0..4 Good
////        signalDbm = telephonyManager.signalStrength?: 0
//    } else{
    try {

    } catch (e:Exception){
        e.printStackTrace()
    }
    telephonyManager.run {
//            telephonyManager.listen(mPhoneStatelistener,PhoneStateListener.LISTEN_DISPLAY_INFO_CHANGED)
        var listOfCellInfo: MutableList<CellInfo> = emptyList<CellInfo>().toMutableList()
        try {
            listOfCellInfo = telephonyManager.allCellInfo
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                // no compatible exact solution, gave up
                // https://stackoverflow.com/a/63370975/9079640
                telephonyManager.requestCellInfoUpdate(signalExecutor,SignalStrengthCallback(listOfCellInfo))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (listOfCellInfo.isNotEmpty()) {
            for (cellInfo in listOfCellInfo) {
                when {
                    cellInfo is CellInfoGsm -> {
                        signalResult = cellInfo.cellSignalStrength.level
                        signalDbm = cellInfo.cellSignalStrength.dbm
                        signalAsu = cellInfo.cellSignalStrength.asuLevel
                        signalType = SignalType.GPRS
                        signalBest =  -70
                        signalWorst = -100
                    }

                    cellInfo is CellInfoCdma -> {
                        signalResult = cellInfo.cellSignalStrength.level
                        signalDbm = cellInfo.cellSignalStrength.dbm
                        signalAsu = cellInfo.cellSignalStrength.asuLevel
                        signalType = SignalType.CDMA
                        signalBest =  -70
                        signalWorst = -100
                    }

                    cellInfo is CellInfoWcdma -> {
                        signalResult = cellInfo.cellSignalStrength.level
                        signalDbm = cellInfo.cellSignalStrength.dbm
                        signalAsu = cellInfo.cellSignalStrength.asuLevel
                        signalType = SignalType.WCDMA
                        signalBest =   -70
                        signalWorst = -100
                    }

                    cellInfo is CellInfoLte -> {
                        signalResult = cellInfo.cellSignalStrength.level
                        signalDbm = cellInfo.cellSignalStrength.dbm
                        signalAsu = cellInfo.cellSignalStrength.asuLevel
                        signalType = SignalType.LTE
                        signalBest =   -80
                        signalWorst = -100
                    }

                    else -> {

                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    when {
                        cellInfo is CellInfoTdscdma -> {
                            signalResult = cellInfo.cellSignalStrength.level
                            signalDbm = cellInfo.cellSignalStrength.dbm
                            signalAsu = cellInfo.cellSignalStrength.asuLevel
                            signalType = SignalType.TD_SCDMA
                            signalBest =   -70
                            signalWorst = -100
                        }

                        cellInfo is CellInfoNr -> { // 5G
                            signalResult = cellInfo.cellSignalStrength.level
                            signalDbm = cellInfo.cellSignalStrength.dbm
                            signalAsu = cellInfo.cellSignalStrength.asuLevel
                            signalType = SignalType.NR
                            signalBest =   -80
                            signalWorst = -100
                        }

                        else -> {

                        }
                    }
                }
            }
        }
    }
    // get signal strength before Pie

//    }

    return SignalStrength(
        signalLevel = signalResult,
        signalDbm = signalDbm,
        signalAsu = signalAsu,
        signalType = signalType,
        signalBest = signalBest,
        signalWorst = signalWorst,
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
class SignalStrengthCallback(
    // https://www.tutorialspoint.com/how-to-create-an-instance-of-an-abstract-class-in-kotlin#:~:text=In%20Kotlin%2C%20we%20cannot%20create,and%20inherit%20the%20abstract%20class.
    // https://kotlinlang.org/docs/inline-classes.html
//    telephonyManager: TelephonyManager,
    private var newCellInfo: MutableList<CellInfo>,
) : TelephonyManager.CellInfoCallback() {
    override fun onCellInfo(cellInfo: MutableList<CellInfo>) {
//        TODO("Not yet implemented")
        newCellInfo = cellInfo
    }
}

data class SignalStrength(
    val signalLevel:Int = 0,
    val signalDbm:Int = 0,
    val signalAsu:Int = 0,
    val signalType: SignalType = SignalType.NONE,
    val signalBest:Int = 0,
    val signalWorst:Int = -100,
)

enum class SignalType(val value:Int = 0, val sayFull:String = "", val sayShort:String = ""){
    NONE(0, "None", "?"),
    GPRS(1, "General Packet Radio Service", "G"),
    EDGE(2, "Enhanced Data Rates for GSM Evolution", "E"),
    UMTS(3, "Universal Mobile Telecommunications System", "U"),
    CDMA(4, "Code Division Multiple Access", "C"),
    EVDO_0(5, "Evolution Data Optimized", "E0"),
    EVDO_A(6, "Evolution Data Optimized Revision A", "Ea"),
    RTT(7, "Radio Transmission Technology", "R"),
    HSDPA(8, "High-Speed Downlink Packet Access", "H"),
    HSUPA(9, "High-Speed Uplink Packet Access", "H"),
    HSPA(10, "High-Speed Packet Access", "H"),
    IDEN(11, "Integrated Digital Enhanced Network", "I"),
    EVDO_B(12, "Evolution Data Optimized Revision B", "Eb"),
    LTE(13, "Long Term Evolution", "LTE"),
    NR(14, "New Radio", "5G"), // 5G
    WCDMA(15, "Wideband Code Division Multiple Access", "W"),
    TD_SCDMA(16, "Time Division-Synchronous Code Division Multiple Access", "T"),
}

//internal class MyPhoneStateListener : PhoneStateListener() {
//    override fun onSignalStrengthsChanged(signalStrength: android.telephony.SignalStrength?) {
//        // https://stackoverflow.com/questions/19805880/get-signal-strength-in-android
//        super.onSignalStrengthsChanged(signalStrength)
//        if (signalStrength != null) {
//            mSignalStrength = signalStrength.getGsmSignalStrength()
//        }
//        mSignalStrength = (2 * mSignalStrength) - 113 // -> dBm
//    }
//}