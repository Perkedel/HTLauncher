package com.perkedel.htlauncher.data.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ItemEditorViewModel:ViewModel() {
    var uri: Uri? by mutableStateOf(null)
        private set
    var filename: String? by mutableStateOf(null)

    fun updateUri(uri: Uri?){
        this.uri = uri
    }
    fun updateFilename(filename: String){
        this.filename = filename
    }
}