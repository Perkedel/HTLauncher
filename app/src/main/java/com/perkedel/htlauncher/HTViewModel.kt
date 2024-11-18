package com.perkedel.htlauncher

import android.net.Uri
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.TestJsonData
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.JsonElement

class HTViewModel : ViewModel() {
    // https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state#11
    private val _uiState = MutableStateFlow(HTUIState())
    val uiState : StateFlow<HTUIState> = _uiState.asStateFlow()

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dissmissPermissionDialog(){
        if(Build.VERSION.SDK_INT >= 35)
            visiblePermissionDialogQueue.removeLast()
        else
            visiblePermissionDialogQueue.remove(visiblePermissionDialogQueue.last())
    }

    fun onPermissionResult(
        permission: String,
        isGranted:Boolean = false,
    ){
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)){
            visiblePermissionDialogQueue.add(0,permission)
        }
    }

    fun openTheMoreMenu(opened:Boolean = true){
//        _uiState.value.openMoreMenu = opened
        _uiState.update {
            currentState -> currentState.copy(
                openMoreMenu = opened
            )
        }
    }

    fun selectSaveDirUri(dirUri: Uri? = null){
        _uiState.update {
            currentState -> currentState.copy(
                selectedSaveDir = dirUri
            )
        }
    }

    fun changeTestResult(into:String = "HOAHOA"){
        _uiState.update {
            currentState -> currentState.copy(
                testResult = into,
            )
        }
    }

    fun injectTestJsonResult(into:TestJsonData){
        _uiState.update {
            currentState -> currentState.copy(
                testJsonElement = into,
            )
        }
    }

    fun openTheMoreMenu(){
        _uiState.update {
                currentState -> currentState.copy(
            openMoreMenu = !_uiState.value.openMoreMenu
        )
        }
    }

    fun setHomeScreenJson(file:Uri){
        _uiState.update {
            currentState -> currentState.copy(
                coreConfig = file,
            )
        }
    }

    fun loadHomeScreenJsonElements(contains:HomepagesWeHave){
        _uiState.update {
            currentState ->currentState.copy(
                coreConfigJson = contains,
            )
        }
    }
}