package com.perkedel.htlauncher

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.TestJsonData
import com.perkedel.htlauncher.enumerations.EditWhich
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class HTViewModel : ViewModel() {
    // https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state#11
    private val _uiState = MutableStateFlow(HTUIState())
    val uiState : StateFlow<HTUIState> = _uiState.asStateFlow()

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    suspend fun preloadFiles(context: Context, contentResolver: ContentResolver, uiStating:HTUIState, listOfFolder:List<String>, folders: MutableMap<String,Uri>, json: Json){
        // https://programmingheadache.com/2024/02/13/effortless-loading-screen-with-state-flows-and-jetpack-compose-just-4-easy-steps/
        setIsReady(into = false)

//        val launch = viewModelScope.launch {

            // Full screen
            // https://stackoverflow.com/a/69689196/9079640
//        systemUiController.isStatusBarVisible = false

            // You must Folders!!
            val homeSafData:HomepagesWeHave = HomepagesWeHave()
            if(uiStating.selectedSaveDir != null && uiStating.selectedSaveDir.toString().isNotEmpty()){
                for(a in listOfFolder){
                    folders[a] = getADirectory(uiStating.selectedSaveDir!!, context, a)
                    Log.d("FolderQuery", "Folder ${folders[a]} queried")
                }
                for(i in folders){
                    Log.d("InitFileLoader","Folder ${i.key} we have ${i.value}")
                }
//            getADirectory(htuiState.selectedSaveDir!!, context, "Items")
//            getADirectory(htuiState.selectedSaveDir!!, context, "Themes")
//            getADirectory(htuiState.selectedSaveDir!!, context, "Medias")


                val homeSaf:String = json.encodeToString<HomepagesWeHave>(homeSafData)
                val homeSafFileUri = getATextFile(
                    dirUri = uiStating.selectedSaveDir!!,
                    context = context,
                    fileName = "${context.resources.getString(R.string.home_screen_file)}.json",
                    initData = homeSaf,
                    hardOverwrite = true,
                )
                Log.d("InitFileLoader", "Pls Homescreen:\n${homeSaf}")
                setHomeScreenJson(
                    homeSafFileUri
                )
                Log.d("InitFileLoader", "Pls the file Homescreen ${uiStating.coreConfig}")


            } else {
                Log.d("InitFileLoader", "Save Dir Not Selected")
            }


            if(uiStating.selectedSaveDir != null &&uiStating.selectedSaveDir.toString().isNotEmpty()) {
                // https://dev.to/vtsen/how-to-debug-jetpack-compose-recomposition-with-logging-k7g
                // https://developer.android.com/reference/android/util/Log
                // https://stackoverflow.com/a/74044617/9079640
                Log.d("DebugHomescreen", "Will check ${uiStating.selectedSaveDir}")
                if (uiStating.coreConfig != null && uiStating.coreConfig.toString().isNotEmpty()) {
                    Log.d("DebugHomescreen", "There is something!")
                    val fileStream:String = openATextFile(
                        uri = uiStating.coreConfig!!,
                        contentResolver = contentResolver
                    )
                    Log.d("DebugHomescreen", "It contains:\n${fileStream}")
                    loadHomeScreenJsonElements(
                        json.decodeFromString<HomepagesWeHave>(
                            fileStream
                        )
                    )
                } else {
                    Log.d("DebugHomescreen", "There is nothing!")
                    loadHomeScreenJsonElements(
                        homeSafData
                    )
                }
            } else {
                // DONE: when not select, add dummy demo page
                Log.d("DebugHomescreen", "Literally nothing!")
                loadHomeScreenJsonElements(
                    homeSafData
                )
            }

            // Load Pages & Items
            if(uiStating.testPreloadAll && uiStating.coreConfigJson != null && folders[context.resources.getString(R.string.pages_folder)] != null){
                for(i in uiStating.coreConfigJson!!.pagesPath){
                    Log.d("PageLoader","Checking page ${i}")
                    Log.d("PageLoader","Eval context ${context}")
                    Log.d("PageLoader","Eval resource name ${context.resources.getString(R.string.pages_folder)}")
                    Log.d("PageLoader","Eval dirUri ${folders[context.resources.getString(R.string.pages_folder)]}")

                    var aPage: PageData = PageData()

                    if(uiStating.pageList.contains(i) && uiStating.pageList[i] != null){
                        Log.d("PageLoader", "Already Exist ${uiStating.itemList[i]}")
                        aPage = uiStating.pageList[i]!!
                    } else {
                        val aPageUri: Uri = getATextFile(
                            dirUri = folders[context.resources.getString(R.string.pages_folder)]!!,
                            context = context,
                            initData = json.encodeToString<PageData>(PageData()),
                            fileName = "$i.json",
                            hardOverwrite = false,
                        )
                        Log.d("PageLoader", "Page URI in total ${aPageUri}")
                        aPage = json.decodeFromString<PageData>(
                            openATextFile(
                                uri = aPageUri,
                                contentResolver = contentResolver,
                            )
                        )
                        if (uiStating.pageList.contains(i)) {
                            // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/contains-key.html
                            Log.d("PageLoader", "Key $i Exist!")
                        } else {
                            Log.d("PageLoader", "Key $i 404 NOT FOUND!")
                        }
                        uiStating.pageList[i] = aPage
                    }

                    // item
                    for (j in aPage.items) {
                        Log.d("ItemLoader", "Checking item ${j}")
                        var aItem: ItemData = ItemData()
                        if (uiStating.itemList.contains(j) && uiStating.itemList[j] != null) {
                            Log.d("ItemLoader", "Already Exist ${uiStating.itemList[j]}")
                            aItem = uiStating.itemList[j]!!
                        } else {
                            val aItemUri: Uri = getATextFile(
                                dirUri = folders[context.resources.getString(R.string.items_folder)]!!,
                                context = context,
                                initData = json.encodeToString<ItemData>(ItemData()),
                                fileName = "$j.json",
                                hardOverwrite = true,
                            )
                            aItem = json.decodeFromString<ItemData>(
                                openATextFile(
                                    uri = aItemUri,
                                    contentResolver = contentResolver,
                                )
                            )
                            if (uiStating.itemList.contains(j)) {
                                // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/contains-key.html
                                Log.d("ItemLoader", "Key $j Exist!")
                            } else {
                                Log.d("ItemLoader", "Key $j 404 NOT FOUND!")
                            }
                            uiStating.itemList[j] = aItem
                        }
                    }
                }
            }
            setIsReady(into = true)
//        }
    }

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

    fun setIsReady(into:Boolean){
        _uiState.update {
            currentState -> currentState.copy(
                isReady = into
            )
        }
    }

    fun setEditWhich(into:EditWhich){
        _uiState.update {
            currentState -> currentState.copy(
                toEditWhatFile = into
            )
        }
    }
}