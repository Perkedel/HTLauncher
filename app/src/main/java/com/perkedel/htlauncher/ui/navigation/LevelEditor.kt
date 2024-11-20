package com.perkedel.htlauncher.ui.navigation

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material.icons.filled.ShapeLine
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.enumerations.EditWhich
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.preference

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
){
    // https://fonts.google.com/icons?selected=Material+Symbols+Outlined:logout:FILL@0;wght@400;GRAD@0;opsz@24&icon.size=24&icon.color=%23e8eaed
    // https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary
    val counters:MutableMap<String,Int> = LinkedHashMap<String,Int>()


    ProvidePreferenceLocals {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            preference(
                key = "pages_editor",
                title = { Text(text = stringResource(R.string.pages_folder_label) ) },
                icon = { Icon(imageVector = Icons.Default.Pages, contentDescription = null) },
                summary = { Text(text = "${uiState.pageList.size} ${stringResource(R.string.pieces_unit)}") },
                onClick = {
                    onEditWhat(EditWhich.Pages)
                }
            )
            preference(
                key = "items_editor",
                title = { Text(text = stringResource(R.string.items_folder_label) ) },
                icon = { Icon(imageVector = Icons.Default.Category, contentDescription = null) },
                summary = { Text(text = "${uiState.itemList.size} ${stringResource(R.string.pieces_unit)}") },
                onClick = {
                    onEditWhat(EditWhich.Items)
                }
            )
            preference(
                key = "themes_editor",
                title = { Text(text = stringResource(R.string.themes_folder_label) ) },
                icon = { Icon(imageVector = Icons.Default.FormatPaint, contentDescription = null) },
//                summary = { Text(text = "You are already FULL VERSION.") },
                onClick = {
                    onEditWhat(EditWhich.Themes)
                }
            )
            preference(
                key = "medias_editor",
                title = { Text(text = stringResource(R.string.medias_folder_label) ) },
                icon = { Icon(imageVector = Icons.Default.Image, contentDescription = null) },
//                summary = { Text(text = "You are already FULL VERSION.") },
                onClick = {
                    onEditWhat(EditWhich.Medias)
                }
            )
        }
    }
}

@HTPreviewAnnotations
@Composable
fun LevelEditorPreview(){
    HTLauncherTheme {
        LevelEditor()
    }
}