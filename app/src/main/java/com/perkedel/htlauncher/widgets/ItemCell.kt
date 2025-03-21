@file:OptIn(ExperimentalLayoutApi::class)

package com.perkedel.htlauncher.widgets

import android.app.Notification.Action
import android.content.ContentResolver
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.SoundEffectConstants
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.bumptech.glide.load.resource.drawable.DrawableResource
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.enumerations.ActionDataLaunchType
import com.perkedel.htlauncher.enumerations.ActionGoToSystemSetting
import com.perkedel.htlauncher.enumerations.ActionInternalCommand
import com.perkedel.htlauncher.enumerations.InternalCategories
import com.perkedel.htlauncher.enumerations.PageViewStyle
import com.perkedel.htlauncher.enumerations.ShowWhichIcon
import com.perkedel.htlauncher.func.WindowInfo
import com.perkedel.htlauncher.func.rememberWindowInfo
import com.perkedel.htlauncher.getADirectory
import com.perkedel.htlauncher.getATextFile
import com.perkedel.htlauncher.modules.rememberTextToSpeech
import com.perkedel.htlauncher.modules.ttsSpeakOrStop
import com.perkedel.htlauncher.openATextFile
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.zhanghai.compose.preference.Preference
import org.jetbrains.annotations.ApiStatus.Internal

