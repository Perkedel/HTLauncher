@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3AdaptiveApi::class,
)

package com.perkedel.htlauncher.ui.activities

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import android.net.Uri
import android.util.Log
import android.view.SoundEffectConstants
import android.view.View
import android.widget.Toast
import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.viewModels
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.enumerations.EditWhich
import com.perkedel.htlauncher.openATextFile
import com.perkedel.htlauncher.ui.navigation.EditItemData
import kotlinx.serialization.json.Json
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.enumerations.ItemDetailPaneNavigate
import com.perkedel.htlauncher.enumerations.ItemExtraPaneNavigate
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.ui.dialog.HTAlertDialog
import com.perkedel.htlauncher.ui.navigation.ActionSelectApp
import com.perkedel.htlauncher.ui.navigation.AddIntoTheListOf
import com.perkedel.htlauncher.ui.navigation.EditActionData
import com.perkedel.htlauncher.ui.navigation.EditHomeData
import com.perkedel.htlauncher.ui.navigation.EditHomePageOrders
import com.perkedel.htlauncher.ui.navigation.EditPageData
import com.perkedel.htlauncher.ui.navigation.EditPageItems
import com.perkedel.htlauncher.ui.page.BasePage
import com.perkedel.htlauncher.widgets.HTButton
import com.perkedel.htlauncher.widgets.ItemCell
import com.perkedel.htlauncher.writeATextFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import okio.IOException

class ItemEditorActivity : ComponentActivity() {

    private val editorViewModel by viewModels<ItemEditorViewModel>()
    private val htViewModel by viewModels<HTViewModel>()
//    private val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
//    private val navigator: ThreePaneScaffoldNavigator<Any>


