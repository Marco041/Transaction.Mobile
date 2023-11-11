package com.example.transaction.ui.savingaccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.transaction.R
import com.example.transaction.viewModels.SavingAccountInputViewModel
import com.example.transaction.views.common.SnackbarWithoutScaffold


@Composable
fun SavingAccountInput(formCustomBtn:  @Composable () -> Unit,
           viewModel: SavingAccountInputViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        InputSavingAccountForm(formCustomBtn, viewModel)
    }
}

@Composable
fun InputSavingAccountForm(formCustomBtn:  @Composable () -> Unit,
                           viewModel: SavingAccountInputViewModel) {

    Column(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()) {

        val name by viewModel.name.collectAsState()
        TextField(
            value = name,
            onValueChange = { viewModel.setName(it) },
            label = {
                Text(
                    text = stringResource(R.string.name_label))
            },
            modifier = Modifier.fillMaxWidth()
        )

        val amount by viewModel.amount.collectAsState()
        TextField(
            value = amount.toString(),
            onValueChange = { viewModel.setAmount(it) },
            label = { Text(stringResource(R.string.amount_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )

        val yield by viewModel.yield.collectAsState()
        TextField(
            value = yield.toString(),
            onValueChange = { viewModel.setYield(it) },
            label = { Text(stringResource(R.string.yield_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )

        val desc by viewModel.description.collectAsState()
        TextField(
            value = desc,
            onValueChange = { viewModel.setDescription(it) },
            label = {
                Text(
                    text = stringResource(R.string.description_label))
            },
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
        )

        formCustomBtn()

        val message = viewModel.snackBarMessage.collectAsState()
        val showSb = viewModel.openSnackbar.collectAsState()
        val saveResult = viewModel.saveResult.collectAsState()

        SnackbarWithoutScaffold(
            message = message.value,
            showSb = showSb.value,
            openSnackbar = { viewModel.setOpenSnackbar(it) },
            saveResult.value)
    }
}