package com.perkedel.htlauncher.enumerations

import androidx.annotation.IntegerRes

enum class BatteryStrengthTypes(@IntegerRes val strength:Int) {
    UnknownBatteryStrength(strength = -1),
    DeadBatteryStrength(strength = 0),
    CriticalBatteryStrength(strength = 1),
    LowBatteryStrength(strength = 2),
    MediumBatteryStrength(strength = 3),
    HalfBatteryStrength(strength = 4),
    AlmostBatteryStrength(strength = 5),
    HighBatteryStrength(strength = 6),
    FullBatteryStrength(strength = 7),
}