@OptIn(ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ItemCell(
    readTheItemData: ItemData? = null,
    readTheItemFile: String = "",
    handoverText:String = "",
    viewStyle: PageViewStyle = PageViewStyle.Default,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    pm : PackageManager = context.packageManager,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    uiState: HTUIState = HTUIState(),
    viewModel: HTViewModel = HTViewModel(),
    view: View = LocalView.current,
    contentResolver: ContentResolver = context.contentResolver,
    json: Json = Json {
        // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
        prettyPrint = true
        encodeDefaults = true
    },
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    onClick: ((List<ActionData>)->Unit)? = {},
    onLongClick: ((List<ActionData>)->Unit)? = {},
//    windowInfo: WindowInfo = rememberWindowInfo(),
//    configuration: Configuration = LocalConfiguration.current,
//    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
//    isOrientation: Int = configuration.orientation,
    preventCreateNewFile:Boolean = false,
){
//    val tts by rememberTextToSpeech()
    // Load file
    var itemFolder = Uri.parse("")
    var itemUri:Uri = Uri.parse("")
    var mediaFolder:Uri = Uri.parse("")
    var itemOfIt:ItemData = ItemData(
        name = readTheItemFile
    )

    // DONE:
    if(readTheItemData != null){
//        Log.d("ItemCell", "Injecting ${readTheItemData}")
        itemOfIt = readTheItemData
    } else {
//        Log.d("ItemCell", "Instead File $readTheItemFile")
        if (uiState.itemList.contains(readTheItemFile) && uiState.itemList[readTheItemFile] != null) {
//        if (uiState.itemList.contains(readTheItemFile) && uiState.itemList[readTheItemFile] != null) {
//        if(uiState.selectedSaveDir != null){
//            itemOfIt = uiState.itemList[readTheItemFile]!!
//            Log.d("ItemCell", "Reading ${readTheItemFile}")
            itemOfIt = viewModel.getItemData(
                readTheItemFile,
                json = json,
                context = context,
                ignoreFile = !preventCreateNewFile,
            )
        }
    }

    if(uiState.selectedSaveDir != null){
//        mediaFolder = getADirectory(
//            dirUri = uiState.selectedSaveDir,
//            context = context,
//            dirName = stringResource(R.string.medias_folder)
//            )
//        itemFolder = getADirectory(
//            dirUri = uiState.selectedSaveDir,
//            context = context,
//            dirName = stringResource(R.string.items_folder)
//        )
    }

    // Done:
    var selectImage:Any = itemOfIt.imagePath
    var selectLabel:String = if(itemOfIt.label.isNotEmpty()) itemOfIt.label else handoverText
    if(itemOfIt.action.isNotEmpty() && itemOfIt.action[0].type != ActionDataLaunchType.Internal && itemOfIt.action[0].type != ActionDataLaunchType.Category) {
        try {
//            selectImage = when (itemOfIt.showWhichIcon) {
//                ShowWhichIcon.Default -> {
//                    when(itemOfIt.action[0].type){
//                        ActionDataLaunchType.LauncherActivity -> {
//                            if (itemOfIt.action[0].action.isNotEmpty()) pm.getApplicationIcon(
//                                itemOfIt.action[0].action
//                            ) else "idk"
//                        }
//                        else -> itemOfIt.action[0].action
//                    }
//
//                }
//
//                else -> itemOfIt.action[0].action
//            }
            selectImage = when(itemOfIt.showWhichIcon){
                ShowWhichIcon.Default -> viewModel.getItemIcon(itemOfIt, context = context, pm = pm) ?: itemOfIt.action[0].action
                else -> viewModel.getItemIcon(itemOfIt, context = context, pm = pm) ?: itemOfIt.action[0].action
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (itemOfIt.useLabel) {
                selectLabel = if (itemOfIt.label.isNotEmpty()) itemOfIt.label else handoverText
            } else {
                if (itemOfIt.action[0].action.isNotEmpty()) {
                    when(itemOfIt.action[0].type){
                        ActionDataLaunchType.LauncherActivity -> {
                            // https://stackoverflow.com/a/5841353/9079640
                            val ai: ApplicationInfo = pm.getApplicationInfo(itemOfIt.action[0].action, 0)
                            selectLabel = if (ai != null) pm.getApplicationLabel(ai)
                                .toString() else if (itemOfIt.label.isNotEmpty()) itemOfIt.label else handoverText
                        }
                        else -> selectLabel = itemOfIt.action[0].action
                    }

                }
            }
        } catch (e: Exception) {
            selectLabel = itemOfIt.label.ifEmpty { handoverText }
            e.printStackTrace()
        }
    } else {
        // CRASH
//        selectImage = ActionInternalCommand.valueOf(itemOfIt.action[0].action).icon
//        selectLabel = stringResource(ActionInternalCommand.valueOf(itemOfIt.action[0].action).label)

        var settingIcon: Int = R.drawable.mavrickle
        var settingLabel: String = "SETTING???"
//        LaunchedEffect(itemOfIt.action[0].action) {
        try {
            settingIcon = ActionGoToSystemSetting.valueOf(
                itemOfIt.action[0].action.replaceFirst(
                    "Settings",
                    ""
                )
            ).icon
        } catch (_: Exception) {
        }
        try {
            settingLabel = context.resources.getString(
                ActionGoToSystemSetting.valueOf(
                    itemOfIt.action[0].action.replaceFirst(
                        "Settings",
                        ""
                    )
                ).label
            )
        } catch (_: Exception) {

        }
//        }
        selectImage =
            if(itemOfIt.action.isNotEmpty() && itemOfIt.action[0].action.isNotBlank()) {
//                when (itemOfIt.action[0].action) {
                when {
                    // https://stackoverflow.com/questions/68932422/loading-local-drawables-with-coil-compose
                    // WOW Codeium you got it!! (Result these to refer ActionInternalCommand.???.image as the first two)
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.AllApps.id) -> ActionInternalCommand.AllApps.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.Camera.id) -> ActionInternalCommand.Camera.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.Telephone.id) -> ActionInternalCommand.Telephone.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.Gallery.id) -> ActionInternalCommand.Gallery.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.Clock.id) -> ActionInternalCommand.Clock.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.Contacts.id) -> ActionInternalCommand.Contacts.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.Messages.id) -> ActionInternalCommand.Messages.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.Emergency.id) -> ActionInternalCommand.Emergency.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.Settings.id) -> ActionInternalCommand.Settings.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.SystemSettings.id) -> ActionInternalCommand.SystemSettings.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.Preferences.id) -> ActionInternalCommand.Preferences.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.GoToPage.id) -> ActionInternalCommand.GoToPage.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.OpenAPage.id) -> ActionInternalCommand.OpenAPage.image
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.Aria.id) -> ActionInternalCommand.Aria.image
                    itemOfIt.action[0].action == stringResource(InternalCategories.SettingsSystem.id) -> InternalCategories.SettingsSystem.image
                    itemOfIt.action[0].action == stringResource(InternalCategories.SettingsOverall.id) -> InternalCategories.SettingsOverall.image
                    itemOfIt.action[0].action.startsWith("Settings") -> settingIcon
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.GetStarted.id) -> ActionInternalCommand.GetStarted.image
                    else -> R.drawable.placeholder
                }
            } else R.drawable.mavrickle
        selectLabel = if(itemOfIt.action.isNotEmpty() && itemOfIt.action[0].action.isNotBlank()) {
//            if(!itemOfIt.action[0].action.startsWith("Settings")) {
                when {
//                when (itemOfIt.action[0].action) {
                    itemOfIt.action[0].action ==stringResource(ActionInternalCommand.AllApps.id) -> stringResource(
                        ActionInternalCommand.AllApps.label
                    )

                    itemOfIt.action[0].action ==stringResource(ActionInternalCommand.Camera.id) -> stringResource(
                        ActionInternalCommand.Camera.label
                    )

                    itemOfIt.action[0].action ==stringResource(ActionInternalCommand.Telephone.id) -> stringResource(
                        ActionInternalCommand.Telephone.label
                    )

                    itemOfIt.action[0].action ==stringResource(ActionInternalCommand.Clock.id) -> stringResource(
                        ActionInternalCommand.Clock.label
                    )

                    itemOfIt.action[0].action ==stringResource(ActionInternalCommand.Messages.id) -> stringResource(
                        ActionInternalCommand.Messages.label
                    )

                    itemOfIt.action[0].action ==stringResource(ActionInternalCommand.Emergency.id) -> stringResource(
                        ActionInternalCommand.Emergency.label
                    )

                    itemOfIt.action[0].action ==stringResource(ActionInternalCommand.Gallery.id) -> stringResource(
                        ActionInternalCommand.Gallery.label
                    )

                    itemOfIt.action[0].action ==stringResource(ActionInternalCommand.Settings.id) -> stringResource(
                        ActionInternalCommand.Settings.label
                    )

                    itemOfIt.action[0].action ==stringResource(ActionInternalCommand.SystemSettings.id) -> stringResource(
                        ActionInternalCommand.SystemSettings.label
                    )

                    itemOfIt.action[0].action ==stringResource(ActionInternalCommand.Preferences.id) -> stringResource(
                        ActionInternalCommand.Preferences.label
                    )

                    itemOfIt.action[0].action ==stringResource(ActionInternalCommand.Contacts.id) -> stringResource(
                        ActionInternalCommand.Contacts.label
                    )

                    itemOfIt.action[0].action ==stringResource(InternalCategories.SettingsSystem.id) -> stringResource(
                        InternalCategories.SettingsSystem.label
                    )

                    itemOfIt.action[0].action ==stringResource(InternalCategories.SettingsOverall.id) -> stringResource(
                        InternalCategories.SettingsOverall.label
                    )

                    itemOfIt.action[0].action.startsWith("Settings") -> {
//                        stringResource(
//                            ActionGoToSystemSetting.valueOf(
//                                itemOfIt.action[0].action.replaceFirst(
//                                    "Settings",
//                                    ""
//                                )
//                            ).label
//                        )
                        settingLabel
                    }
                    itemOfIt.action[0].action == stringResource(ActionInternalCommand.GetStarted.id) -> stringResource(
                        ActionInternalCommand.GetStarted.label
                    )
                    else -> itemOfIt.action[0].action
                }
//            } else {
//                stringResource(
//                    ActionGoToSystemSetting.valueOf(
//                        itemOfIt.action[0].action.replaceFirst(
//                            "Settings",
//                            ""
//                        )
//                    ).label
//                )
//            }
        } else handoverText
    }
    val selectCompartmentType:String = if(itemOfIt.action.isNotEmpty()){
        when (itemOfIt.action[0].type) {
            ActionDataLaunchType.LauncherActivity -> stringResource(
                R.string.aria_label_LauncherActivity,
                selectLabel
            )

            ActionDataLaunchType.ShellOpen -> stringResource(
                R.string.aria_label_ShellOpen,
                selectLabel
            )

            ActionDataLaunchType.Activity -> stringResource(
                R.string.aria_label_Activity,
                selectLabel
            )
            ActionDataLaunchType.Category -> {
//                when (itemOfIt.action[0].action) {
//                    else -> try {
//                        context.resources.getString(InternalCategories.valueOf(itemOfIt.action[0].action.replaceFirst("Category","")).label)
//                    } catch (_:Exception){
//                        ""
//                    }
//                }
                selectLabel
            }
            ActionDataLaunchType.Internal -> {
                // NO SOLUTION
                // if enum not found, crash
                // https://stackoverflow.com/questions/69163458/java-lang-illegalargumentexception-no-enum-constant-found
                // https://www.reddit.com/r/JetpackCompose/comments/1fvyftg/help_im_creating_an_app_just_to_practice_im/
                // https://www.reddit.com/r/JetpackCompose/comments/1fvyftg/help_im_creating_an_app_just_to_practice_im/
                // https://stackoverflow.com/questions/77933152/using-mutablestateof-with-enums-kotlin-jetpack-composable
                // https://stackoverflow.com/questions/74860799/how-to-catch-an-error-in-jetpack-compose

                try {
//                stringResource(ActionInternalCommand.valueOf(itemOfIt.action[0].action).label)
                    context.resources.getString(ActionInternalCommand.valueOf(itemOfIt.action[0].action).label)
                } catch (_: Exception) {
                    selectLabel
                }
//                when (itemOfIt.action[0].action) {
//                    stringResource(ActionInternalCommand.AllApps.id) -> stringResource(
//                        ActionInternalCommand.AllApps.label
//                    )
//
//                    else -> selectLabel
//                }
            }

            else -> selectLabel
        }
    } else selectLabel

    val selectAria:String = if(itemOfIt.useAria && itemOfIt.aria.isNotEmpty()) itemOfIt.aria else selectCompartmentType
