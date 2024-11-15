package com.perkedel.htlauncher

import android.net.Uri
import com.perkedel.htlauncher.data.HomepagesWeHave
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

data class HTUIState(
    val currentPage: Int = 0,
    val openMoreMenu: Boolean = false,
    val selectedSaveDir: Uri? = null,
    val readAloudHoldClick: Boolean = false,
    val toastAloudHoldClick: Boolean = false,
    val dismissDialogClickOutside: Boolean = true,
    val testResult:String = "HAIHAHA",
//    val testFilePath:Uri,
    val testJsonElement:JsonElement = Json.parseToJsonElement(string = "{ \"test\" \n: \"HUAHAHU\" }"),
    val coreConfig:Uri? = null,
    val coreConfigJson:HomepagesWeHave? = null,
) {
}