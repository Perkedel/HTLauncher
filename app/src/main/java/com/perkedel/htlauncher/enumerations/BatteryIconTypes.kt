package com.perkedel.htlauncher.enumerations

import androidx.annotation.IntegerRes

enum class BatteryIconTypes(@IntegerRes val type:Int)
{
    ProgressBatteryIcon(type = 0),
    IconBatteryIcon(type = 1),
}