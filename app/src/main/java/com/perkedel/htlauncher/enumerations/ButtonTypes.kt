package com.perkedel.htlauncher.enumerations

import androidx.annotation.IntegerRes

enum class ButtonTypes(@IntegerRes val buttonType:Int) {
    DefaultButton(buttonType = 0),
    OutlineButton(buttonType = 1),
    TextButton(buttonType = 2),
    IconButton(buttonType = 3),
}