    override fun onCreate(savedInstanceState: Bundle?) {
        // https://stackoverflow.com/questions/10107442/android-how-to-pass-parcelable-object-to-intent-and-use-getparcelable-method-of
        // https://stackoverflow.com/questions/58687179/how-to-pass-data-between-activity-using-parcelable-in-android-studio
        // https://developer.android.com/kotlin/parcelize
        // https://androidhub.wordpress.com/2011/08/03/android-intents-for-passing-data-between-activities-part-3/
        // https://stackoverflow.com/questions/39463631/java-util-arraylist-cannot-be-cast-to-android-os-parcelable-key-expected-parcel
        // https://developer.android.com/games/guides

        super.onCreate(savedInstanceState)
//        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        val b:Bundle? = intent.extras
        val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("uri", Uri::class.java)
        } else {
            intent.getParcelableExtra("uri")
        }
        val saveUri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("saveDirUri", Uri::class.java)
        } else {
            intent.getParcelableExtra("saveDirUri")
        }
        if(editorViewModel.uri == null) {
//            val editType: EditWhich? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                intent.getParcelableExtra("editType", EditWhich::class.java)
////                intent.extras?.getParcelable("editType", EditWhich::class.java)
//            } else {
////                intent.getParcelableExtra("editType")
//                intent.extras?.getParcelable("editType")
//            }
//            val editTypeName: String? = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                intent.getParcelableExtra("editTypeName", String::class.java)
//            } else {
//                intent.getParcelableExtra("editTypeName")
//            }) as? String
//            val fileName: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                intent.getParcelableExtra("filename", String::class.java)
//            } else {
//                intent.getParcelableExtra("filename")
//            }
            val editTypeName: String? = b?.getString("editTypeName")
            val fileName: String? = b?.getString("filename")
//            val content:String = if(uri != null) openATextFile(
//                uri = uri,
//                contentResolver = applicationContext.contentResolver
//            ) else ""
            editorViewModel.updateUri(uri)
//            editorViewModel.updateEditType(editType)
            editorViewModel.updateEditType(when(editTypeName){
                applicationContext.resources.getString(R.string.home_screen_file) -> EditWhich.Home
                "Home" -> EditWhich.Home
                "home" -> EditWhich.Home
                applicationContext.resources.getString(R.string.items_folder) -> EditWhich.Items
                applicationContext.resources.getString(R.string.pages_folder) -> EditWhich.Pages
                applicationContext.resources.getString(R.string.medias_folder) -> EditWhich.Medias
                applicationContext.resources.getString(R.string.themes_folder) -> EditWhich.Themes
                applicationContext.resources.getString(R.string.shortcuts_folder) -> EditWhich.Shortcuts
                else -> EditWhich.Misc
            })
//            editorViewModel.typedEditNow(editorViewModel.editType)
//            editorViewModel.updateRawContent(content)
//            editorViewModel.typedEditNow(editType = editType, rawJson = content)
//            editorViewModel.updateFilename(fileName)
//            Log.d("ItemEditor", "Obtained New Uri awa ${uri} which is ${editTypeName}")
//            Log.d("ItemEditor", "Obtained New Uri awa ${fileName} ${uri} which is ${editType}")
            Log.d("ItemEditor", "Obtained New Uri awa ${fileName} ${uri} which is ${editTypeName}")
        }
        if(editorViewModel.saveDirUri == null){
            editorViewModel.selectSaveDirUri(saveUri)
            htViewModel.selectSaveDirUri(editorViewModel.saveDirUri)
            Log.d("ItemEditor", "Obtained Save Dir Uri ${editorViewModel.saveDirUri}")
        }
        Log.d("ItemEditor", "Welcome to Edit File")
        enableEdgeToEdge()

        setContent {
            HTLauncherTheme {
                Surface(
                    color = rememberColorScheme().background
                ) {
                    ItemEditorGreeting(
                        modifier = Modifier.fillMaxSize(),
                        context = applicationContext,
                        viewModel = editorViewModel,
                        htViewModel = htViewModel,
                        editUri = editorViewModel.uri,
                        onBack = {
                            pressBackButton()
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri:Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM,Uri::class.java)
        } else {
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }
        Log.d("ItemEditor", "Obtained New Uri ${uri}")
        editorViewModel.updateUri(uri)
    }

    // https://stackoverflow.com/a/50651129/9079640
    // https://youtu.be/2cOeUF2rZnE
//    override fun onBackPressed() {
//        super.onBackPressed()
//    }

    fun pressBackButton(){
        // TODO: Save file automatically
//        if(editorViewModel.ha == null && !(editorViewModel.navigator?.navigateBack()!!)){
        if(!editorViewModel.hasGoBack){
            finish()
        } else {
            Log.d("ItemEditor", "Still has back")
        }
    }

    val onBackPressedCallback:OnBackPressedCallback = object :OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            pressBackButton()
        }

        override fun handleOnBackProgressed(backEvent: BackEventCompat) {
            super.handleOnBackProgressed(backEvent)
        }

        override fun handleOnBackCancelled() {
            super.handleOnBackCancelled()
        }
    }
}

fun soPressBack(nav:ThreePaneScaffoldNavigator<Any>){
    if(!nav.canNavigateBack()){
//        finis
    } else {
        nav.navigateBack()
    }
}

fun saveThisFile(
    saveUri: Uri,
    context: Context,
    contentResolver: ContentResolver,
    itemType: EditWhich,
    content: String = "{}",
    viewModel: ItemEditorViewModel,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    try {
        writeATextFile(
            uri = saveUri,
            contentResolver = contentResolver,
            with = content
        )
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = context.resources.getString(R.string.save_success),
            )
        }
