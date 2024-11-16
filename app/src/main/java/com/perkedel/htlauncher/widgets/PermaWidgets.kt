package com.perkedel.htlauncher.widgets

import android.os.Build
import android.os.SystemClock
import android.widget.TextClock
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import java.text.SimpleDateFormat
import java.util.Date

//@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun FirstPageCard(
    isOnNumberWhat:Int = 0,
    modifier: Modifier,
    isCompact:Boolean = true,
    onMoreMenuButton: () -> Unit,
){
//    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    // https://www.geeksforgeeks.org/how-to-get-current-time-and-date-in-android-using-jetpack-compose/
//    val sdf = SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z")
    val sdf = SimpleDateFormat("yyyy-MM-dd '|' HH:mm:ss a z")
    val currentDateAndTime = sdf.format(Date())

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = if(isCompact) Modifier
                    .weight(1f)
                else Modifier
                    .fillMaxHeight()
                    .size(200.dp)

            ) {
                Text("HAHAHA ${isOnNumberWhat} | ${currentDateAndTime}")

                // https://www.geeksforgeeks.org/text-clock-in-android-using-jetpack-compose/
//                AndroidView(
//                    factory = { context ->
//                        TextClock(context).apply {
//                            // on below line we are setting 12 hour format.
////                            format12Hour?.let { this.format12Hour = "hh:mm:ss a" }
//                            format24Hour?.let { this.format24Hour = "hh:mm:ss a" }
//                            // on below line we are setting time zone.
//                            timeZone?.let { this.timeZone = it }
//                            // on below line we are setting text size.
////                            textSize.let { this.textSize = 30f }
//                            // pls format text
////                            textColors.let {this.textColors =  }
//                        }
//                    },
//                    modifier = Modifier.padding(5.dp),
//                )

                // bottom the more menu?
            }
            IconButton(
                onClick = onMoreMenuButton,
                modifier = Modifier

            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Menu"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FirstPageCardPreview(){
    HTLauncherTheme {
        FirstPageCard(
            modifier = Modifier,
            onMoreMenuButton = {},

        )
    }
}