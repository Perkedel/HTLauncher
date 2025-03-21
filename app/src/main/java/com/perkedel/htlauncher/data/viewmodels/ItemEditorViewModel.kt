@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, FlowPreview::class)

package com.perkedel.htlauncher.data.viewmodels

import android.content.ClipData.Item
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Message
import android.util.Log
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.data.SearchableApps
import com.perkedel.htlauncher.enumerations.EditWhich
import com.perkedel.htlauncher.enumerations.ItemDetailPaneNavigate
import com.perkedel.htlauncher.enumerations.ItemExtraPaneNavigate
import com.perkedel.htlauncher.func.AsyncService
import com.perkedel.htlauncher.func.removeDotExtensions
import com.perkedel.htlauncher.getADirectory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ItemEditorViewModel(
    private val asyncService: AsyncService = AsyncService()
):ViewModel() {
    var uri: Uri? by mutableStateOf(null)
        private set
    var saveDirUri: Uri? by mutableStateOf(null)

    var editType: EditWhich? by mutableStateOf(null)
        private set

    var filename: String? by mutableStateOf(null)
    var rawContent: String? by mutableStateOf(null)

    // Search
    val _searchInTheWild = MutableStateFlow("")
    val searchInTheWild = _searchInTheWild.asStateFlow()
    val _isSearchingInTheWild = MutableStateFlow(false)
    val isSearchingInTheWild = _isSearchingInTheWild.asStateFlow()
    val _wildList = MutableStateFlow(listOf<String>())
    val wildList = searchInTheWild
        .debounce(1500L)
        .onEach { _isSearchingInTheWild.update { true } }
        .combine(_wildList){ text, wild ->
            if(text.isBlank()){
                wild
            } else {
                wild.filter {
                    it.contains(text)
//                    it.startsWith(text)
//                    it.endsWith(text)
//                    it == text
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _wildList.value
        )
    fun updateWildList(with:List<String>){
        _wildList.value = with
    }
    fun updateSearchInTheWild(with: String){
        _searchInTheWild.value = with
    }
    fun updateIsSearchingInTheWild(with: Boolean){
        _isSearchingInTheWild.value = with
    }

    // TypedEdit
    var filenameDisplay:String? by mutableStateOf(null)
    var itemData: ItemData? by mutableStateOf(null)
    var homeData: HomepagesWeHave? by mutableStateOf(null)
    var pageData: PageData? by mutableStateOf(null)
    var jsoning: Json? by mutableStateOf(Json{
        prettyPrint = true
        encodeDefaults = true
    })
    var actionEdit:ActionData? by mutableStateOf(null)
    var actionId:Int? by mutableStateOf(0)
    var itemDetailPaneNavigate: ItemDetailPaneNavigate? by mutableStateOf(ItemDetailPaneNavigate.Default)
    var itemExtraPaneNavigate: ItemExtraPaneNavigate? by mutableStateOf(ItemExtraPaneNavigate.Default)
    var isEditingAction:Boolean by mutableStateOf(false)

    // Error
    var errorOccured: Boolean? by mutableStateOf(false)
    var errorMessage: String? by mutableStateOf("")

//    var navigator: ThreePaneScaffoldNavigator<Any>? = null
    var hasGoBack:Boolean = false
//    var navCallback:Ca

    fun updateUri(uri: Uri?){
        this.uri = uri
    }
    fun displayFilename(filename: String?){
        this.filenameDisplay = filename
    }
    fun selectSaveDirUri(uri: Uri?){
        this.saveDirUri = uri
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

//    fun setNavigator(into:ThreePaneScaffoldNavigator<Any>){
//        this.navigator = into
//    }

    fun setGoBack(into:Boolean){
        this.hasGoBack = into
    }

    fun updateItemData(with: ItemData){
        this.itemData = with
    }
    fun updatePageData(with: PageData){
        this.pageData = with
        Log.d("UpdatePageData","I will update page data\n${this.pageData}")
    }
    fun updateHomeData(with: HomepagesWeHave){
        this.homeData = with
    }
    fun updateActionData(with: ActionData){
        this.actionEdit = with
    }
    fun updateActionDataId(with: Int = 0){
        this.actionId = with
    }
    fun selectActionPackage(with:ApplicationInfo){
        if(actionEdit != null){
            actionEdit = actionEdit?.copy(
                action = with.packageName
            ) ?: ActionData(
                action = with.packageName
            )
            appendItemDataAction(
                with = actionEdit ?: ActionData(
                    action = with.packageName
                ),
                id = actionId ?: 0
            )
        }
    }

    fun clearActionData(){
        this.actionEdit = null
    }
    fun setOpenActionData(into:Boolean = false){
        this.isEditingAction = into
    }
    fun selectExtraNavigate(whichIs: ItemExtraPaneNavigate = ItemExtraPaneNavigate.Default){
        this.itemExtraPaneNavigate = whichIs
    }
    fun selectDetailNavigate(whichIs: ItemDetailPaneNavigate = ItemDetailPaneNavigate.Default){
        this.itemDetailPaneNavigate = whichIs
    }

    fun addItemDataAction(with: ActionData = ActionData()){
        actionEdit = with
        val compile: MutableList<ActionData> = (this.itemData?.action ?: ItemData().action).toMutableList()
        compile.add(with)
        this.itemData = this.itemData?.copy(
            action = compile
        )
    }
    fun appendItemDataAction(with:ActionData = ActionData(), id:Int = 0){
        actionEdit = with
        val compile: MutableList<ActionData> = (this.itemData?.action ?: ItemData().action).toMutableList()
        compile[id] = with
        this.itemData = this.itemData?.copy(
            action = compile
        )
    }
    fun reorderItemDataAction(whichIs:ActionData = ActionData(), into:Int = 0){
        actionEdit = whichIs
        val compile: MutableList<ActionData> = (this.itemData?.action ?: ItemData().action).toMutableList()
        compile.remove(whichIs)
        compile.add(into,whichIs)
        this.itemData = this.itemData?.copy(
            action = compile
        )
    }
    fun resyncItemDataAction(){
        val compile: MutableList<ActionData> = (this.itemData?.action ?: ItemData().action).toMutableList()
        compile[actionId ?: 0] = actionEdit ?: ActionData()
        this.itemData = this.itemData?.copy(
            action = compile
        )
    }

    fun changeItemOrders(into:List<String>){
        val json = Json {
            // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
            prettyPrint = true
            encodeDefaults = true
        }
        this.pageData = this.pageData?.copy(
            items = into
        )
        this.pageData?.let {
//            updateRawContent(json.encodeToString(it))
        }

    }
    fun changePagePathInHome(into:List<String> = emptyList()){
        var compile = this.homeData?.pagesPath?.toMutableList() ?: HomepagesWeHave().pagesPath.toMutableList()
        this.homeData = this.homeData?.copy(
            pagesPath = into
        )
    }
    fun changeItemsInPage(into: List<String>){
        this.pageData = this.pageData?.copy(
            items = into
        )
    }

    fun typedEditNow(editType: EditWhich?, rawJson: String = ""){
        val json = Json {
            // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
            prettyPrint = true
            encodeDefaults = true
        }
        when(editType){
            EditWhich.Items -> updateItemData(json.decodeFromString<ItemData>(rawJson))
            EditWhich.Pages -> updatePageData(json.decodeFromString<PageData>(rawJson))
            EditWhich.Home -> updateHomeData(json.decodeFromString<HomepagesWeHave>(rawJson))
            else -> {}
        }
//        updateJsoning(json.decodeFromString(rawJson))
    }

    fun updateError(into: Boolean = false, message: String = ""){
        this.errorMessage = message.ifEmpty { this.errorMessage }
        this.errorOccured = into
    }

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
//            installAllApps(with,packageManager)
            _appAll.value = asyncService.getSearchableApps(with,packageManager)
//        }
    }
    suspend fun initializeAllAppsResolve(with:List<ResolveInfo>, packageManager: PackageManager){
//        viewModelScope.launch {
//            installAllApps(with,packageManager)
        _appAll.value = asyncService.getSearchableAppsResolve(with,packageManager)
//        }
    }
    suspend fun appendAllApps(base:List<SearchableApps>, with:List<PackageInfo>, packageManager: PackageManager){
//        viewModelScope.launch {
//            installAllApps(with,packageManager)
        _appAll.value = asyncService.appendSearchableApps(base, with,packageManager)
//        }
    }
    suspend fun appendAllAppsResolve(base:List<SearchableApps>, with:List<ResolveInfo>, packageManager: PackageManager){
//        viewModelScope.launch {
//            installAllApps(with,packageManager)
        _appAll.value = asyncService.appendSearchableAppsResolve(base, with,packageManager)
//        }
    }

    fun getItemFolderContents(saveDirUri: Uri, context:Context):List<String> {
        if(saveDirUri.toString().isNotBlank()) {
            val itemFolder: Uri = getADirectory(
                dirUri = saveDirUri,
                context = context,
                dirName = context.resources.getString(R.string.items_folder)
            )
            val itemFiles: List<DocumentFile> =
                DocumentFile.fromTreeUri(context, itemFolder)?.listFiles()?.toList() ?: emptyList()
            // https://stackoverflow.com/questions/47871538/how-to-get-a-filename-without-extension-in-kotlin
            // https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/replace-after.html
            // https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/replace-after.html
//            Log.d("GetItemContent","Item File contents: ${itemFiles.map { it.name?.substring(0, it.name?.lastIndexOf('.') ?: 0) ?: "" }}")
            Log.d("GetItemContent","Item File contents: ${itemFiles.map { removeDotExtensions(it.name ?: "") }}")
//            return itemFiles.map { it.name?.replaceAfter(".json","") ?: "" }.toList()
//            return itemFiles.map {  it.name?.substring(0, it.name?.lastIndexOf('.') ?: 0) ?: "" }.toList()
            return itemFiles.map {  removeDotExtensions(it.name ?: "") }.toList()
        } else return emptyList()
    }
    fun getPageFolderContents(saveDirUri: Uri, context:Context):List<String> {
        if(saveDirUri.toString().isNotBlank()) {
            val pageFolder: Uri = getADirectory(
                dirUri = saveDirUri,
                context = context,
                dirName = context.resources.getString(R.string.pages_folder)
            )
            val pageFiles: List<DocumentFile> =
                DocumentFile.fromTreeUri(context, pageFolder)?.listFiles()?.toList() ?: emptyList()
//            return pageFiles.map { it.name?.substring(0, it.name?.lastIndexOf('.') ?: 0) ?: "" }.toList()
//            return pageFiles.map { it.name?.replaceAfterLast(".json", "") ?: "" }.toList()
            Log.d("GetPageFolderContent","Page Folder: ${pageFiles.map { removeDotExtensions(it.name ?: "") }.toList()}")
            return pageFiles.map { removeDotExtensions(it.name ?: "") }.toList()
        } else return emptyList()
    }
}