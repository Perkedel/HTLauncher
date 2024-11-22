@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class,
)

package com.perkedel.htlauncher.ui.activities

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
import androidx.activity.viewModels
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.enumerations.EditWhich
import com.perkedel.htlauncher.openATextFile
import kotlinx.serialization.json.Json
import javax.annotation.meta.When

class ItemEditorActivity : ComponentActivity() {

    private val editorViewModel by viewModels<ItemEditorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        // https://stackoverflow.com/questions/10107442/android-how-to-pass-parcelable-object-to-intent-and-use-getparcelable-method-of
        // https://stackoverflow.com/questions/58687179/how-to-pass-data-between-activity-using-parcelable-in-android-studio
        // https://developer.android.com/kotlin/parcelize
        // https://androidhub.wordpress.com/2011/08/03/android-intents-for-passing-data-between-activities-part-3/
        // https://stackoverflow.com/questions/39463631/java-util-arraylist-cannot-be-cast-to-android-os-parcelable-key-expected-parcel
        // https://developer.android.com/games/guides

        super.onCreate(savedInstanceState)
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
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            HTAppBar(
                                textTitle = "Edito",
                                textDescription = "(${editorViewModel.editType?.toString()}) ${editorViewModel.uri?.toString()}",
                                canNavigateBack = true,
                            )
                        },
                    ) { innerPadding ->
                        ItemEditorGreeting(
                            modifier = Modifier.padding(innerPadding),
                            context = applicationContext,
                            viewModel = editorViewModel,
                            editUri = editorViewModel.uri,
                        )
                    }
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
}

@Composable
fun ItemEditorGreeting(
    modifier: Modifier = Modifier,
    context:Context = LocalContext.current,
    uiState:HTUIState = HTUIState(),
    viewModel: ItemEditorViewModel = ItemEditorViewModel(),
    editUri:Uri? = null,
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
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    // TODO: read file & type

    Log.d("ItemEditor", "Numer ${editUri?.toString()} which is ${viewModel.editType}")
    if(editUri != null){
        viewModel.updateRawContent(openATextFile(
            uri = viewModel.uri!!,
            contentResolver = context.contentResolver
        ))
        if(viewModel.rawContent != null && viewModel.rawContent!!.isNotEmpty()){
//            viewModel.updateJsoning(Json.decodeFromString(viewModel.rawContent!!))
        }
    }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {

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
            }
        },
        detailPane = {
            val content = navigator.currentDestination?.content?.toString() ?: "idk"
            AnimatedPane {

            }
        },
        extraPane = {
            val content = navigator.currentDestination?.content?.toString() ?: "aaa"
            AnimatedPane {

            }
        }
    )
}



@HTPreviewAnnotations
@Composable
fun GreetingPreview2() {
    HTLauncherTheme {
        ItemEditorGreeting()
    }
}