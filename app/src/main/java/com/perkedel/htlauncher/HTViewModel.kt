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
import kotlinx.coroutines.flow.update

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

    fun openTheMoreMenu(){
        _uiState.update {
                currentState -> currentState.copy(
            openMoreMenu = !_uiState.value.openMoreMenu
        )
        }
    }
}