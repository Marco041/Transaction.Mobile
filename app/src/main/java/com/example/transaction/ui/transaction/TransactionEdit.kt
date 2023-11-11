package com.example.transaction.ui.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.transaction.R
import com.example.transaction.helper.AppBarState
import com.example.transaction.viewModels.TransactionEditViewModel
import kotlinx.coroutines.launch


    @Composable
    fun TransactionEdit(id: Int,
                        filtersAppBar: (AppBarState?) -> Unit,
                        viewModel: TransactionEditViewModel) {
        LaunchedEffect(key1 = true) {
            filtersAppBar(null)
            viewModel.loadTransaction(id)
        }

        TransactionInput({ FormButton(viewModel) }, viewModel)
    }

    @Composable
    fun FormButton(viewModel: TransactionEditViewModel) {

        val editOk = stringResource(id = R.string.edit_success)
        val editKo = stringResource(id = R.string.edit_fail)

        val deleteOk = stringResource(id = R.string.delete_success)
        val deleteKo = stringResource(id = R.string.delete_fail)

        val composableScope = rememberCoroutineScope()

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row() {
                Button(
                    onClick = {
                        composableScope.launch {
                            val result = viewModel.editTransaction()
                            viewModel.setSaveResult(result)
                            viewModel.setOpenSnackbar(true)
                            viewModel.setSnackBarMessaget(if (result) editOk else editKo)
                        }
                    },
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Text(text = stringResource(R.string.edit_label))
                }

                Button(
                    onClick = {
                        composableScope.launch {
                            val result = viewModel.deleteTransaction()
                            viewModel.setSaveResult(result)
                            viewModel.setOpenSnackbar(true)
                            viewModel.setSnackBarMessaget(if (result) deleteOk else deleteKo)
                            if (result) viewModel.clearState()
                        }
                    },
                    modifier = Modifier
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.inversePrimary)

                ) {
                    Text(text = stringResource(R.string.delete_label),
                    color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }