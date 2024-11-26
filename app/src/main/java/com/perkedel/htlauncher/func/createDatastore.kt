package com.perkedel.htlauncher.func

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun createDataStore(producePath: () -> String) :DataStore<Preferences>{
   return PreferenceDataStoreFactory.createWithPath(
       produceFile = {producePath().toPath()}
   )
}

//fun getDataStore(producePath: () -> String) :DataStore<Preferences>{
//    return
//}

fun createDataStore(context: Context): DataStore<Preferences> {
    // https://youtu.be/cQ64RkdPZBw
    return createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}

const val DATA_STORE_FILE_NAME = "htlauncher.preferences_pb"