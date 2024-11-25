@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class,
)

package com.perkedel.htlauncher.ui.activities

import android.content.ClipData.Item
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHost
import coil3.Uri as CoilUri
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.viewModels
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.enumerations.EditWhich
import com.perkedel.htlauncher.openATextFile
import com.perkedel.htlauncher.ui.navigation.EditItemData
import kotlinx.serialization.json.Json
import javax.annotation.meta.When
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.ui.dialog.HTAlertDialog
import com.perkedel.htlauncher.widgets.HTButton
import com.perkedel.htlauncher.writeATextFile
import kotlinx.serialization.encodeToString
import okio.IOException

class ItemEditorActivity : ComponentActivity() {

    private val editorViewModel by viewModels<ItemEditorViewModel>()
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
        if(editorViewModel.uri == null) {
            val b:Bundle? = intent.extras
            val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("uri", Uri::class.java)
            } else {
                intent.getParcelableExtra("uri")
            }
            val editType: EditWhich? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("editType", EditWhich::class.java)
//                intent.extras?.getParcelable("editType", EditWhich::class.java)
            } else {
//                intent.getParcelableExtra("editType")
                intent.extras?.getParcelable("editType")
            }
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

fun saveThisFile(saveUri:Uri, context: Context, contentResolver: ContentResolver, itemType: EditWhich, content:String = "{}", viewModel: ItemEditorViewModel,){
    try{
    writeATextFile(
        uri = saveUri,
        contentResolver = contentResolver,
        with = content
    )
        Toast.makeText(context,context.resources.getString(R.string.save_success),Toast.LENGTH_SHORT).show()
    } catch (e:Exception){
        e.printStackTrace()
        viewModel.updateError(
            into = true,
            message = e.localizedMessage ?: context.resources.getString(R.string.error_unknown_reason)
        )
        Toast.makeText(context,context.resources.getString(R.string.save_error),Toast.LENGTH_SHORT).show()
    } catch (e:IOException){
        e.printStackTrace()
        viewModel.updateError(
            into = true,
            message = e.localizedMessage ?: context.resources.getString(R.string.error_unknown_reason)
        )
        Toast.makeText(context,context.resources.getString(R.string.save_error),Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ItemEditorGreeting(
    modifier: Modifier = Modifier,
    context:Context = LocalContext.current,
    uiState:HTUIState = HTUIState(),
    viewModel: ItemEditorViewModel = ItemEditorViewModel(),
    editUri:Uri? = null,
    navigator:ThreePaneScaffoldNavigator<Any> = rememberListDetailPaneScaffoldNavigator<Any>(),
    onBack:()-> Unit = {},
    onSave:(Uri,ContentResolver,EditWhich, String)-> Unit = { uri, resolver, editType, content ->
        saveThisFile(
            saveUri = uri,
            context = context,
            contentResolver = resolver,
            itemType = editType,
            content = content,
            viewModel = viewModel
        )
    },
    json: Json = Json {
        // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
        prettyPrint = true
        encodeDefaults = true
    },
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
    }

    var backProgress: Float? by remember {
        mutableStateOf(null)
    }

    val onBackPressedCallback:OnBackPressedCallback = object :OnBackPressedCallback(true){
        // https://github.com/philipplackner/PredictiveBackMigration
        override fun handleOnBackPressed() {
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
    val saveNow: () -> Unit = {
        if(editUri != null) {
            viewModel.updateRawContent(
                when(viewModel.editType){
                    EditWhich.Items -> json.encodeToString<ItemData>(viewModel.itemData ?: ItemData())
                    EditWhich.Pages -> json.encodeToString<PageData>(viewModel.pageData ?: PageData())
                    EditWhich.Home -> json.encodeToString<HomepagesWeHave>(viewModel.homeData ?: HomepagesWeHave())
                    else -> "{}"
                }
            )
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
            onSave(editUri,context.contentResolver,viewModel.editType ?: EditWhich.Misc, viewModel.rawContent ?: "")
//                                onSave(editUri,context.contentResolver,viewModel.editType ?: EditWhich.Misc, when(viewModel.editType){
//                                    EditWhich.Items -> json.encodeToString<ItemData>(value = viewModel.itemData ?: ItemData())
//                                    else -> "{}"
//                                })
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HTAppBar(
                textTitle = "Edito",
                textDescription = "(${viewModel.editType?.toString()}) ${viewModel.uri?.toString()}",
                canNavigateBack = true,
                navigateUp = {
                    onBack()
                    if(navigator.canNavigateBack()){
                        navigator.navigateBack()
                    }
                },
                onMoreMenu = {

                },
                actions = {
                    IconButton(
                        onClick = saveNow,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = stringResource(R.string.action_save)
                        )
                    }
                }
            )
        },
    ){ innerPadding ->
        NavigableListDetailPaneScaffold(
            modifier = modifier.padding(innerPadding),
            navigator = navigator,
            listPane = {
                when(viewModel.editType){
                    EditWhich.Items -> EditItemData(
                        modifier = Modifier,
                        viewModel = viewModel,
                        data = viewModel.itemData
                    )
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
                AnimatedPane {
                    Text(content)
                }
            },
            extraPane = {
                val content = navigator.currentDestination?.content?.toString() ?: "aaa"
                AnimatedPane {

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