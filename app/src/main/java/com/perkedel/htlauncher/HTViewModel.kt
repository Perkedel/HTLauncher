@file:OptIn(FlowPreview::class)

package com.perkedel.htlauncher

import android.content.ClipData.Item
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.pdf.PdfDocument.Page
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.viewModelScope
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.SearchableApps
import com.perkedel.htlauncher.data.TestJsonData
import com.perkedel.htlauncher.constanta.HTLauncherHardcodes
import com.perkedel.htlauncher.enumerations.ActionDataLaunchType
import com.perkedel.htlauncher.enumerations.ActionInternalCommand
import com.perkedel.htlauncher.enumerations.EditWhich
import com.perkedel.htlauncher.enumerations.InternalCategories
import com.perkedel.htlauncher.func.AsyncService
import com.perkedel.htlauncher.func.removeDotExtensions
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(FlowPreview::class)
class HTViewModel(
    private val asyncService: AsyncService = AsyncService()
) : ViewModel() {
    // eltaroba
    // https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state#11
    private val _uiState = MutableStateFlow(HTUIState())
    val uiState : StateFlow<HTUIState> = _uiState.asStateFlow()

//    var _currentPageIconModel: MutableStateFlow<Any?> = MutableStateFlow<Any?>(null)
//    val currentPageIconModel:StateFlow<Any?> = _currentPageIconModel.asStateFlow()

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    val _appSearchText = MutableStateFlow("")
    val appSearchText = _appSearchText.asStateFlow()

    val _appSearchActive = MutableStateFlow(false)
    val appSearchActive = _appSearchActive.asStateFlow()

    val _appReadyToShow = MutableStateFlow(false)
    val appReadyToShow = _appReadyToShow.asStateFlow()

    val _appAll = MutableStateFlow(listOf<SearchableApps>())
    val appAll = appSearchText
        .debounce(1500L)
        .onEach { _appSearchActive.update { true } }
        .combine(_appAll){ text, apps ->
            if(text.isBlank()){
                apps.sortedBy {
                    it.label
                }
            } else {
                apps.filter {
//                    it.packageName.contains(text)
                    it.doesMatchSearchQuery(text)
                }.sortedBy {
                    it.label
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
    suspend fun initializeAllApps(with:List<PackageInfo>, packageManager: PackageManager){
//        viewModelScope.launch {
            _appReadyToShow.value = false
//            installAllApps(with,packageManager)
            _appAll.value = asyncService.getSearchableApps(with,packageManager)
            _appReadyToShow.value = true
//        }
    }
    suspend fun initializeAllAppsResolve(with:List<ResolveInfo>, packageManager: PackageManager){
//        viewModelScope.launch {
            _appReadyToShow.value = false
//            installAllApps(with,packageManager)
            _appAll.value = asyncService.getSearchableAppsResolve(with,packageManager)
            _appReadyToShow.value = true
//        }
    }
    suspend fun appendAllApps(base:List<SearchableApps>, with:List<PackageInfo>, packageManager: PackageManager){
//        viewModelScope.launch {
            _appReadyToShow.value = false
//            installAllApps(with,packageManager)
            _appAll.value = asyncService.appendSearchableApps(base, with,packageManager)
            _appReadyToShow.value = true
//        }
    }
    suspend fun appendAllAppsResolve(base:List<SearchableApps>, with:List<ResolveInfo>, packageManager: PackageManager){
//        viewModelScope.launch {
            _appReadyToShow.value = false
//            installAllApps(with,packageManager)
            _appAll.value = asyncService.appendSearchableAppsResolve(base, with,packageManager)
            _appReadyToShow.value = true
//        }
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
    fun preloadFiles(context: Context, contentResolver: ContentResolver, uiStating:HTUIState = uiState.value, listOfFolder:List<String>, folders: MutableMap<String,Uri>, json: Json = Json{
        // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
        prettyPrint = true
        encodeDefaults = true
    }, force:Boolean = false){
        // https://programmingheadache.com/2024/02/13/effortless-loading-screen-with-state-flows-and-jetpack-compose-just-4-easy-steps/

//        viewModelScope.launch {

            var initing:Boolean = uiStating.inited
            Log.d("PreloadFiles","Inited is ${initing}")
            if(force){
                // clear everything
                uiStating.pageList.clear()
                uiStating.itemList.clear()
//                uiStating.coreConfig = null
//                uiStating.coreConfigJson = null
                updateIniting(false)
                initing = false
            } else {
                if(initing){
                    return
                }
            }

//        val launch = viewModelScope.launch {

            // Full screen
            // https://stackoverflow.com/a/69689196/9079640
//        systemUiController.isStatusBarVisible = false

            // You must Folders!!
            val homeSafData:HomepagesWeHave = HTLauncherHardcodes.HOMESCREEN_FILE
            var homeSafFileUri:Uri = Uri.EMPTY
            if(!initing) {

                if (uiStating.selectedSaveDir != null && uiStating.selectedSaveDir.toString()
                        .isNotEmpty()
                ) {
                    for (a in listOfFolder) {
                        folders[a] = getADirectory(uiStating.selectedSaveDir!!, context, a)
                        Log.d("FolderQuery", "Folder ${folders[a]} queried")
                    }
                    for (i in folders) {
                        Log.d("InitFileLoader", "Folder ${i.key} we have ${i.value}")
                    }
//            getADirectory(htuiState.selectedSaveDir!!, context, "Items")
//            getADirectory(htuiState.selectedSaveDir!!, context, "Themes")
//            getADirectory(htuiState.selectedSaveDir!!, context, "Medias")


                    val homeSaf: String = json.encodeToString<HomepagesWeHave>(homeSafData)
                    homeSafFileUri = getATextFile(
                        dirUri = uiStating.selectedSaveDir!!,
                        context = context,
                        fileName = "${context.resources.getString(R.string.home_screen_file)}.json",
                        initData = homeSaf,
                        hardOverwrite = true,
                    )
                    Log.d("InitFileLoader", "Pls Homescreen ${homeSafFileUri}:\n${homeSaf}")
                    setHomeScreenJson(
                        homeSafFileUri
                    )
                    setHomeScreenJson(
                        homeSafFileUri
                    )
                    Log.d(
                        "InitFileLoader",
                        "Pls the file Homescreen ${uiStating.coreConfig} fill ${homeSafFileUri}"
                    )


                } else {
                    Log.d("InitFileLoader", "Save Dir Not Selected")
                }

                if (uiStating.selectedSaveDir != null && uiStating.selectedSaveDir.toString()
                        .isNotEmpty()
                ) {
                    // https://dev.to/vtsen/how-to-debug-jetpack-compose-recomposition-with-logging-k7g
                    // https://developer.android.com/reference/android/util/Log
                    // https://stackoverflow.com/a/74044617/9079640
                    Log.d(
                        "DebugHomescreen",
                        "Will check ${uiStating.selectedSaveDir}, the ${homeSafFileUri}"
                    )
                    if (homeSafFileUri.toString().isNotEmpty()) {
//                if (uiStating.coreConfig != null && uiStating.coreConfig.toString().isNotEmpty()) {
                        Log.d("DebugHomescreen", "There is something!")
                        val fileStream: String = openATextFile(
                            uri = homeSafFileUri,
//                        uri = uiStating.coreConfig!!,
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
                } else
                {
                    // DONE: when not select, add dummy demo page
                    Log.d("DebugHomescreen", "Literally nothing!")
                    loadHomeScreenJsonElements(
                        homeSafData
                    )
                }

                // Load Pages & Items
                if(uiStating.testPreloadAll && uiStating.coreConfigJson != null && folders[context.resources.getString(R.string.pages_folder)] != null){
                    setIsReady(into = false)
                    var pageFolder:Uri = getADirectory(
                        dirUri = uiStating.selectedSaveDir!!,
                        context = context,
                        dirName = context.resources.getString(R.string.pages_folder)
                    )
                    val pageFiles:List<DocumentFile> = DocumentFile.fromTreeUri(context,pageFolder)?.listFiles()?.toList() ?: emptyList()
                    var pageFileNames:List<String> = pageFiles.map { removeDotExtensions(it.name ?: "") }.toList()
//                var pageFileNames:List<String> = pageFiles.map { it.name?.substring(0, it.name?.lastIndexOf('.') ?: 0) ?: "" }.toList()
//                var pageFileNames:List<String> = pageFiles.map { it.name ?: "" }.toList()
                    // but also pls add the built-in things!


                    val itemFolder:Uri = getADirectory(
                        dirUri = uiStating.selectedSaveDir!!,
                        context = context,
                        dirName = context.resources.getString(R.string.items_folder)
                    )
                    val itemFiles:List<DocumentFile> = DocumentFile.fromTreeUri(context,itemFolder)?.listFiles()?.toList() ?: emptyList()
                    val itemFileNames:List<String> = itemFiles.map { removeDotExtensions(it.name ?: "") }.toList()
//                val itemFileNames:List<String> = itemFiles.map { it.name?.substring(0, it.name?.lastIndexOf('.') ?: 0) ?: "" }.toList()

                    // fill rest
                    Log.d("FolderQuery","Loading Rest of the items now!")
                    // https://medium.com/@cepv2010/how-to-easily-choose-files-in-android-compose-28f4637d1c21 not this
                    // https://medium.easyread.co/android-data-and-file-storage-cheatsheet-for-media-95f7f66080e3 not that
                    for(i in pageFiles){
                        if(uiStating.pageList.contains(removeDotExtensions(i.name ?: ""))) continue
                        Log.d("PageQuery","Check $i")
                        val pageingUri:Uri = getATextFile(
                            dirUri = folders[context.resources.getString(R.string.pages_folder)]!!,
                            context = context,
                            initData = json.encodeToString<PageData>(PageData(
                                name = removeDotExtensions(i.name ?: "anItem")
//                                name = i.name?.replaceAfterLast(".json","") ?: "anItem"
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
                        if(uiStating.itemList.contains(removeDotExtensions(i.name ?: ""))) continue
                        Log.d("ItemQuery","Check $i")
                        val itemingUri:Uri = getATextFile(
                            dirUri = folders[context.resources.getString(R.string.items_folder)]!!,
                            context = context,
                            initData = json.encodeToString<ItemData>(ItemData(
                                name = removeDotExtensions(i.name ?: "anItem")
//                                name = i.name?.replaceAfterLast(".json","") ?: "anItem"
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
                        if(uiStating.pageList.contains(i) && i != "Home") continue
                        Log.d("PageLoader","Checking page ${i}")
                        Log.d("PageLoader","Eval context ${context}")
                        Log.d("PageLoader","Eval resource name ${context.resources.getString(R.string.pages_folder)}")
                        Log.d("PageLoader","Eval dirUri ${folders[context.resources.getString(R.string.pages_folder)]}")

                        val predeterminedPage: PageData = when(i){
                            context.resources.getString(R.string.home_screen_page_file) -> HTLauncherHardcodes.HOMEPAGE_FILE
                            "Settings" -> HTLauncherHardcodes.SETTINGS_PAGE_FILE
                            else -> PageData(
                                name = i,
                                isHome = i.contains("Home")
                            )
                        }
                        var aPage: PageData = predeterminedPage

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
//                        if (uiStating.pageList.contains(i)) {
//                            // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/contains-key.html
//                            Log.d("PageLoader", "Key $i Exist!")
//                        } else {
//                            Log.d("PageLoader", "Key $i 404 NOT FOUND!")
//                        }
                        }
                        uiStating.pageList[i] = aPage

                        // item
                        for (j in aPage.items) {
//                    for (j in itemFileNames) {
                            Log.d("ItemLoader", "Checking item ${j}")

                            val itemIsInternalCommand:Boolean =
                                j == ActionInternalCommand.AllApps.name ||
                                        j == ActionInternalCommand.Camera.name ||
                                        j == ActionInternalCommand.Telephone.name ||
                                        j == ActionInternalCommand.GoToPage.name ||
                                        j == ActionInternalCommand.Gallery.name ||
                                        j == ActionInternalCommand.Clock.name ||
                                        j == ActionInternalCommand.Contacts.name ||
                                        j == ActionInternalCommand.Emergency.name ||
                                        j == context.resources.getString(ActionInternalCommand.Emergency.id) ||
                                        j == "SOS" ||
                                        j == ActionInternalCommand.Messages.name ||
                                        j == ActionInternalCommand.Settings.name ||
                                        j == ActionInternalCommand.SystemSettings.name ||
                                        j == ActionInternalCommand.Preferences.name ||
                                        j == InternalCategories.SettingsSystem.name ||
                                        j == InternalCategories.SettingsOverall.name
//                        Log.d("ItemLoader","Item is internal command $j")
//                        val itemIsCategory:Boolean =
//                            j == InternalCategories.SettingsSystem.name ||
//                                    j == InternalCategories.SettingsOverall.name
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
//                            itemIsCategory -> ItemData(
//                                name = j,
//                                label = j,
//                                action = listOf(
//                                    ActionData(
//                                        name = j,
//                                        action = j,
//                                        type = ActionDataLaunchType.Category,
//                                    )
//                                ),
//                            )
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
//                            if (uiStating.itemList.contains(j)) {
//                                // https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/contains-key.html
//                                Log.d("ItemLoader", "Key $j Exist!")
//                            } else {
//                                Log.d("ItemLoader", "Key $j 404 NOT FOUND!")
//                            }

                            }
                            uiStating.itemList[j] = aItem
                        }
                    }
                    setIsReady(into = true)
                    initing = true
                    updateIniting(initing)
                }

            } else {
                setIsReady(into = true)
                initing = true
                updateIniting(initing)
            }
//        }

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

    fun updateIniting(into: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                inited = into
            )
        }
    }

    fun updateCurrentPageIconModel(with:Any?){
//        _currentPageIconModel.value = with
        _uiState.update { currentState ->
            currentState.copy(
                currentPageIconModel = with
            )
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

    fun getPageData(of:String = "", json:Json = Json{
        prettyPrint = true
        encodeDefaults = true
    }, context: Context, ignoreFile:Boolean = false, forceReload:Boolean = false):PageData{
        val predeterminedPage: PageData = when(of){
            "Home" -> HTLauncherHardcodes.HOMEPAGE_FILE
            "home" -> HTLauncherHardcodes.HOMEPAGE_FILE
            "Settings" -> HTLauncherHardcodes.SETTINGS_PAGE_FILE
            else -> PageData(
                name = of,
                isHome = of.contains("Home")
            )
        }
        if(_uiState.value.pageList.contains(of)) return _uiState.value.pageList[of] ?: predeterminedPage
        var aPage: PageData = predeterminedPage
        if(_uiState.value.selectedSaveDir != null && _uiState.value.selectedSaveDir.toString().isNotBlank() && !ignoreFile) {
            if(_uiState.value.pageList.contains(of) && _uiState.value.pageList[of] != null || forceReload){
                aPage = _uiState.value.pageList[of]!!
            } else {
                val selectFolder: Uri = getADirectory(
                    dirUri = _uiState.value.selectedSaveDir!!,
                    dirName = "Pages",
                    context = context
                )
                val aPageUri: Uri = getATextFile(
                    dirUri = selectFolder,
                    context = context,
                    initData = json.encodeToString<PageData>(predeterminedPage),
                    fileName = "$of.json",
                    hardOverwrite = false,
                )
                aPage = json.decodeFromString<PageData>(
                    openATextFile(
                        uri = aPageUri,
                        contentResolver = context.contentResolver,
                    )
                )
            }
            _uiState.value.pageList[of] = aPage
        } else {

        }


        return _uiState.value.pageList[of] ?: predeterminedPage
    }
    fun getItemData(of: String, json:Json = Json{
        prettyPrint = true
        encodeDefaults = true
    }, context: Context, ignoreFile:Boolean = false, forceReload:Boolean = false):ItemData{
        // codeium shim!!!!
        val itemIsInternalCommand:Boolean =
            of == context.resources.getString(ActionInternalCommand.AllApps.id) ||
                    of == context.resources.getString(ActionInternalCommand.Camera.id) ||
                    of == context.resources.getString(ActionInternalCommand.Telephone.id) ||
                    of == context.resources.getString(ActionInternalCommand.GoToPage.id) ||
                    of == context.resources.getString(ActionInternalCommand.Gallery.id) ||
                    of == context.resources.getString(ActionInternalCommand.Clock.id) ||
                    of == context.resources.getString(ActionInternalCommand.Contacts.id) ||
                    of == context.resources.getString(ActionInternalCommand.Emergency.id) ||
                    of == "SOS" ||
                    of == context.resources.getString(ActionInternalCommand.Messages.id) ||
                    of == context.resources.getString(ActionInternalCommand.Settings.id) ||
                    of == context.resources.getString(ActionInternalCommand.SystemSettings.id) ||
                    of == context.resources.getString(ActionInternalCommand.Preferences.id) ||
                    of == context.resources.getString(InternalCategories.SettingsSystem.id) ||
                    of == context.resources.getString(InternalCategories.SettingsOverall.id) ||
                    of.startsWith("Settings")
//        val itemIsCategory:Boolean =
//            of == context.resources.getString(InternalCategories.SettingsSystem.id) ||
//                    of == context.resources.getString(InternalCategories.SettingsOverall.id)
//        if(itemIsInternalCommand) Log.d("GetItemData","Internal Command ${of}")
//        if(itemIsCategory) Log.d("GetItemData","Internal Category ${of}")
//        Log.d("GetItemData","Item $of requested")
        val predeterminedItem: ItemData = when {
//            itemIsCategory -> ItemData(
//                name = of,
//                label = of,
//                isCategory = true,
//                action = listOf(
//                    ActionData(
//                        name = of,
//                        action = of,
//                        type = ActionDataLaunchType.Category,
//                    )
//                ),
//            )
            itemIsInternalCommand -> ItemData(
                name = of,
                label = of,
                action = listOf(
                    ActionData(
                        name = of,
                        action = of,
                        type = ActionDataLaunchType.Internal,
                    )
                ),
            )
//            "AllApps" -> HTLauncherHardcodes.ALLAPPS_FILE
//            "Telephone" -> HTLauncherHardcodes.TELEPHONE_FILE
//            "CategorySettingsOverall" -> HTLauncherHardcodes.CATEGORY_SETTINGS_OVERALL_FILE
//            "CategorySettingsSystem" -> HTLauncherHardcodes.CATEGORY_SETTINGS_SYSTEM_FILE
            else -> ItemData(
                name = of,
                label = of,
            )
        }
        if(_uiState.value.itemList.contains(of)) return _uiState.value.itemList[of] ?: predeterminedItem

        var aItem: ItemData = predeterminedItem
        if(_uiState.value.selectedSaveDir != null && _uiState.value.selectedSaveDir.toString().isNotBlank() && !ignoreFile){
            if((_uiState.value.itemList.contains(of) && _uiState.value.itemList[of] != null) || forceReload){
                aItem = _uiState.value.itemList[of]!!
            } else {
                val selectFolder: Uri = getADirectory(
                    dirUri = _uiState.value.selectedSaveDir!!,
                    dirName = "Items",
                    context = context
                )
                val aItemUri: Uri = getATextFile(
                    dirUri = selectFolder,
                    context = context,
                    initData = json.encodeToString<ItemData>(predeterminedItem),
                    fileName = "$of.json",
                )

                aItem = json.decodeFromString<ItemData>(
                    openATextFile(
                        uri = aItemUri,
                        contentResolver = context.contentResolver,
                    )
                )
            }
            _uiState.value.itemList[of] = aItem
        } else {
//            Log.d("GetItemData","Savedir null")
        }

        return _uiState.value.itemList[of] ?: predeterminedItem
    }
    fun getIconFile(of:String, context: Context, pm:PackageManager):Uri?{
        if(uiState.value.selectedSaveDir == null) return null
        val selectMediaFolder: Uri = getADirectory(
            dirUri = _uiState.value.selectedSaveDir!!,
            dirName = context.resources.getString(R.string.medias_folder),
            context = context
        )
        try {
            return getAFile(
                dirUri = selectMediaFolder,
                fileName = of,
                context = context,
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    fun getItemIcon(of:String, json:Json = Json{
        prettyPrint = true
        encodeDefaults = true
    }, context: Context, ignoreFile:Boolean = false, forceReload:Boolean = false, pm:PackageManager): Any?{
        val target:ItemData = getItemData(
            of = of,
            json = json,
            context = context,
            ignoreFile = ignoreFile,
            forceReload = forceReload
        )
        return when(target.action[0].type){
            ActionDataLaunchType.LauncherActivity -> if(target.action[0].action.isNotBlank()) pm.getApplicationIcon(
                target.action[0].action ?: "") else R.drawable.all_apps
            ActionDataLaunchType.Activity -> if(target.action[0].action.isNotBlank()) pm.getApplicationIcon(
                target.action[0].action ?: "") else R.drawable.all_apps
            ActionDataLaunchType.Internal -> HTLauncherHardcodes.getInternalActionIcon(target.action[0].action)
            else -> R.drawable.placeholder
        }
    }
    fun getPageIcon(of: String, json: Json = Json{
        prettyPrint = true
        encodeDefaults = true
    }, context: Context, ignoreFile:Boolean = false, forceReload:Boolean = false, pm:PackageManager): Any?{
        val target:PageData = getPageData(
            of = of,
            json = json,
            context = context,
            ignoreFile = ignoreFile,
            forceReload = forceReload
        )
        return when{
            target.name == context.getString(R.string.internal_pages_settings) -> R.drawable.settings
            target.name == context.getString(R.string.settings_item_file) -> R.drawable.settings
            target.iconPath.isNotBlank() -> getIconFile(of, context, pm)
            else -> R.drawable.open_a_page
        }
    }
}