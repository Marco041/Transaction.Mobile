package com.example.transaction.ui.transaction

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
import com.example.transaction.viewModels.TransactionInsertViewModel
import kotlinx.coroutines.launch


@Composable
fun TransactionInsert(viewModel: TransactionInsertViewModel){
    LaunchedEffect(key1 = true) {
        viewModel.clearState()
    }
    TransactionInput({ FormButton(viewModel) }, viewModel)
}

@Composable
fun FormButton(viewModel: TransactionInsertViewModel){

    val insertOk = stringResource(id = R.string.insert_success)
    val insertKo = stringResource(id = R.string.insert_fail)

    val composableScope = rememberCoroutineScope()

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = {
                composableScope.launch {
                    val result = viewModel.insertTransaction()
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