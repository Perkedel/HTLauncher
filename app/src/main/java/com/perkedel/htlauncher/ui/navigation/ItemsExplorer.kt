package com.perkedel.htlauncher.ui.navigation

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.constanta.HTLauncherHardcodes
import com.perkedel.htlauncher.enumerations.ActionDataLaunchType
import com.perkedel.htlauncher.enumerations.EditWhich
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.ProvidePreferenceLocals

@Composable
fun ItemsExplorer(
    navController: NavController = rememberNavController(),
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    onSelectedSaveDir: (Uri) -> Unit = {  },
    onChooseSaveDir: () -> Unit = {},
    onChooseTextFile: () -> Unit = {},
    onCheckPermission: () -> Unit = {},
    onEditWhat:(EditWhich,String) -> Unit = {edit,filename->{}},
    onClickVersion:() -> Unit = {/* GO TO ABOUT SCREEN*/},
    saveDirResult: Uri? = null,
    testTextResult:String = "",
    onOpenTextFile: ((uri: Uri, contentResolver: ContentResolver)->Unit)? = { uri, contentResolver -> {}},
    versionName:String = "XXXX.XX.XX",
    versionNumber:Long = 0,
    systemUiController: SystemUiController = rememberSystemUiController(),
    viewModel: HTViewModel = HTViewModel(),
    uiState: HTUIState = HTUIState(),
    exploreType:EditWhich = EditWhich.Items,
    inspectionMode: Boolean = LocalInspectionMode.current,
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
){
//    val counts:Int = if(!inspectionMode) when(exploreType){
//        EditWhich.Items -> uiState.itemList.size
//        EditWhich.Pages -> uiState.pageList.size
//        else -> 0
//    } else 3
    val editData:List<String> = if(!inspectionMode) when(exploreType){
        EditWhich.Items -> uiState.itemList.keys.toList()
        EditWhich.Pages -> uiState.pageList.keys.toList()
        else -> listOf("EMPTY")
    } else listOf(
        "Hello",
        "Test",
        "HUHA"
    )

    ProvidePreferenceLocals {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
//            item {
//                Preference(
//                    title = { Text(text = stringResource(R.string.home_screen_file_label) ) },
//                    icon = { Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null) },
////                    summary = { Text(text = "You are already FULL VERSION.") },
//                    onClick = {
//                        onEditWhat(exploreType,context.resources.getString(R.string.home_screen_file))
//                    }
//                )
//            }
            items(
                count = editData.size,
            ){
                Preference(
                    title = { Text(text = editData[it] ) },
                    icon = {
                        when(exploreType){
                            EditWhich.Items -> AsyncImage(
                                modifier = Modifier.size(72.dp),
                                placeholder = painterResource(R.drawable.placeholder),
                                error = painterResource(R.drawable.mavrickle),
                                model = when(uiState.itemList[editData[it]]?.action?.get(0)?.type){
                                    ActionDataLaunchType.LauncherActivity -> if(uiState.itemList[editData[it]]?.action?.get(0)?.action?.isNotBlank() == true) pm.getApplicationIcon(uiState.itemList[editData[it]]?.action?.get(0)?.action ?: "") else R.drawable.all_apps
                                    ActionDataLaunchType.Internal -> HTLauncherHardcodes.getInternalActionIcon(uiState.itemList[editData[it]]?.action?.get(0)?.action)
                                    else -> R.drawable.placeholder
                                },
                                contentDescription = "",
                            )
                            else -> Icon(imageVector = Icons.Default.Code, contentDescription = null)
                        }
                    },
//                    summary = { Text(text = "You are already FULL VERSION.") },
                    onClick = {
                        onEditWhat(exploreType,editData[it])
                    }
                )
            }
        }
    }
}

@HTPreviewAnnotations
@Composable
fun ItemsExplorerPreview(){
    HTLauncherTheme {
        Surface {
            ItemsExplorer()
        }

    }
}