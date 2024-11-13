package com.perkedel.htlauncher

import android.net.Uri

data class HTUIState(
    val currentPage: Int = 0,
    val openMoreMenu: Boolean = false,
    val selectedSaveDir: Uri? = null,
) {
}