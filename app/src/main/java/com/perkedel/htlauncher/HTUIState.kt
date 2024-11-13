package com.perkedel.htlauncher

import android.net.Uri

data class HTUIState(
    val currentPage: Int = 0,
    val openMoreMenu: Boolean = false,
    val selectedSaveDir: Uri? = null,
    val readAloudHoldClick: Boolean = false,
    val toastAloudHoldClick: Boolean = false,
    val dismissDialogClickOutside: Boolean = true,
    val testResult:String = "HAIHAHA",
//    val testFilePath:Uri,
) {
}