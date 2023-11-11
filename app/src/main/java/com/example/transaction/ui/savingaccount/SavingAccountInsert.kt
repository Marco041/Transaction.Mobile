package com.example.transaction.ui.savingaccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.transaction.R
import com.example.transaction.viewModels.SavingAccountInsertViewModel
import kotlinx.coroutines.launch


@Composable
fun SavingAccountInsert(viewModel: SavingAccountInsertViewModel){
    LaunchedEffect(key1 = true) {
        viewModel.clearState()
    }

    SavingAccountInput({ FormButton(viewModel) }, viewModel)
}

@Composable
fun FormButton(viewModel: SavingAccountInsertViewModel){

    val insertOk = stringResource(id = R.string.insert_success)
    val insertKo = stringResource(id = R.string.insert_fail)

    val composableScope = rememberCoroutineScope()

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                composableScope.launch {
                    val result = viewModel.insertSavingAccount()
                    viewModel.setSaveResult(result)
                    viewModel.setOpenSnackbar(true)
                    viewModel.setSnackBarMessaget(if (result) insertOk else insertKo)
                }
            },
            modifier = Modifier
                .padding(vertical = 10.dp)
        ) {
            Text(text = stringResource(R.string.insert_label))
        }
    }
}