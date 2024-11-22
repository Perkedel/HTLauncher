package com.perkedel.htlauncher.data.viewmodels

import android.content.ClipData.Item
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.enumerations.EditWhich
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

class ItemEditorViewModel:ViewModel() {
    var uri: Uri? by mutableStateOf(null)
        private set

    var editType: EditWhich? by mutableStateOf(null)
        private set

    var filename: String? by mutableStateOf(null)
    var rawContent: String? by mutableStateOf(null)

    // TypedEdit
    var itemData: ItemData? by mutableStateOf(null)
    var homeData: HomepagesWeHave? by mutableStateOf(null)
    var pageData: PageData? by mutableStateOf(null)
    var jsoning: Json? by mutableStateOf(null)

    fun updateUri(uri: Uri?){
        this.uri = uri
    }
    fun updateFilename(filename: String?){
        this.filename = filename
    }
    fun updateEditType(editType: EditWhich?){
        this.editType = editType
    }
    fun updateRawContent(into : String?){
        this.rawContent = into
    }
    fun updateJsoning(contain:Json){
        this.jsoning = contain
    }

    fun typedEditNow(editType: EditWhich?, rawJson: String){
        val json = Json {
            // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
            prettyPrint = true
            encodeDefaults = true
        }
        when(editType){
            EditWhich.Items -> itemData = json.decodeFromString<ItemData>(rawJson)
            EditWhich.Pages -> pageData = json.decodeFromString<PageData>(rawJson)
            EditWhich.Home -> homeData = json.decodeFromString<HomepagesWeHave>(rawJson)
            else -> {}
        }
//        updateJsoning(json.decodeFromString(rawJson))
    }
}