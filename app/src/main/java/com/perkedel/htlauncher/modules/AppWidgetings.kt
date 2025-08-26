package com.perkedel.htlauncher.modules

import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.perkedel.htlauncher.HTUIState
import com.perkedel.htlauncher.HTViewModel
import com.perkedel.htlauncher.R

// https://stackoverflow.com/questions/77532675/how-to-host-and-draw-installed-app-widgets-in-a-compose-app
// THERE IS NO FUCKING DOCUMENT ABOUT IT!!!!
// FFS GIVE ME THE FUCKING ONE WILL YA?!?!??!?!
// that above asked an AI to prompt vibe it for them. This is even worse, but that's all we have!

@Composable
fun AppWidgetHostView(
    context: Context = LocalContext.current,
    appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(context),
//    appWidgetId: Int = integerResource(R.id.APPWIDGET_HOST_ID),
    appWidgetId: Int = 1,
    appWidgetHost: AppWidgetHost = AppWidgetHost(context, appWidgetId),
    appWidgetInfo: AppWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId),
    viewModel: HTViewModel = HTViewModel(),
    uiState: HTUIState = HTUIState(),
    modifier: Modifier = Modifier
) {
    val appWidgetHostView = appWidgetHost.createView(
        context,
        appWidgetId,
        appWidgetManager.getAppWidgetInfo(appWidgetId)
    )

    DisposableEffect(appWidgetHostView) {
        onDispose {
            appWidgetHost.deleteAppWidgetId(appWidgetId)
        }
    }

    LaunchedEffect(
        appWidgetId,
        appWidgetInfo,
    ) {
        Log.d("Widgeting","!!! AppWidgetHostView LaunchedEffect")
        Log.d("Widgeting","!!! Providing now ${appWidgetInfo.toString()}")

        // set the view
        appWidgetHostView.setAppWidget(appWidgetId, appWidgetInfo)
    }

    // Measure the app widget size
    appWidgetHostView.measure(
        View.MeasureSpec.makeMeasureSpec(100.dp.value.toInt(), View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(100.dp.value.toInt(), View.MeasureSpec.EXACTLY)
    )

    // Layout the app widget
    appWidgetHostView.layout(
        0,
        0,
        appWidgetHostView.measuredWidth,
        appWidgetHostView.measuredHeight
    )

    // TODO: Fix unrefreshable changing widget

    // Draw the app widget
    AndroidView(
        factory = { appWidgetHostView },
        modifier = modifier,
//        update = {}
    )
}

/*
@Composable
fun AppWidgetListItem(appWidget: AppWidget, appWidgetHostId: Int) {
    val context = LocalContext.current
    val appWidgetHost = AppWidgetHost(context, appWidgetHostId)
    val appWidgetManager = AppWidgetManager.getInstance(context)

    val (isExpanded, setExpanded) = remember { mutableStateOf(false) }
//    val remoteViews = appWidgetManager.getAppWidgetViews(appWidget.appWidgetId)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
            .clickable { setExpanded(!isExpanded) }
    ) {
        Text(text = appWidget.appWidgetInfo.label)

        if (isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                AppWidgetHostView(
                    appWidgetManager = appWidgetManager,
                    appWidgetHost = appWidgetHost,
                    appWidgetId = appWidget.appWidgetId,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetsView(
    modifier: Modifier = Modifier,
    appWidgetHost: AppWidgetHost,
    appWidgetManager: AppWidgetManager,
    appWidgetHostId: Int
) {
    val context = LocalContext.current

    val appWidgetIds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        appWidgetHost.appWidgetIds
    } else {
        TODO("VERSION.SDK_INT < O")
//        appWidgetHost.appWidgetIds
    }
    val appWidgets = appWidgetIds.map { appWidgetId ->
        val appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId)
        if (appWidgetInfo != null) {
            AppWidget(appWidgetId, appWidgetInfo)
        } else {
            null
        }
    }.mapNotNull { it }

    Column {
        TopAppBar(
            title = {
                Text(text = "Installed Widgets")
            },
            actions = {
                IconButton(onClick = { /* Handle settings action */ }) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null)
                }
            }
        )

        LazyColumn {
            items(appWidgets.size) { i ->
                AppWidgetListItem(appWidgets[i], appWidgetHostId)
            }
        }
    }
}
*/