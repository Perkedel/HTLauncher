package com.perkedel.htlauncher.widgets

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bumptech.glide.load.resource.drawable.DrawableResource
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ItemCell(
    readTheItemFile: String = "",
    handoverText:String = "",
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    pm : PackageManager = context.packageManager,
    haptic: HapticFeedback = LocalHapticFeedback.current,
){
    // Load file
    if(readTheItemFile.isNotEmpty()){

    }

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
                    Toast
                        .makeText(context, "Click ${handoverText}", Toast.LENGTH_SHORT)
                        .show()
                },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    Toast
                        .makeText(
                            context,
                            "Long Click ${handoverText}",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            )
            .padding(8.dp)
            .aspectRatio(1f)
//                                .background(Color.Transparent)
            .clip(RoundedCornerShape(10.dp))
        ,
        color = Color.Transparent,
    ) {
        Box(

        ){
            Image(
                // https://developer.android.com/develop/ui/compose/graphics/images/loading
                painter = painterResource(id = R.drawable.mavrickle),

                contentDescription = "Mavrickle",
                modifier = Modifier.fillMaxSize(),
            )

            OutlinedText(
                modifier = Modifier

                    .align(Alignment.BottomCenter)
                ,
                text = "${handoverText}",
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
                )
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun ItemCellPreview(){
    HTLauncherTheme {
        ItemCell(
            handoverText = "HALLO"
        )
    }
}