//    val selectAria:String = if(itemOfIt.useAria && itemOfIt.aria.isNotEmpty()) itemOfIt.aria else selectLabel

    val gridViewStyle: @Composable ()->Unit = {
        Surface(
            modifier = modifier
                .combinedClickable(
                    // https://gist.github.com/dovahkiin98/95157e662daacddfbc1b60e0fb8bb7c0
                    // https://developer.android.com/develop/ui/compose/touch-input/pointer-input/tap-and-press
                    // https://stackoverflow.com/questions/65835642/button-long-press-listener-in-android-jetpack-compose
                    onClick = {
                        haptic.performHapticFeedback(
                            HapticFeedbackType.LongPress
                        )
//                    Toast
//                        .makeText(context, "Click ${handoverText}", Toast.LENGTH_SHORT)
//                        .show()
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        Log.d(
                            "ItemCell",
                            "Click ${handoverText}\n${uiState.itemList[readTheItemFile]}"
                        )
                        if (onClick != null) {
                            onClick(
                                itemOfIt.action
                            )
                        }
                    },
                    onLongClick = {
//                    val readout:String = if(itemOfIt.useAria && itemOfIt.aria.isNotEmpty()) itemOfIt.aria else itemOfIt.label
                        ttsSpeakOrStop(
                            handover = tts,
                            message = selectAria,
//                        message = itemOfIt.aria,
                        )
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        Toast
                            .makeText(
                                context,
                                selectAria,
//                            itemOfIt.aria,
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        if (onLongClick != null) {
                            onLongClick(itemOfIt.action)
                        }
                    },
//                onClickLabel = selectAria,
                    onClickLabel = selectAria,
                )
                .padding(8.dp)
                .aspectRatio(1f)
//                                .background(Color.Transparent)
                .clip(RoundedCornerShape(10.dp))
            ,
            color = Color.Transparent,
        ) {
            Box(
                modifier = Modifier
                ,
            ){
//            Image(
//                // https://developer.android.com/develop/ui/compose/graphics/images/loading
//                painter = painterResource(id = R.drawable.mavrickle),
//
//                contentDescription = "Mavrickle",
//                modifier = Modifier.fillMaxSize(),
//            )

                AsyncImage(
                    // https://stackoverflow.com/a/73197578/9079640
                    // https://stackoverflow.com/a/68727678/9079640
//                model = itemOfIt.imagePath, // TODO: load image from Medias folder
                    model = selectImage,
//                model = Drawable.cre,
                    contentDescription = selectAria,
                    modifier = Modifier.fillMaxSize(),
                    error = painterResource(id = R.drawable.mavrickle),
                    placeholder = painterResource(id = R.drawable.placeholder),
                )

                if(itemOfIt.showLabel) {
                    OutlinedText(
                        modifier = Modifier
//                    .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .basicMarquee()
                        ,
                        text = selectLabel,
//                    text = itemOfIt.aria,
//                text = if(uiState.itemList[readTheItemFile] != null && uiState.itemList[readTheItemFile]!!.label.isNotEmpty()) uiState.itemList[readTheItemFile]!!.label else handoverText,
                        textAlign = TextAlign.Center,
//                color = rememberColorScheme().primary,
//                style = TextStyle.Default.copy(
//                    // https://stackoverflow.com/a/66958833/9079640
//                    // https://canopas.com/how-to-apply-stroke-effects-to-text-in-jetpack-compose-b1c02c9907bd
//                    drawStyle = Stroke(
//                        width = 2f,
//                        miter = 2f,
//                        join = StrokeJoin.Round,
//                    )
//                    ,
//                    color = Color(0xFFF67C37)
//                )
                        fillColor = rememberColorScheme().onSurface,
                        outlineColor = rememberColorScheme().surfaceBright,
                        outlineDrawStyle = Stroke(
                            width = 10f,
                            miter = 5f,
                            join = StrokeJoin.Round,
                        ),
                    )
                }

            }
        }
    }
    val barViewStyle: @Composable ()->Unit = {
        Preference(
            modifier = modifier,
            title = {
                Text(
                    text = selectLabel,
                )
            },
            icon = {
                AsyncImage(
                    // https://stackoverflow.com/a/73197578/9079640
                    // https://stackoverflow.com/a/68727678/9079640
//                model = itemOfIt.imagePath, // TODO: load image from Medias folder
                    model = selectImage,
//                model = Drawable.cre,
                    contentDescription = selectAria,
                    modifier = Modifier.fillMaxSize(),
                    error = painterResource(id = R.drawable.mavrickle),
                    placeholder = painterResource(id = R.drawable.placeholder),
                )
            }
        )
    }
    val categoryViewStyle: @Composable ()->Unit = {
        SettingCategoryBar(
            modifier = modifier,
            title = selectLabel,
            tts = tts,
            context = context,
            icon = {
                AsyncImage(
                    // https://stackoverflow.com/a/73197578/9079640
                    // https://stackoverflow.com/a/68727678/9079640
//                model = itemOfIt.imagePath, // TODO: load image from Medias folder
                    model = selectImage,
//                model = Drawable.cre,
                    contentDescription = selectAria,
                    modifier = Modifier
//                        .aspectRatio(1f)
                        .size(32.dp)
                    ,
                    error = painterResource(id = R.drawable.mavrickle),
                    placeholder = painterResource(id = R.drawable.placeholder),
                )
            }
        )
    }

    // DONE:
    when{
        itemOfIt.isCategory -> categoryViewStyle()
        viewStyle == PageViewStyle.Column -> barViewStyle()
        viewStyle == PageViewStyle.Default || viewStyle == PageViewStyle.Grid -> gridViewStyle()
        else -> gridViewStyle()
    }

}

