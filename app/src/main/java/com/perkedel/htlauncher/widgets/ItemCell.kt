package com.perkedel.htlauncher.widgets

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
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.bumptech.glide.load.resource.drawable.DrawableResource
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.ActionData
import com.perkedel.htlauncher.data.ItemData
import com.perkedel.htlauncher.data.PageData
import com.perkedel.htlauncher.enumerations.ActionDataLaunchType
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ItemCell(
    readTheItemData: ItemData? = null,
    readTheItemFile: String = "",
    handoverText:String = "",
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    pm : PackageManager = context.packageManager,
    haptic: HapticFeedback = LocalHapticFeedback.current,
    uiState: HTUIState = HTUIState(),
    viewModel: HTViewModel = HTViewModel(),
    contentResolver: ContentResolver = context.contentResolver,
    json: Json = Json {
        // https://coldfusion-example.blogspot.com/2022/03/jetpack-compose-kotlinx-serialization_79.html
        prettyPrint = true
        encodeDefaults = true
    },
    tts: MutableState<TextToSpeech?> = rememberTextToSpeech(),
    onClick: ((List<ActionData>)->Unit)? = {},
    onLongClick: (()->Unit)? = {},
    windowInfo: WindowInfo = rememberWindowInfo(),
    configuration: Configuration = LocalConfiguration.current,
    isCompact: Boolean = windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact,
    isOrientation: Int = configuration.orientation,
){
    // Load file
    var itemFolder = Uri.parse("")
    var itemUri:Uri = Uri.parse("")
    var mediaFolder:Uri = Uri.parse("")
    var itemOfIt:ItemData = ItemData(
        name = readTheItemFile
    )
    LaunchedEffect(true) {
//        Log.d("ItemCell", "Eval filename = ${readTheItemFile}")
//        Log.d("ItemCell", "Eval selected save = ${uiState.selectedSaveDir}")

        if(readTheItemData != null){
            itemOfIt = readTheItemData
        } else {
            if (uiState.itemList.contains(readTheItemFile) && uiState.itemList[readTheItemFile] != null) {
                itemOfIt = uiState.itemList[readTheItemFile]!!
            } else {
//            if (readTheItemFile.isNotEmpty() && uiState.selectedSaveDir != null && uiState.selectedSaveDir.toString()
//                    .isNotEmpty()
//            ) {
//                itemFolder = getADirectory(
//                    dirUri = uiState.selectedSaveDir,
//                    context = context,
//                    dirName = context.resources.getString(R.string.items_folder)
//                )
//                itemUri = getATextFile(
//                    dirUri = itemFolder,
//                    context = context,
//                    fileName = "${readTheItemFile}.json",
//                    initData = Json.encodeToString<ItemData>(ItemData()),
//                    hardOverwrite = false,
//                )
//                Log.d("ItemCell", "Eval Item Folder ${itemFolder}")
//                Log.d("ItemCell", "Eval Item Uri ${itemUri}")
//                itemOfIt = Json.decodeFromString<ItemData>(openATextFile(itemUri, contentResolver))
//                Log.d("ItemCell", "an Item ${readTheItemFile} has:\n${itemOfIt}")
//            } else {
//                Log.d("ItemCell", "(EMPTY) an Item ${readTheItemFile} has:\n${itemOfIt}")
//            }
//            uiState.itemList[readTheItemFile] = itemOfIt
            }
        }
    }
    if(readTheItemData != null){
        itemOfIt = readTheItemData
    } else {
        if (uiState.itemList.contains(readTheItemFile) && uiState.itemList[readTheItemFile] != null) {
            itemOfIt = uiState.itemList[readTheItemFile]!!
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
    var selectImage:Any = itemOfIt.imagePath
    var selectLabel:String = if(itemOfIt.label.isNotEmpty()) itemOfIt.label else handoverText
    if(LocalInspectionMode.current){

    } else {
        try {
            selectImage = when (itemOfIt.showWhichIcon) {
                ShowWhichIcon.Default -> if (itemOfIt.action[0].action.isNotEmpty()) pm.getApplicationIcon(
                    itemOfIt.action[0].action
                ) else "idk"

                else -> if (itemOfIt.action[0].action.isNotEmpty()) pm.getApplicationIcon(itemOfIt.action[0].action) else "idk"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (itemOfIt.useLabel) {
                selectLabel = if (itemOfIt.label.isNotEmpty()) itemOfIt.label else handoverText
            } else {
                if (itemOfIt.action[0].action.isNotEmpty()) {
                    // https://stackoverflow.com/a/5841353/9079640
                    val ai: ApplicationInfo = pm.getApplicationInfo(itemOfIt.action[0].action, 0)
                    selectLabel = if (ai != null) pm.getApplicationLabel(ai)
                        .toString() else if (itemOfIt.label.isNotEmpty()) itemOfIt.label else handoverText
                }
            }
        } catch (e: Exception) {
            selectLabel = if (itemOfIt.label.isNotEmpty()) itemOfIt.label else handoverText
            e.printStackTrace()
        }
    }
    var selectCompartmentType:String = when(itemOfIt.action[0].type){
        ActionDataLaunchType.LauncherActivity -> stringResource(R.string.aria_label_LauncherActivity,selectLabel)
        ActionDataLaunchType.ShellOpen -> stringResource(R.string.aria_label_ShellOpen,selectLabel)
        ActionDataLaunchType.Activity -> stringResource(R.string.aria_label_Activity,selectLabel)
        else -> selectLabel
    }
    var selectAria:String = if(itemOfIt.useAria && itemOfIt.aria.isNotEmpty()) itemOfIt.aria else selectCompartmentType

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
                    Log.d("ItemCell","Click ${handoverText}\n${uiState.itemList[readTheItemFile]}")
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
                    )
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    Toast
                        .makeText(
                            context,
                            selectAria,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    if (onLongClick != null) {
                        onLongClick()
                    }
                },
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
                contentDescription = itemOfIt.aria,
                modifier = Modifier.fillMaxSize(),
                error = painterResource(id = R.drawable.mavrickle),
            )

            if(itemOfIt.showLabel) {
                OutlinedText(
                    modifier = Modifier
//                    .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    text = selectLabel,
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

@HTPreviewAnnotations
@Composable
fun ItemCellPreview(){
    HTLauncherTheme {
        ItemCell(
            handoverText = "HALLO",
            readTheItemData = ItemData(

            )
        )
    }
}