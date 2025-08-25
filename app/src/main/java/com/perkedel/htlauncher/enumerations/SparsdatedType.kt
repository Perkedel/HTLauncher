package com.perkedel.htlauncher.enumerations

import androidx.annotation.IntegerRes

enum class SparsdatedType(@IntegerRes val sparsdatedType: Int) {
    GooglePlay(sparsdatedType = 0),
    GeneralSparsdated(sparsdatedType = 1), // a.k.a. Unknown
    FDroid(sparsdatedType = 2),
    GitHub(sparsdatedType = 3),
}