@HTPreviewAnnotations
//@Preview
@Composable
fun ItemCellPreview(){
    // https://youtu.be/EMJ_Py1mcj4
    // https://youtube.com/watch?v=EMJ_Py1mcj4
    // https://youtu.be/bTgyDqBoZ_o
    // https://youtu.be/m41dDoa0f5w
    // https://github.com/masterscoding/compose-learning
    // https://youtu.be/XfYlRn_Jy1g
    // https://thengoding.com/2024/05/16/jetpack-compose-lazyverticalgrid
    // https://medium.com/@kathankraithatha/grids-in-jetpack-compose-59d99551ec58
    // https://stackoverflow.com/a/75782959/9079640
    // https://www.valueof.io/blog/lazy-grids-gridcells-fixed-adaptive-custom-compose
    // no such solution! we want category bar spans whole width while item is grid
    // wait nvm we found it
    // https://www.valueof.io/blog/lazy-grids-gridcells-fixed-adaptive-custom-compose
    HTLauncherTheme {
        Column {
//            FlowRow(
//                modifier = Modifier,
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                ItemCell(
//                    handoverText = "HALLO",
//                    readTheItemData = ItemData(
//                        label = "HALLO AAAAAAAAAAAAAAAAAAAAAA",
//                        useLabel = true,
//                        isCategory = true,
//                    ),
//                    modifier = Modifier
//                )
//                ItemCell(
//                    handoverText = "aALLO",
//                    readTheItemData = ItemData(
//                        label = "EEEEEEAAAAAAAAAAAAAAAAAAAAA",
//                        useLabel = true,
//                    ),
//                    modifier = Modifier.size(100.dp)
//                )
//                ItemCell(
//                    handoverText = "HALLO",
//                    readTheItemData = ItemData(
//                        label = "HALLO AAAAAAAAAAAAAAAAAAAAAA",
//                        useLabel = true,
//                    ),
//                    modifier = Modifier.size(100.dp)
//                )
//            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3)
//            rows = GridCells.Fixed(3)
//                columns = StaggeredGridCells.Fixed(3)
//                columns = StaggeredGridCells.Adaptive(100.dp),
//                rows = StaggeredGridCells.Fixed(3)
            ) {
                item(
//                    span = StaggeredGridItemSpan.FullLine
                    span = {GridItemSpan(this.maxLineSpan)}
                ) {
                    ItemCell(
                        handoverText = "HALLO",
                        readTheItemData = ItemData(
                            label = "HALLO Awwwwwwwwwwwwwwwwwwwwwwwwwwwwww",
                            useLabel = true,
                            isCategory = true,
                        ),
                        modifier = Modifier
                    )

                }
                item {
                    ItemCell(
                        handoverText = "HALLO",
                        readTheItemData = ItemData(
                            label = "HALLO AAAAAAAAAAAAAAAAAAAAAA",
                            useLabel = true,
                        )
                    )

                }
                items(
                    items = ActionInternalCommand.entries.toList()
                ){
                    ItemCell(
                        handoverText = "HALLOa",
                        readTheItemData = ItemData(
                            name = "ALLAPP",
                            label = "ALLAPP",
                            action = listOf(
                                ActionData(
                                    type = ActionDataLaunchType.Internal,
                                    action = stringResource(it.id)
                                )
                            )
                        )
                    )
                }
            }
        }
    }
}