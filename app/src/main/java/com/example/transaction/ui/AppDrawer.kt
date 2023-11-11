package com.example.transaction.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.transaction.R
import com.example.transaction.dto.NavigationCallback

@Composable
fun AppDrawer(
    currentRoute: String,
    navigationCallback: NavigationCallback,
    closeDrawerCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier) {
        Spacer(Modifier.height(12.dp))

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.list_label)) },
            icon = { Icon(Icons.Filled.List, null) },
            selected = currentRoute == AppRoute.TRANSACTION_ROUTE,
            onClick = { navigationCallback.navigateToTransaction(); closeDrawerCallback() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.saving_account_label)) },
            icon = { Icon(Icons.Filled.Info, null) },
            selected = currentRoute == AppRoute.SAVING_ACCOUNT_ROUTE,
            onClick = { navigationCallback.navigateToSavingAccount(); closeDrawerCallback() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.report_label)) },
            icon = { Icon(Icons.Filled.Search, null) },
            selected = currentRoute == AppRoute.REPORT_ROUTE,
            onClick = { navigationCallback.navigateToReport(); closeDrawerCallback() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
