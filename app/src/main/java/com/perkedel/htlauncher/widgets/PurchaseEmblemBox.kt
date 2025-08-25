@file:OptIn(ExperimentalFoundationApi::class)

package com.perkedel.htlauncher.widgets

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.enumerations.SparsdatedType
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import com.perkedel.htlauncher.ui.theme.rememberColorScheme

@Composable
fun PurchaseEmblemBox(
    context: Context = LocalContext.current,
    pm: PackageManager = context.packageManager,
    expectedPackage:String = "com.perkedel.iamrich",
    modifier: Modifier = Modifier,
){
    var richExists:Boolean = false // is this installed
    var richInstalledFrom:String = "" // is this com.android.vending?
    var richInstallerIcon: Any? = null // Google play icon
    var isSparsdated:Boolean = true // if not com.android.vending that mean it's pirated!
    var sparsdatedType: SparsdatedType = SparsdatedType.GeneralSparsdated
    var hardcodeSparsdatedSay:String = ""

    try{
        richExists = pm.getPackageInfo(expectedPackage, 0) != null
    } catch (e: Exception)
    {
        richExists = false
    }

    try {
        richInstalledFrom = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            pm.getInstallSourceInfo(expectedPackage).installingPackageName
        } else {
            pm.getInstallerPackageName(expectedPackage)
        } ?: "---"

        isSparsdated = richInstalledFrom != "com.android.vending"
        when(richInstalledFrom){
            "com.android.vending" -> {sparsdatedType = SparsdatedType.GooglePlay;hardcodeSparsdatedSay = "Google Play"}
            "com.fdroid.fdroid" -> {sparsdatedType = SparsdatedType.FDroid;hardcodeSparsdatedSay = "F-Droid"}
            "com.looker.droidify" -> {sparsdatedType = SparsdatedType.FDroid;hardcodeSparsdatedSay = "F-Droid"}
//            "com.github.android.apps" -> {sparsdatedType = SparsdatedType.GitHub;hardcodeSparsdatedSay = "GitHub"}
            else -> {sparsdatedType = SparsdatedType.GeneralSparsdated;hardcodeSparsdatedSay = "Unknown"}
        }
    } catch (e: Exception)
    {
        richInstalledFrom = "___"
        isSparsdated = true
        sparsdatedType = SparsdatedType.GeneralSparsdated
        e.printStackTrace()
    }

    Card(
        modifier = modifier
    ){
        Box(
            modifier = Modifier.padding(16.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = {

                    }
                )
        )
        {
            Column(

            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = stringResource(R.string.purchase_title),
                    textAlign = TextAlign.Center,
                    fontSize = 38.sp
                )
                Spacer(
                    modifier.size(16.dp)
                )
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = stringResource(R.string.purchase_I_am_rich_title),
                    textAlign = TextAlign.Center,
                )
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Text(
                        text = if(richExists) "Installed from $richInstalledFrom" else "`I am Rich` Not installed"
                    )
                    Text(
                        text = if(isSparsdated) "Pirated Copy: $hardcodeSparsdatedSay" else "Legit Google Play"
                    )
                }
                Spacer(
                    modifier.size(16.dp)
                )
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = stringResource(R.string.purchase_keep_installed),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = context.getString(R.string.purchase_contact, stringResource(R.string.source_code_site_0)),
                    textAlign = TextAlign.Center,
                    fontSize = 8.sp
                )
            }
        }
    }
}

@HTPreviewAnnotations
@Composable
fun PurchaseEmblemBoxPreview(){
    HTLauncherTheme{
        Surface(
            color = rememberColorScheme().background
        ) {
            PurchaseEmblemBox()
        }
    }
}