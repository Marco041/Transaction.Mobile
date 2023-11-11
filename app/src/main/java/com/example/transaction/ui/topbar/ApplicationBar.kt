package com.example.transaction.ui.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.transaction.dto.ApplicationBarState
import com.example.transaction.services.DatabaseFileService


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ApplicationBar(
    barState: ApplicationBarState,
    databaseFileService: DatabaseFileService,
    changeShowSettingDialog: (Boolean) -> Unit
){
    var expandSettings by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(text = barState.title, color = MaterialTheme.colorScheme.inversePrimary)
        },
        navigationIcon = {
            NavBarLeftIconHandler(
                barState.canGoBack,
                { barState.goBackCallback() },
                { barState.changeDrawerCallback() }
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.inversePrimary,
        ),
        actions = {
            IconButton(onClick = { barState.changeDrawerCallback() }) { }

            if(barState.changeAppBarOptionState() != null) {
                barState.changeAppBarOptionState()!!.actions?.invoke(this)
            }

            AddSettingActions(
                databaseFileService,
                expandSettings,
                { expandSettings = it },
                changeShowSettingDialog
            )
        }
    )
}

@Composable
fun NavBarLeftIconHandler(canNavigateBack: Boolean,
                          canNavigateBackClick: () -> Unit,
                          openMenuCallback: () -> Unit) {
    if (canNavigateBack) {
        IconButton(onClick = canNavigateBackClick)
        {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = ""
            )
        }
    }
    else {
        IconButton(onClick = openMenuCallback) {
            Icon(
                Icons.Filled.Menu,
                "Menu",
                tint = MaterialTheme.colorScheme.inversePrimary
            )
        }
    }
}




