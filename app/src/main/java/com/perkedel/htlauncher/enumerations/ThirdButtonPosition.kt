package com.perkedel.htlauncher.enumerations

import androidx.annotation.StringRes

enum class ThirdButtonPosition (@StringRes val title: Int){
    Left(title = 1),
    Middle(title = 0),
    Right(title = 2),
}