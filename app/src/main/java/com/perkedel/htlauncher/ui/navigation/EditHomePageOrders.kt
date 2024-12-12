package com.perkedel.htlauncher.ui.navigation

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.data.viewmodels.ItemEditorViewModel
import com.perkedel.htlauncher.enumerations.PageViewStyle
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import me.zhanghai.compose.preference.ProvidePreferenceLocals

@Composable
fun EditHomePageOrders(
    modifier: Modifier = Modifier,
    data: HomepagesWeHave? = null,
    id:Int = 0,
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    onRebuild: (HomepagesWeHave) -> Unit = { homescreen: HomepagesWeHave -> },
//    onSwap: (Int, Int) -> Unit = { i: Int, i1: Int -> },
    onSwap: (List<String>) -> Unit = {},
    onClose: () -> Unit = {},
    view: View = LocalView.current,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    viewModel: ItemEditorViewModel = viewModel(),
    htViewModel: HTViewModel = viewModel(),
    initViewStyle: PageViewStyle = PageViewStyle.Column,
    windowInfo: WindowInfo = rememberWindowInfo(),
    configuration: Configuration = LocalConfiguration.current,
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
){
    ProvidePreferenceLocals {

    }
}