package com.perkedel.htlauncher

import android.content.ClipData.Item
import android.net.Uri
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.TestJsonData
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

data class HTUIState(
    val currentPage: Int = 0,
    val openMoreMenu: Boolean = false,
    val selectedSaveDir: Uri? = null,
    val readAloudHoldClick: Boolean = false,
    val toastAloudHoldClick: Boolean = false,
    val dismissDialogClickOutside: Boolean = true,
    val isReady: Boolean = false,
    val testResult:String = "HAIHAHA",
//    val testFilePath:Uri,
//    val testJsonElement:JsonElement = Json.parseToJsonElement(string = "{ \"test\" \n: \"HUAHAHU\" }"),
    val testJsonElement:TestJsonData = TestJsonData(),
    val testPreloadAll:Boolean = true,
    val coreConfig:Uri? = null,
    val coreConfigJson:HomepagesWeHave? = null,
    // https://www.baeldung.com/kotlin/maps
    // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/
    val folders:MutableMap<String,Uri> = LinkedHashMap<String,Uri>(),
    val pageList:MutableMap<String,PageData> = LinkedHashMap<String,PageData>(),
    val itemList:MutableMap<String,ItemData> = LinkedHashMap<String,ItemData>(),
) {
}