//        Toast.makeText(context,context.resources.getString(R.string.save_success),Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        viewModel.updateError(
            into = true,
            message = e.localizedMessage
                ?: context.resources.getString(R.string.error_unknown_reason)
        )
        Toast.makeText(
            context,
            context.resources.getString(R.string.save_error),
            Toast.LENGTH_SHORT
        ).show()
    } catch (e: IOException) {
        e.printStackTrace()
        viewModel.updateError(
            into = true,
            message = e.localizedMessage
                ?: context.resources.getString(R.string.error_unknown_reason)
        )
        Toast.makeText(
            context,
            context.resources.getString(R.string.save_error),
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
fun ItemEditorGreeting(
    modifier: Modifier = Modifier,
    context:Context = LocalContext.current,
    view: View = LocalView.current,
    viewModel: ItemEditorViewModel = ItemEditorViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    htViewModel: HTViewModel = HTViewModel(),
//    uiState:HTUIState = htViewModel.uiState,
    editUri:Uri? = null,
    navigator:ThreePaneScaffoldNavigator<Any> = rememberListDetailPaneScaffoldNavigator<Any>(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBack:()-> Unit = {
    },
    onSave:(Uri,ContentResolver,EditWhich, String)-> Unit = { uri, resolver, editType, content ->
        view.playSoundEffect(SoundEffectConstants.CLICK)
        saveThisFile(
            saveUri = uri,
            context = context,
            contentResolver = resolver,
            itemType = editType,
            content = content,
            viewModel = viewModel,
            coroutineScope = coroutineScope,
            snackbarHostState = snackbarHostState,
        )
    },
    json: Json = Json {
        // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
        prettyPrint = true
        encodeDefaults = true
    },
    windowInfo: WindowInfo = rememberWindowInfo(),
    configuration: Configuration = LocalConfiguration.current,
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
) {
    // https://youtu.be/W3R_ETKMj0E Philip Lackner List detail
    // https://github.com/philipplackner/ListPaneScaffoldGuide
    // https://github.com/philipplackner/ListPaneScaffoldGuide/blob/master/app/src/main/java/com/plcoding/listpanescaffoldguide/MainActivity.kt
    // https://youtu.be/rJkQzGDUqAQ
    // https://youtu.be/2hIY1xuImuQ
    // https://youtu.be/2hIY1xuImuQ Philip Lackner intent basic
    // https://youtu.be/SJw3Nu_h8kk Philip Lackner Activity Lifecycle
    // https://youtu.be/doGsRC2J1Fc Stevdza-San argument navigation
    // https://youtu.be/h61Wqy3qcKg Philip Lackner share data between screen
    // https://youtu.be/Q3iZyW2etm4 Where is it?!
    // https://youtu.be/NhoV78E6yWo
    // https://developer.android.com/develop/ui/compose/animation/composables-modifiers#animatedcontent
    // https://developer.android.com/develop/ui/compose/animation/composables-modifiers#animatedvisibility
    // https://developer.android.com/develop/ui/compose/animation/shared-elements
    // https://developer.android.com/develop/ui/compose/components/progress
    // https://developer.android.com/guide/navigation/backstack
    // https://developer.android.com/develop/ui/compose/layouts/adaptive/list-detail
    // https://stackoverflow.com/questions/69192042/how-to-use-jetpack-compose-app-bar-backbutton
    // https://oguzhanaslann.medium.com/handling-back-presses-in-jetpack-compose-and-onbackinvokedcallback-982e805173f0
    // https://stackoverflow.com/questions/30231072/popbackstack-finishes-activity-in-onbackpressed
    // https://developer.android.com/develop/ui/compose/layouts/adaptive/list-detail
    // https://stackoverflow.com/questions/68560948/how-to-handle-back-navigation-with-jetpack-compose-navigation-without-fragmen
//    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    // DONE: read file & type
//    viewModel.setNavigator(navigator)
    LaunchedEffect(
//        key1 = viewModel.homeData,
//        key2 = viewModel.pageData,
//        key3 = viewModel.itemData,
        key1 = editUri,
    ) {
        Log.d("ItemEditor", "Numer ${editUri?.toString()} which is ${viewModel.editType}")
        if(editUri != null){
            viewModel.updateRawContent(openATextFile(
                uri = viewModel.uri!!,
                contentResolver = context.contentResolver
            ))
            if(viewModel.rawContent != null && viewModel.rawContent!!.isNotEmpty()){
//            viewModel.updateJsoning(Json.decodeFromString(viewModel.rawContent!!))
                viewModel.typedEditNow(viewModel.editType,viewModel.rawContent!!)
            }
//            viewModel.displayFilename(viewModel.uri?.lastPathSegment) // HOW DID YOU KNOW, CODEIUM?!
            // nvm, you lastPathSegment succ! it shows whole path!!!
            viewModel.displayFilename(when(viewModel.editType){
                EditWhich.Home -> context.resources.getString(R.string.home_screen_file)
                EditWhich.Items -> viewModel.itemData?.name
                EditWhich.Pages -> viewModel.pageData?.name
                else -> viewModel.uri?.lastPathSegment
            })
        }
    }


    val saveNow: () -> Unit = {
        if(editUri != null) {
            var toSave:String = when(viewModel.editType){
                EditWhich.Items -> json.encodeToString<ItemData>(viewModel.itemData ?: ItemData())
                EditWhich.Pages -> json.encodeToString<PageData>(viewModel.pageData ?: PageData())
                EditWhich.Home -> json.encodeToString<HomepagesWeHave>(viewModel.homeData ?: HomepagesWeHave())
                else -> "{}"
            }
            toSave = toSave.substring(0,toSave.lastIndexOf('}')) + '}' // save file bug double write
//            viewModel.updateRawContent(
//
//            )
//                                viewModel.updateRawContent(json.encodeToString(value = when(viewModel.editType){
//                                    EditWhich.Items -> viewModel.itemData
//                                    EditWhich.Pages -> viewModel.pageData
//                                    EditWhich.Home -> viewModel.homeData
//                                    else -> "{}"
////                                    EditWhich.Themes -> TODO()
////                                    EditWhich.Medias -> TODO()
////                                    EditWhich.Shortcuts -> TODO()
////                                    EditWhich.Misc -> TODO()
////                                    null -> TODO()
//                                }))
//            onSave(editUri,context.contentResolver,viewModel.editType ?: EditWhich.Misc, viewModel.rawContent ?: "")
            onSave(editUri,context.contentResolver,viewModel.editType ?: EditWhich.Misc, toSave)
//                                onSave(editUri,context.contentResolver,viewModel.editType ?: EditWhich.Misc, when(viewModel.editType){
//                                    EditWhich.Items -> json.encodeToString<ItemData>(value = viewModel.itemData ?: ItemData())
//                                    else -> "{}"
//                                })
        }
    }

    var backProgress: Float? by remember {
        mutableStateOf(null)
    }

    val onBackPressedCallback:OnBackPressedCallback = object :OnBackPressedCallback(true){
        // https://github.com/philipplackner/PredictiveBackMigration
        override fun handleOnBackPressed() {
//            saveNow()
//            pressBackButton()
            if(navigator.canNavigateBack()){
                navigator.navigateBack()
            } else {
                onBack()
            }
            backProgress = null
        }

        override fun handleOnBackProgressed(backEvent: BackEventCompat) {
            super.handleOnBackProgressed(backEvent)
            backProgress = backEvent.progress
        }

        override fun handleOnBackCancelled() {
            super.handleOnBackCancelled()
            backProgress = null
        }
    }

    viewModel.setGoBack(navigator.canNavigateBack())
//    BackHandler(navigator.canNavigateBack()) {
//        navigator.navigateBack()
//    }
//    BackHandler {
//        onBack()
//    }
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

    DisposableEffect(backPressedDispatcher, navigator) {
        if(navigator.canNavigateBack()){
            backPressedDispatcher.addCallback(onBackPressedCallback)
//            navigator.navigateBack()
        }

        onDispose {
            onBackPressedCallback.remove()
        }
    }
    val onBacking: () -> Unit = {

    }
    val rebuildItem: ()-> Unit = {

    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HTAppBar(
                textTitle = viewModel.filenameDisplay ?: "EDIT",
                textDescription = "(${viewModel.editType?.toString()}) ${viewModel.uri?.toString()}",
                canNavigateBack = true,
                navigateUp = {
                    saveNow()
                    if(navigator.canNavigateBack()){
                        navigator.navigateBack()
                    } else {
                        onBack()
                    }
                },
                onMoreMenu = {

                },
                actions = {
                    // https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/samples/src/main/java/androidx/compose/material3/samples/TooltipSamples.kt
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                        tooltip = {PlainTooltip { Text(stringResource(R.string.action_save)) }},
                        state = rememberTooltipState(),
                    ) {
                        IconButton(
                            onClick = saveNow,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = stringResource(R.string.action_save)
                            )
                        }
                    }

                }
            )
        },
        snackbarHost = {
            // https://developer.android.com/develop/ui/compose/components/snackbar
            // https://youtu.be/_yON9d9if6g?si=l5HVadsldckAuErk
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ){ innerPadding ->
        NavigableListDetailPaneScaffold(
            modifier = modifier.padding(innerPadding),
            navigator = navigator,
            listPane = {
                when(viewModel.editType){
                    EditWhich.Items -> EditItemData(
                        modifier = Modifier,
                        viewModel = viewModel,
                        data = viewModel.itemData,
                        onEditActionData = { actionData, idOf ->
                            viewModel.updateActionDataId(idOf)
                            viewModel.updateActionData(actionData)
                            viewModel.selectDetailNavigate(ItemDetailPaneNavigate.EditingAction)
                            viewModel.setOpenActionData(true)
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                content = "Action ${idOf} ${viewModel.actionEdit}"
                            )
                        },
                        onRebuildItem = {
                            viewModel.updateItemData(it)
                            viewModel.updateRawContent(json.encodeToString<ItemData>(it))
                        },
                        snackbarHostState = snackbarHostState,
                    )
                    EditWhich.Pages -> EditPageData(
                        modifier = Modifier,
                        viewModel = viewModel,
                        data = viewModel.pageData,
                        onRebuildItem = {
                            viewModel.updatePageData(it)
//                            viewModel.updateRawContent(json.encodeToString<PageData>(it))
                        },
                        onSelectedKey = {
                            when(it){
                                "reorder_items" -> {
                                    viewModel.selectDetailNavigate(ItemDetailPaneNavigate.ReorderItems)
                                    navigator.navigateTo(
                                        pane = ListDetailPaneScaffoldRole.Detail,
                                        content = "Reorder Items"
                                    )
                                }
                                else -> {}
                            }
                        }
                    )
                    EditWhich.Home -> {
                        EditHomeData(
                            modifier = Modifier,
                            viewModel = viewModel,
                            data = viewModel.homeData,
                            onRebuildItem = {
                                viewModel.updateHomeData(it)
//                                viewModel.updateRawContent(json.encodeToString<HomepagesWeHave>(it))
                            },
                            onSelectedKey = {
                                when(it) {
                                    "reorder_pages" -> {
                                        viewModel.selectDetailNavigate(ItemDetailPaneNavigate.ReorderPages)
                                        navigator.navigateTo(
                                            pane = ListDetailPaneScaffoldRole.Detail,
                                            content = "Reorder Pages"
                                        )
                                    }
                                    else -> {}
                                }
                            },
                            snackbarHostState = snackbarHostState
                        )
                    }
                    else -> {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                            ,
                        ) {
//                Text("FILE!! ${viewModel.uri}")
                            Text("FILE!! ${editUri}")
//                viewModel.uri?.let {
                            editUri?.let {
//                    viewModel.updateRawContent(openATextFile(
//                        uri = viewModel.uri!!,
//                        contentResolver = context.contentResolver
//                    ))
                                Text("Contain:\n${viewModel.rawContent}")

                            }
                            HTButton(
                                modifier = Modifier,
                                title = "Hello",
                                onClick = {
                                    navigator.navigateTo(
                                        pane = ListDetailPaneScaffoldRole.Detail,
                                        content = "Item aaa ${viewModel.rawContent}"
                                    )
                                }
                            )
                        }
                    }
                }

            },
            detailPane = {
                val content = navigator.currentDestination?.content?.toString() ?: "idk"
//                val content: Any? = navigator.currentDestination?.content? ?: {
//                    when{
//                        viewModel.editType == EditWhich.Items -> {
//
//                        }
//                        else -> {}
//                    }
//                }
                AnimatedPane {
//
                    Surface(
//                        color = rememberColorScheme().inversePrimary
                    ) {
                        Box(
//                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            when{
                                viewModel.isEditingAction || viewModel.itemDetailPaneNavigate == ItemDetailPaneNavigate.EditingAction -> {
                                    EditActionData(
                                        modifier = Modifier.fillMaxSize(),
                                        data = viewModel.actionEdit,
                                        id = viewModel.actionId ?: 0,
                                        onRebuild = { actioning:ActionData,idOf:Int ->
                                            viewModel.appendItemDataAction(actioning,idOf)
                                            var obtainItemData:ItemData? = viewModel.itemData
                                            var obtainActionData:ActionData? = viewModel.actionEdit
                                            obtainItemData?.let {
//                                                obtainItemData = obtainItemData!!.copy(
//                                                    action = actioning
//                                                )
//                                                viewModel.updateRawContent(json.encodeToString<ItemData>(
//                                                    obtainItemData!!
//                                                ))
                                            }

//                                            viewModel.resyncItemDataAction()
//                                            saveNow()
                                        },
                                        onClose = {
                                            saveNow()
//                                            viewModel.resyncItemDataAction()
                                            viewModel.clearActionData()
                                            viewModel.setOpenActionData(false)
                                            Log.d("ItemEditorActivity","Total Action${viewModel.itemData?.action}")
                                            viewModel.selectDetailNavigate(ItemDetailPaneNavigate.Default)
                                            navigator.navigateBack()
                                        },
                                        onSelectAction = {
                                            viewModel.selectExtraNavigate(ItemExtraPaneNavigate.SelectApp)
                                            navigator.navigateTo(
                                                pane = ListDetailPaneScaffoldRole.Extra,
                                                content = "select action aaa ${viewModel.rawContent}"
                                            )
                                        },
                                        snackbarHostState = snackbarHostState,
                                    )
                                }
                                viewModel.itemDetailPaneNavigate == ItemDetailPaneNavigate.ReorderItems -> {
                                    EditPageItems(
                                        modifier = Modifier.fillMaxSize(),
                                        data = viewModel.pageData,
                                        onClose = {
                                            saveNow()
                                            viewModel.selectDetailNavigate(ItemDetailPaneNavigate.Default)
                                            navigator.navigateBack()
                                        },
                                        onSwap = {
//                                            Log.d("SwapPageItems","Here now list\n ${it}")
//                                            viewModel.changeItemOrders(it)
//                                            viewModel.pageData?.let {
//                                                Log.d("SwapPageItems","Here now page\n ${it}")
//                                                viewModel.updateRawContent(json.encodeToString<PageData>(
//                                                    viewModel.pageData!!
//                                                ))
//                                            }
                                        },
                                        onRebuild = {
                                            Log.d("RebuildPageData","Please rebuild:\n$it")
                                            viewModel.updatePageData(it)
//                                            viewModel.updateRawContent(json.encodeToString<PageData>(it))
                                        },
                                        onTryAdd = {
                                            viewModel.selectExtraNavigate(ItemExtraPaneNavigate.AddItem)
                                            navigator.navigateTo(
                                                pane = ListDetailPaneScaffoldRole.Extra,
                                                content = "Add Item to Page"
                                            )
                                        },
                                        snackbarHostState = snackbarHostState,
                                    )
                                }
                                viewModel.itemDetailPaneNavigate == ItemDetailPaneNavigate.ReorderPages -> {
                                    EditHomePageOrders(
                                        modifier = Modifier.fillMaxSize(),
                                        data = viewModel.homeData,
                                        onClose = {
                                            saveNow()
                                            viewModel.selectDetailNavigate(ItemDetailPaneNavigate.Default)
                                            navigator.navigateBack()
                                        },
                                        onSwap = {
//                                            viewModel.changePageOrders(it)
                                        },
                                        onRebuild = {
                                            Log.d("RebuildHomeData","Please rebuild:\n$it")
                                            viewModel.updateHomeData(it)
//                                            viewModel.updateRawContent(json.encodeToString<HomepagesWeHave>(it))
                                        },
                                        onTryAdd = {
                                            viewModel.selectExtraNavigate(ItemExtraPaneNavigate.AddItem)
                                            navigator.navigateTo(
                                                pane = ListDetailPaneScaffoldRole.Extra,
                                                content = "Add Page to Home"
                                            )
                                        },
                                        snackbarHostState = snackbarHostState,
                                    )
                                }
                                viewModel.editType == EditWhich.Items -> {
                                    Card(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        ItemCell(
                                            readTheItemData = viewModel.itemData ?: ItemData()
                                        )
                                    }
                                }
                                viewModel.editType == EditWhich.Pages -> {
                                    Card(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        BasePage(
                                            pageData = viewModel.pageData,
                                        )
                                    }
                                }
                                else -> {
                                    Text(content)
                                }
                            }
                        }
                    }

                }
            },
            extraPane = {
                val content = navigator.currentDestination?.content?.toString() ?: "aaa"
                AnimatedPane {
                    when{
                        viewModel.itemExtraPaneNavigate == ItemExtraPaneNavigate.SelectApp -> {
                            ActionSelectApp(
//                                modifier = Modifier.fillMaxSize(),
                                viewModel = viewModel,
                                onSelectedApp = {
                                    viewModel.selectActionPackage(it)
                                    saveNow()
                                    viewModel.selectExtraNavigate(ItemExtraPaneNavigate.Default)
//                                    viewModel.resyncItemDataAction()
                                    navigator.navigateBack()
                                },
                                snackbarHostState = snackbarHostState,
                            )
                        }
                        viewModel.itemExtraPaneNavigate == ItemExtraPaneNavigate.AddItem -> {
                            AddIntoTheListOf(
                                modifier = Modifier.fillMaxSize(),
                                viewModel = viewModel,
                                pageData = viewModel.pageData,
                                itemData = viewModel.itemData,
                                data = when(viewModel.editType){
                                    EditWhich.Home -> {
                                        // view all things in Page folder
                                        Log.d("SelectAddIntoList","Home Edit add the file! Save ${viewModel.saveDirUri}")
                                        viewModel.getPageFolderContents(
                                            saveDirUri = viewModel.saveDirUri ?: Uri.EMPTY,
                                            context = context,
                                        )
                                    }
                                    EditWhich.Pages -> {
                                        // view all things in Page folder
                                        Log.d("SelectAddIntoList","Page Edit add the file! Save ${viewModel.saveDirUri}")
                                        viewModel.getItemFolderContents(
                                            saveDirUri = viewModel.saveDirUri ?: Uri.EMPTY,
                                            context = context,
                                        )
                                    }
                                    else -> emptyList()
                                },
                                homepagesWeHave = viewModel.homeData,
                                addIntoWhich = viewModel.editType,
                                onSelectThing = { name, which, overwrite ->
                                    Log.d("SelectAddIntoList","Name = $name, which = $which, overwrite = $overwrite")
                                    when(which){
                                        EditWhich.Home -> {
                                            viewModel.changePagePathInHome(overwrite)
                                        }
                                        EditWhich.Pages -> {
                                            viewModel.changeItemsInPage(overwrite)
//                                            viewModel.updateRawContent(json.encodeToString<PageData>(it))
                                        }
                                        else -> {}
                                    }
                                    viewModel.selectExtraNavigate(ItemExtraPaneNavigate.Default)
                                    navigator.navigateBack()
                                },
                                pm = context.packageManager,
                                snackbarHostState = snackbarHostState,
                            )
                        }
                        else -> {
                            Text(content)
                        }
                    }
                }
            }
        )

        if(viewModel.errorOccured == true){
            HTAlertDialog(
                modifier = Modifier,
                context = context,
                title = stringResource(R.string.save_error),
                text = stringResource(R.string.save_error_description),
                onDismissRequest = {
                    viewModel.updateError(false)
                },
                onConfirm = {
                    viewModel.updateError(false)
                    saveNow()
                },
                confirmText = stringResource(R.string.action_retry)
            ){
                Card(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        text = viewModel.errorMessage ?: "",
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }

}

@HTPreviewAnnotations
@Composable
fun GreetingPreview2() {
    HTLauncherTheme {
        ItemEditorGreeting()
    }
}