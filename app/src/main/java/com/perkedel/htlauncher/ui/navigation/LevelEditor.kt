package com.perkedel.htlauncher.ui.navigation

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.ShapeLine
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.enumerations.EditWhich
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.widgets.OptionItemCard
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.preference
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.LazyVerticalGridScrollbar

@Composable
fun LevelEditor(
    navController: NavController = rememberNavController(),
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    onSelectedSaveDir: (Uri) -> Unit = {  },
    onChooseSaveDir: () -> Unit = {},
    onChooseTextFile: () -> Unit = {},
    onCheckPermission: () -> Unit = {},
    onEditWhat:(EditWhich) -> Unit = {},
    onClickVersion:() -> Unit = {/* GO TO ABOUT SCREEN*/},
    saveDirResult: Uri? = null,
    testTextResult:String = "",
    onOpenTextFile: ((uri: Uri, contentResolver: ContentResolver)->Unit)? = { uri, contentResolver -> {}},
    versionName:String = "XXXX.XX.XX",
    versionNumber:Long = 0,
    systemUiController: SystemUiController = rememberSystemUiController(),
    viewModel: HTViewModel = HTViewModel(),
    uiState: HTUIState = HTUIState(),
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    configuration: Configuration = LocalConfiguration.current,
    windowInfo: WindowInfo = rememberWindowInfo(),
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
    grided:Boolean = true,
){
    // https://fonts.google.com/icons?selected=Material+Symbols+Outlined:logout:FILL@0;wght@400;GRAD@0;opsz@24&icon.size=24&icon.color=%23e8eaed
    // https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary
    val counters:MutableMap<String,Int> = LinkedHashMap<String,Int>()
    val gridFixCount = if(isCompact) 2 else 4
    val lazyListState = rememberLazyListState()
    val lazyGridState = rememberLazyGridState()

    ProvidePreferenceLocals {
        if(grided) {
            LazyVerticalGridScrollbar(
                state = lazyGridState,
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyGridState,
                    columns = GridCells.Fixed(gridFixCount),
                ) {
                    item(
                        span = { GridItemSpan(this.maxLineSpan) }
                    ) {
                        OptionItemCard(
                            title = stringResource(R.string.home_screen_file_label),
                            summary = stringResource(
                                R.string.home_screen_file_description,
                                pluralStringResource(
                                    R.plurals.pages_units,
                                    uiState.pageList.size,
                                    uiState.pageList.size
                                )
                            ),
                            iconModel = R.drawable.home,
                            icon = Icons.Default.Home,
                            onClick = {
                                onEditWhat(EditWhich.Home)
                            },
                            fullSpanDesign = true,
                            readAriaOnLongClick = true,
                        )
                    }
                    item {
                        OptionItemCard(
                            title = stringResource(R.string.pages_folder_label),
                            summary = context.resources.getQuantityString(
                                R.plurals.pieces_units,
                                uiState.pageList.size,
                                uiState.pageList.size
                            ),
                            iconModel = R.drawable.open_a_page,
                            icon = Icons.Default.Pages,
                            onClick = {
                                onEditWhat(EditWhich.Pages)
                            },
                            readAriaOnLongClick = true,
                        )
                    }
                    item {
                        OptionItemCard(
                            title = stringResource(R.string.items_folder_label),
                            summary = context.resources.getQuantityString(
                                R.plurals.pieces_units,
                                uiState.itemList.size,
                                uiState.itemList.size
                            ),
                            iconModel = R.drawable.cube,
                            icon = Icons.Default.Category,
                            onClick = {
                                onEditWhat(EditWhich.Items)
                            },
                            readAriaOnLongClick = true,
                        )
                    }
                    item {
                        OptionItemCard(
                            title = stringResource(R.string.themes_folder_label),
//                            summary = context.resources.getQuantityString(
//                                R.plurals.pieces_units,
//                                uiState.themeList.size,
//                                uiState.themeList.size
//                            ),
                            iconModel = R.drawable.paint_theme,
                            icon = Icons.Default.FormatPaint,
                            onClick = {
                                onEditWhat(EditWhich.Themes)
                            },
                            readAriaOnLongClick = true,
                        )
                    }
                    item {
                        OptionItemCard(
                            title = stringResource(R.string.medias_folder_label),
//                            summary = stringResource(R.string.settings_description),
                            iconModel = R.drawable.media_note,
                            icon = Icons.Default.MusicNote,
                            onClick = {
                                onEditWhat(EditWhich.Medias)
                            },
                            readAriaOnLongClick = true,
                        )
                    }
                }
            }
        } else {
            LazyColumnScrollbar(
                state = lazyListState,
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState
                ) {
                    preference(
                        key = "home_editor",
                        title = { Text(text = stringResource(R.string.home_screen_file_label)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null
                            )
                        },
//                summary = { Text(text = "${uiState.pageList.size} ${stringResource(R.string.pieces_unit)}") },
                        summary = { Text(text = "Reorder pages") },
                        onClick = {
                            onEditWhat(EditWhich.Home)
                        }
                    )
                    preference(
                        key = "pages_editor",
                        title = { Text(text = stringResource(R.string.pages_folder_label)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Pages,
                                contentDescription = null
                            )
                        },
                        summary = {
                            Text(
                                text = "${
                                    context.resources.getQuantityString(
                                        R.plurals.pieces_units,
                                        uiState.pageList.size,
                                        uiState.pageList.size
                                    )
                                }"
                            )
                        },
                        onClick = {
                            onEditWhat(EditWhich.Pages)
                        }
                    )
                    preference(
                        key = "items_editor",
                        title = { Text(text = stringResource(R.string.items_folder_label)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Category,
                                contentDescription = null
                            )
                        },
                        summary = {
                            Text(
                                text = "${
                                    context.resources.getQuantityString(
                                        R.plurals.pieces_units,
                                        uiState.itemList.size,
                                        uiState.itemList.size
                                    )
                                }"
                            )
                        },
                        onClick = {
                            onEditWhat(EditWhich.Items)
                        }
                    )
                    preference(
                        key = "themes_editor",
                        title = { Text(text = stringResource(R.string.themes_folder_label)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.FormatPaint,
                                contentDescription = null
                            )
                        },
//                summary = { Text(text = "You are already FULL VERSION.") },
                        onClick = {
                            onEditWhat(EditWhich.Themes)
                        }
                    )
                    preference(
                        key = "medias_editor",
                        title = { Text(text = stringResource(R.string.medias_folder_label)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null
                            )
                        },
//                summary = { Text(text = "You are already FULL VERSION.") },
                        onClick = {
                            onEditWhat(EditWhich.Medias)
                        }
                    )
                }
            }
        }
    }
}

@HTPreviewAnnotations
@Composable
fun LevelEditorPreview(){
    HTLauncherTheme {
        Surface(
            modifier = Modifier.fillMaxSize().navigationBarsPadding().statusBarsPadding()
        ) {
            LevelEditor()
        }
    }
}