@file:OptIn(ExperimentalMaterial3Api::class)

package com.perkedel.htlauncher.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.perkedel.htlauncher.R
import com.perkedel.htlauncher.data.HomepagesWeHave
import com.perkedel.htlauncher.enumerations.ButtonTypes
import com.perkedel.htlauncher.ui.bars.HTAppBar
import com.perkedel.htlauncher.ui.dialog.HTAlertDialog
import com.perkedel.htlauncher.ui.previews.HTPreviewAnnotations
import com.perkedel.htlauncher.ui.theme.HTLauncherTheme
import me.zhanghai.compose.preference.Preference
import my.nanihadesuka.compose.LazyVerticalGridScrollbar
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun GoToPageDialog(
    modifier: Modifier = Modifier,
    listTypeView: Boolean = true,
    howManyPagesYouHave: Int = 0,
    configFile: HomepagesWeHave? = null,
    onSelectPage: (Int) -> Unit = {},
    onCancel: () -> Unit = {},
)
{
    val lazyListState = rememberLazyGridState()
    HTAlertDialog(
        title = stringResource(R.string.GoToPage_Title),
        text = stringResource(R.string.GoToPage_Description),
        dismissText = stringResource(R.string.Cancel_button),
        selectIcon = Icons.Default.DoubleArrow,
        modifier = modifier,
        confirmButton = false,
        onDismissRequest = onCancel,
    )
    {
        Row {
            Spacer(
                modifier.weight(1f)
            )
            HTButton(
                modifier = Modifier,
                onClick = onCancel,
                buttonType = ButtonTypes.TextButton,
                title = stringResource(R.string.Cancel_button)
            )
        }

        OutlinedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // lazy vertical grid werrors idk anymore
            if(listTypeView)
            {
                Column {
                    repeat(
                        times = howManyPagesYouHave
                    )
                    {
                        HTButton(
                            title = "${it+1}: ${configFile?.pagesPath?.get(it)}",
//                            title = "${it+1}",
                            modifier = Modifier
                                .fillMaxWidth()
                                    ,
                            onClick = {
                                onSelectPage(it)
                            }
                        )
                    }
                }
            } else
            {
                Column {
                    repeat(
                        times = ceil((howManyPagesYouHave/3).toDouble()).toInt()+1
                    ){
                        if(it <= ceil((howManyPagesYouHave/3).toDouble()).toInt()+1 )
                            Row {
                                HTButton(
                                    title = "${it*3+1}",
                                    onClick = {
                                        onSelectPage(it*3)
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                    ,
                                )
                                if(it+1 <= ceil((howManyPagesYouHave/3).toDouble()).toInt() )
                                    HTButton(
                                        title = "${it*3+2}",
                                        onClick = {
                                            onSelectPage(it*3+1)
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                        ,
                                    )

                                if(it+2 <= ceil((howManyPagesYouHave/3).toDouble()).toInt() +1)
                                    HTButton(
                                        title = "${it*3+3}",
                                        onClick = {
                                            onSelectPage(it*3+2)
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                        ,
                                    )
                            }
                    }
                }
            }
        }
    }
}

@HTPreviewAnnotations
@Composable
fun PreviewGoToPageDialog()
{
    HTLauncherTheme {
        Scaffold(
            modifier = Modifier,
            topBar = { HTAppBar() },
        ){ innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
            ){
                GoToPageDialog(
                    howManyPagesYouHave = 10,
                )
            }
        }
    }
}