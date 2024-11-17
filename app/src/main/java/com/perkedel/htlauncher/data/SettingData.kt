package com.perkedel.htlauncher.data

data class SettingData(
    val fullscreen:Boolean = false,
    val colorMode:Int = 0,
    val darkMode:Int = 0,
    /*
    * Color:
    * 0 = Default. Dynamic Material You
    * 1 = Force This app theme
    *
    * Dark mode:
    * 0 = Auto
    * 1 = Force On
    * 2 = Force Off
    * */

)