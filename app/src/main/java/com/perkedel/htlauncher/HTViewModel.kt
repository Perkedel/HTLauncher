@file:OptIn(FlowPreview::class)

package com.perkedel.htlauncher

import android.content.ClipData.Item
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.pdf.PdfDocument.Page
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
import androidx.compose.ui.res.stringResource
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.viewModelScope
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.SearchableApps
import com.perkedel.htlauncher.data.TestJsonData
import com.perkedel.htlauncher.data.hardcodes.HTLauncherHardcodes
import com.perkedel.htlauncher.enumerations.ActionDataLaunchType
import com.perkedel.htlauncher.enumerations.ActionInternalCommand
import com.perkedel.htlauncher.enumerations.EditWhich
import com.perkedel.htlauncher.func.AsyncService
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

@OptIn(FlowPreview::class)
class HTViewModel(
    private val asyncService: AsyncService = AsyncService()
) : ViewModel() {
    // https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state#11
    private val _uiState = MutableStateFlow(HTUIState())
    val uiState : StateFlow<HTUIState> = _uiState.asStateFlow()

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    val _appSearchText = MutableStateFlow("")
    val appSearchText = _appSearchText.asStateFlow()

    val _appSearchActive = MutableStateFlow(false)
    val appSearchActive = _appSearchActive.asStateFlow()


    val _appAll = MutableStateFlow(listOf<SearchableApps>())
    val appAll = appSearchText
        .debounce(1500L)
        .onEach { _appSearchActive.update { true } }
        .combine(_appAll){ text, apps ->
            if(text.isBlank()){
                apps
            } else {
                apps.filter {
//                    it.packageName.contains(text)
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _appAll.value
        )

    fun updateAppAll(with:List<SearchableApps>){
        _appAll.value = with
    }
    fun updateAppSearchActive(with:Boolean){
        _appSearchActive.value = with
    }
    fun updateAppSearchText(with: String){
        _appSearchText.value = with
    }
    fun installAllApps(with:List<PackageInfo>, packageManager: PackageManager){
        updateAppAll(
            with.map {
                SearchableApps(
                    packageName = it.packageName,
                    label = it.applicationInfo?.loadLabel(packageManager).toString()
                )
            }
        )
    }
    fun initializeAllApps(with:List<PackageInfo>, packageManager: PackageManager){
        viewModelScope.launch {
//            installAllApps(with,packageManager)
            _appAll.value = asyncService.getSearchableApps(with,packageManager)
        }
    }
    fun getSearchableApps(){

    }

    suspend fun preloadApps(context: Context, packageManager: PackageManager){
//        _uiState.update {
//            currentState -> currentState.copy(
//                installedPackageInfo =
//            )
//        }
    }
    suspend fun preloadFiles(context: Context, contentResolver: ContentResolver, uiStating:HTUIState = uiState.value, listOfFolder:List<String>, folders: MutableMap<String,Uri>, json: Json = Json{
        // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
        prettyPrint = true
        encodeDefaults = true
    }, force:Boolean = false){
        // https://programmingheadache.com/2024/02/13/effortless-loading-screen-with-state-flows-and-jetpack-compose-just-4-easy-steps/

        viewModelScope.launch {
            setIsReady(into = false)
            if(force){
                // clear everything
                uiStating.pageList.clear()
                uiStating.itemList.clear()
            }

//        val launch = viewModelScope.launch {

            // Full screen
            // https://stackoverflow.com/a/69689196/9079640
//        systemUiController.isStatusBarVisible = false

            // You must Folders!!
            val homeSafData:HomepagesWeHave = HTLauncherHardcodes.HOMESCREEN_FILE
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
                var pageFolder:Uri = getADirectory(
                    dirUri = uiStating.selectedSaveDir!!,
                    context = context,
                    dirName = context.resources.getString(R.string.pages_folder)
                )
                val pageFiles:List<DocumentFile> = DocumentFile.fromTreeUri(context,pageFolder)?.listFiles()?.toList() ?: emptyList()
                var pageFileNames:List<String> = pageFiles.map { it.name?.replaceAfterLast(".json","") ?: "" }.toList()
//                var pageFileNames:List<String> = pageFiles.map { it.name ?: "" }.toList()

                val itemFolder:Uri = getADirectory(
                    dirUri = uiStating.selectedSaveDir!!,
                    context = context,
                    dirName = context.resources.getString(R.string.items_folder)
                )
                val itemFiles:List<DocumentFile> = DocumentFile.fromTreeUri(context,itemFolder)?.listFiles()?.toList() ?: emptyList()
                val itemFileNames:List<String> = itemFiles.map { it.name?.replaceAfterLast(".json","") ?: "" }.toList()

                // fill rest
                Log.d("FolderQuery","Loading Rest of the items now!")
                // https://medium.com/@cepv2010/how-to-easily-choose-files-in-android-compose-28f4637d1c21 not this
                // https://medium.easyread.co/android-data-and-file-storage-cheatsheet-for-media-95f7f66080e3 not that
                for(i in pageFiles){
                    Log.d("PageQuery","Check $i")
                    val pageingUri:Uri = getATextFile(
                        dirUri = folders[context.resources.getString(R.string.pages_folder)]!!,
                        context = context,
                        initData = json.encodeToString<PageData>(PageData(
                            name = i.name?.replaceAfterLast(".json","") ?: "anItem"
                        )),
                        fileName = i.name ?: "",
//                            fileName = i,
                        hardOverwrite = false,
                    )
                    try {
                        val pageingData:PageData = json.decodeFromString<PageData>(
                            openATextFile(
                                uri = pageingUri,
                                contentResolver = contentResolver,
                            )
                        )
                        uiStating.pageList[pageingData.name] = pageingData
                    } catch (e: Exception) {
//                        Log.d("PageQuery","Error $e")
                    }
                }
                for(i in itemFiles){
                    Log.d("ItemQuery","Check $i")
                    val itemingUri:Uri = getATextFile(
                        dirUri = folders[context.resources.getString(R.string.items_folder)]!!,
                        context = context,
                        initData = json.encodeToString<ItemData>(ItemData(
                            name = i.name?.replaceAfterLast(".json","") ?: "anItem"
                        )),
                        fileName = i.name ?: "",
                        hardOverwrite = false,
                    )
                    try {
                        val itemingData:ItemData = json.decodeFromString<ItemData>(
                            openATextFile(
                                uri = itemingUri,
                                contentResolver = contentResolver,
                            )
                        )
                        uiStating.itemList[itemingData.name] = itemingData
                    } catch (e: Exception) {
//                        Log.d("ItemQuery","Error $e")
                    }
                }

                // in page
                Log.d("OnQuery","Now checking files now!")
                for(i in uiStating.coreConfigJson!!.pagesPath){
//                for(i in pageFileNames){
                    Log.d("PageLoader","Checking page ${i}")
                    Log.d("PageLoader","Eval context ${context}")
                    Log.d("PageLoader","Eval resource name ${context.resources.getString(R.string.pages_folder)}")
                    Log.d("PageLoader","Eval dirUri ${folders[context.resources.getString(R.string.pages_folder)]}")

                    val predeterminedPage: PageData = when(i){
                        context.resources.getString(R.string.home_screen_page_file) -> HTLauncherHardcodes.HOMEPAGE_FILE
                        else -> PageData(
                            name = i,
                            isHome = i.contains("Home")
                        )
                    }
                    var aPage: PageData = PageData()

                    if(uiStating.pageList.contains(i) && uiStating.pageList[i] != null){
                        Log.d("PageLoader", "Already Exist ${uiStating.itemList[i]}")
                        aPage = uiStating.pageList[i]!!
                    } else {
                        val aPageUri: Uri = getATextFile(
                            dirUri = folders[context.resources.getString(R.string.pages_folder)]!!,
                            context = context,
                            initData = json.encodeToString<PageData>(predeterminedPage),
                            fileName = "$i.json",
//                            fileName = i,
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
//                    for (j in itemFileNames) {
                        Log.d("ItemLoader", "Checking item ${j}")

                        val itemIsInternalCommand:Boolean =
                            j.contains(ActionInternalCommand.AllApps.name) ||
                            j.contains(ActionInternalCommand.Camera.name) ||
                            j.contains(ActionInternalCommand.Telephone.name) ||
                            j.contains(ActionInternalCommand.GoToPage.name) ||
                            j.contains(ActionInternalCommand.Gallery.name) ||
                            j.contains(ActionInternalCommand.Clock.name) ||
                            j.contains(ActionInternalCommand.Contacts.name) ||
                            j.contains(ActionInternalCommand.Emergency.name) ||
                            j.contains(context.resources.getString(ActionInternalCommand.Emergency.id)) ||
                            j.contains("SOS", true) ||
                            j.contains(ActionInternalCommand.Messages.name) ||
                            j.contains(ActionInternalCommand.Settings.name)
                        val predeterminedItem: ItemData = when{
                            itemIsInternalCommand -> ItemData(
                                name = j,
                                label = j,
                                action = listOf(
                                    ActionData(
                                        name = j,
                                        action = j,
                                        type = ActionDataLaunchType.Internal,
                                    )
                                ),
                            )
                            else -> ItemData()
                        }
                        var aItem: ItemData = ItemData()

                        if (uiStating.itemList.contains(j) && uiStating.itemList[j] != null) {
                            Log.d("ItemLoader", "Already Exist ${uiStating.itemList[j]}")
                            aItem = uiStating.itemList[j]!!
                        } else {
                            val aItemUri: Uri = getATextFile(
                                dirUri = folders[context.resources.getString(R.string.items_folder)]!!,
                                context = context,
                                initData = json.encodeToString<ItemData>(predeterminedItem),
                                fileName = "$j.json",
//                                fileName = j,
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

    }

    fun createNewFileNow(name:String="",atWhere:EditWhich = EditWhich.Items, context: Context, uiStating:HTUIState = uiState.value, json: Json = Json{
        // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
        prettyPrint = true
        encodeDefaults = true
    },){
        viewModelScope.launch {
            if(uiStating.selectedSaveDir != null) {
                val selectFolder: Uri = getADirectory(
                    dirUri = uiStating.selectedSaveDir,
                    dirName = when(atWhere){
                        EditWhich.Items -> context.resources.getString(R.string.items_folder)
                        EditWhich.Pages -> context.resources.getString(R.string.pages_folder)
                        else -> context.resources.getString(R.string.misc_folder)
                    },
                    context = context
                )

                when(atWhere){
                    EditWhich.Items -> {
                        val predeterminedItem:ItemData = ItemData(
                            name = name,
                            label = name,
                        )
                        val aItemUri: Uri = getATextFile(
                            dirUri = selectFolder,
                            context = context,
                            initData = json.encodeToString<ItemData>(predeterminedItem),
                            fileName = "$name.json",
                            hardOverwrite = true,
                        )
                        val aItem:ItemData = json.decodeFromString<ItemData>(
                            openATextFile(
                                uri = aItemUri,
                                contentResolver = context.contentResolver,
                            )
                        )
                        Log.d("CreateNewFile","New Item $name")
                        uiStating.itemList[name] = aItem
                    }
                    EditWhich.Pages -> {
                        val predeterminedPage: PageData = PageData(
                            name = name,
                        )
                        val aPageUri: Uri = getATextFile(
                            dirUri = selectFolder,
                            context = context,
                            initData = json.encodeToString<PageData>(predeterminedPage),
                            fileName = "$name.json",
                            hardOverwrite = false,
                        )
                        val aPage = json.decodeFromString<PageData>(
                            openATextFile(
                                uri = aPageUri,
                                contentResolver = context.contentResolver,
                            )
                        )
                        Log.d("CreateNewFile","New Page $name")
                        uiStating.pageList[name] = aPage
                    }
                    else -> {

                    }
                }
            }
        }

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

    fun openCreateNewFile(opened: Boolean = true){
        _uiState.update {
            currentState -> currentState.copy(
                openCreateNewFile = opened
            )
        }
    }

    fun openCreateNewFile(){
        _uiState.update { currentState ->
            currentState.copy(
                openCreateNewFile = !_uiState.value.openCreateNewFile
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

    fun setEditingLevel(into:Boolean = false){
        _uiState.update {
            currentState -> currentState.copy(
                editingLevel = into
            )
        }
    }
}