package com.example.transaction.ui.savingaccount

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.transaction.R
import com.example.transaction.dal.SavingAccountModel
import com.example.transaction.helper.AppBarState
import com.example.transaction.viewModels.SavingAccountListViewModel
import com.example.transaction.views.common.PlaceActionButton
import kotlinx.coroutines.launch
import java.text.DecimalFormat


@Composable
fun SavingAccountView(addCallback: () -> Unit,
                      editCallback: (Int) -> Unit,
                      filtersAppBar: (AppBarState?) -> Unit,
                      viewModel: SavingAccountListViewModel){

    val composableScope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        filtersAppBar(null)

        composableScope.launch {
            viewModel.loadData()
        }
    }

    val totalYieldAmount = viewModel.totalYieldAmount.collectAsState().value
    val totalYieldPercentage = viewModel.totalYieldPercentage.collectAsState().value
    val total = viewModel.total.collectAsState().value
    val accounts = viewModel.accountList.collectAsState().value

    PlaceActionButton(
        FabPosition.End, addCallback,
        {
            Icon(
                Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.add_saving_account_label)
            )
        },
        {
            Column(Modifier.padding(8.dp)) {
                if (total != null) {
                    Column() {
                        Row() {
                            Text(
                                text = stringResource(id = R.string.total_accounts)

                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = DecimalFormat("#,##0.00").format(total),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row() {
                            Text(
                                text = stringResource(id = R.string.total_accounts_yield)

                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = DecimalFormat("#,##0.00").format(totalYieldAmount)
                                        + (" (${DecimalFormat("#,##0.00").format(totalYieldPercentage)}%)"),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(65.dp))

                LazyColumn(Modifier.padding(8.dp)) {
                    items(accounts) {
                        if (!it.name.isNullOrEmpty()) {
                            AccountCard(it, editCallback)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun AccountCard(msg: SavingAccountModel, editCallback: (Int) -> Unit) {
    Column(Modifier.padding(2.dp)) {
        Row (Modifier.clickable{ editCallback(msg.id) } ) {
            Text(
                text = msg.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = DecimalFormat("#,##0.00").format(msg.amount), color = Color.Green)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = msg.yield.toString() + "%")
        }
        Text(
            text = msg.description.toString(),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleSmall
        )
    }

    Spacer(modifier = Modifier.height(5.dp))
}
