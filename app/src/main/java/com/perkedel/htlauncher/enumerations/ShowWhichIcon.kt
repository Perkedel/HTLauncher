package com.perkedel.htlauncher.enumerations

enum class ShowWhichIcon(whichIs:Int = 0) {
    Default(whichIs = 0),
    Main(whichIs = 1), // Main package icon
    Tv(whichIs = 2), // TV Banner
    Glance(whichIs = 3), // Widget
    Custom(whichIs = 4),
}