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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.openATextFile

class ItemEditorActivity : ComponentActivity() {

    private val editorViewModel by viewModels<ItemEditorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getStringExtra("filename")
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
                                title = {
                                    Text("Edito")
                                },
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
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    // TODO: read file & type
    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            Column {
                Text("FILE!! ${viewModel.uri}")
                Text("FILE!! ${editUri}")
//                viewModel.uri?.let {
                editUri?.let {
                    Text("Contain:\n${openATextFile(
                        uri = viewModel.uri!!,
                        contentResolver = context.contentResolver
                    )}")
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