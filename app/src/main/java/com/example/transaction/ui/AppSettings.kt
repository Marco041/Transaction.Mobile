package com.example.transaction.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.transaction.R
import com.example.transaction.dto.TransactionSetting
import com.example.transaction.viewModels.BackupOptionViewModel
import kotlinx.coroutines.launch

@Composable
fun AppSettings(viewModel: BackupOptionViewModel,
                changeVisibilityCallback: (Boolean) -> Unit){

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            viewModel.initState(context)
        }
    }

    val zipBackup = viewModel.zipBackup.collectAsState()
    val password = viewModel.zipPassword.collectAsState()
    val changeZipBackup = { it: String -> viewModel.setZipBackup(it) }
    val changeZipPassword = { it: String -> viewModel.setZipPassword(it) }

    val storeData =
        {
            coroutineScope.launch {
                viewModel.storeSettings(
                    context,
                    TransactionSetting(zipBackup.value, password.value)
                )
            }
        }

    BackupSettingDialog(zipBackup.value,
        password.value,
        changeZipBackup,
        changeZipPassword,
        { changeVisibilityCallback(false) },
        {
            storeData()
            changeVisibilityCallback(false)
        }
    )
}

@Composable
fun BackupSettingDialog(
    zipBackup: String,
    password: String,
    changeZipBackup: (String) -> Unit,
    changeZipPassword: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    val scrollState = rememberScrollState()

    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.filters))
        },
        text = {
            Column (
                Modifier
                    .verticalScroll(state = scrollState)
                    .fillMaxWidth()){

                Row() {
                    Checkbox(
                        checked = zipBackup.toBoolean(),
                        onCheckedChange = {
                            changeZipBackup(it.toString())
                        }
                    )
                    Text(
                        stringResource(id = R.string.backup_zip),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Row() {
                    TextField(
                        value = password,
                        label = { Text(stringResource(id = R.string.backup_zip_password)) },
                        onValueChange = { changeZipPassword(it) },
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }

        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(id = R.string.category_selection_ok_btn))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(id = R.string.cancel_label))
            }
        }
    )
}