package com.perkedel.htlauncher

import android.content.ClipData.Item
import android.content.pm.PackageInfo
import android.net.Uri
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.SearchableApps
import com.perkedel.htlauncher.data.TestJsonData
import com.perkedel.htlauncher.enumerations.EditWhich
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement

data class HTUIState(
    val inited: Boolean = false,
    val currentPage: Int = 0,
    val openMoreMenu: Boolean = false,
    val openGoToPage: Boolean = false,
    val openChangeSaveDir: Boolean = false,
    val openChangeSaveDirConfirm: Boolean = false,
    val openPermissionRequest: Boolean = false,
    val openCreateNewFile: Boolean = false,
    val selectedSaveDir: Uri? = null,
    val readAloudHoldClick: Boolean = false,
    val toastAloudHoldClick: Boolean = false,
    val dismissDialogClickOutside: Boolean = true,
    val isReady: Boolean = false,
    val testResult:String = "HAIHAHA",
    val toEditWhatFile:EditWhich = EditWhich.Items,
//    val testFilePath:Uri,
//    val testJsonElement:JsonElement = Json.parseToJsonElement(string = "{ \"test\" \n: \"HUAHAHU\" }"),
    val testJsonElement:TestJsonData = TestJsonData(),
    val testPreloadAll:Boolean = true,
    val editingLevel:Boolean = false,
    var coreConfig:Uri? = null,
    var coreConfigJson:HomepagesWeHave? = null,
    // https://www.baeldung.com/kotlin/maps
    // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/
    val folders:MutableMap<String,Uri> = LinkedHashMap<String,Uri>(),
    val currentPageIconModel:Any? = null,
    val pageList:MutableMap<String,PageData> = LinkedHashMap<String,PageData>(),
    val itemList:MutableMap<String,ItemData> = LinkedHashMap<String,ItemData>(),
//    val themesList:MutableMap<String,ItemData> = LinkedHashMap<String,ItemData>(),
    // TODO: Medias, Shortcuts
//    val installedPackageInfo: MutableList<PackageInfo> = emptyList<PackageInfo>().toMutableList()
    val installedPackageInfo: MutableMap<String,SearchableApps> = LinkedHashMap<String,SearchableApps>(),
    val standaloneWidgetIdSelected:Int = 1,
    val standaloneWidgetConfigMode:String = "",
) {
}