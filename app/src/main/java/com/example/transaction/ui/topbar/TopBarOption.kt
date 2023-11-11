package com.example.transaction.ui.topbar

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import com.example.transaction.MainActivity
import com.example.transaction.R
import com.example.transaction.services.DatabaseFileService
import kotlinx.coroutines.launch


@Composable
fun AddSettingActions(
    databaseFileService: DatabaseFileService,
    expandSettings: Boolean,
    changeExpandSettings: (Boolean) -> Unit,
    showSettingsDialog: (Boolean) -> Unit){

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val shareFile = {
        coroutineScope.launch {
            val uriToShare = databaseFileService.createZip(context)
            openShareChooser(context, "application/zip", uriToShare)
        }
    }

    val filePathToOpen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        val path = it?.path
        if(!path.isNullOrEmpty()) {
            databaseFileService.restoreDatabase(context = context, it)
            changeExpandSettings(false)
        }
    }

    IconButton(onClick = { changeExpandSettings(!expandSettings) }) {
        Icon(
            Icons.Filled.Settings,
            stringResource(id = R.string.setting),
            tint = MaterialTheme.colorScheme.inversePrimary
        )
        DropdownMenu(
            expanded = expandSettings,
            onDismissRequest = { changeExpandSettings(!expandSettings) },
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.import_db)) },
                onClick = { filePathToOpen.launch("*/*") }
            )

            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.export_db)) },
                onClick = {
                    shareFile()
                }
            )

            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.setting)) },
                onClick = { showSettingsDialog(true) }
            )
        }
    }
}

private fun getShareChooser(context: Context, template: Intent): Intent {

    val targets: MutableList<Intent> = mutableListOf()
    val candidates: List<ResolveInfo> =
        context.packageManager.queryIntentActivities(template, 0)
    val excludedComponents = ArrayList<ComponentName>()

    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R) {
        for (candidate in candidates) {
            val packageName = candidate.activityInfo.packageName
            val target = Intent(Intent.ACTION_SEND)
            target.type = "application/x-sqlite3"
            target.setPackage(packageName)
            targets.add(target)

        }
    }

    var chooserIntent: Intent? = null

    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R) {
        chooserIntent =
            Intent.createChooser(targets.removeAt(0), null)
        chooserIntent.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            targets.toTypedArray()
        )
    } else {
        chooserIntent = Intent.createChooser(template, null)
        chooserIntent
            .putExtra(Intent.EXTRA_EXCLUDE_COMPONENTS, excludedComponents.toTypedArray())
    }

    return chooserIntent
}

fun openShareChooser(context: Context, type: String, fileUri: Uri) {
    val template = Intent(Intent.ACTION_SEND)
    template.type = type
    template.putExtra(Intent.EXTRA_STREAM, fileUri)
    context.startActivity(
        getShareChooser(context, template)
    )
}

