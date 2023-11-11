package com.example.transaction.ui.transaction

import android.os.LocaleList
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.transaction.R
import com.example.transaction.dal.TransactionWithCategory
import com.example.transaction.helper.AppBarState
import com.example.transaction.viewModels.TransactionListViewModel
import com.example.transaction.views.common.DropDownList
import com.example.transaction.views.common.PlaceActionButton
import java.time.Month
import java.time.format.TextStyle

@Composable
fun TransactionList  (
    addCallback: () -> Unit,
    editCallback: (String) -> Unit,
    filtersAppBar: (AppBarState) -> Unit,
    viewModel: TransactionListViewModel){

    var openFilterDialog by remember {
        mutableStateOf(false)
    }

    val transactions =
        viewModel.transactionPager.collectAsLazyPagingItems()

    LaunchedEffect(key1 = true) {
        filtersAppBar(
            EditAppBar { openFilterDialog = it }
        )
    }
    RenderFilterDialog(openFilterDialog, { openFilterDialog = it }, viewModel)
    RenderTransactionList(transactions = transactions, addCallback, editCallback)
}

fun EditAppBar(updateOpenFilterDialog: (Boolean) -> Unit): AppBarState{
    return AppBarState(
        actions = {
            IconButton(onClick = { updateOpenFilterDialog(true) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.inversePrimary
                )
            }
        }
    )
}

@Composable
fun RenderFilterDialog(openFilterDialog: Boolean,
                       updateOpenFitlerDialog: (Boolean) -> Unit,
                       viewModel: TransactionListViewModel){
    if (openFilterDialog) {
        FilterDialog(
            onDismissRequest = {
                updateOpenFitlerDialog(false)
                viewModel.resetFilter()
            },
            onConfirmation = {
                updateOpenFitlerDialog(false)
                viewModel.setFilterTransaction()
            },
        viewModel)
    }
}

 @Composable
 fun RenderTransactionList(transactions: LazyPagingItems<TransactionWithCategory>,
                           addTransactionCallback: () -> Unit,
                           editCallback: (String) -> Unit){
     PlaceActionButton(
         FabPosition.End, addTransactionCallback,
         {
             Icon(
                 Icons.Filled.Add,
                 contentDescription = stringResource(id = R.string.add_saving_account_label)
             )
         },
         {
             Column(
                 modifier = Modifier.fillMaxSize(),
                 horizontalAlignment = Alignment.Start
             ) {
                 when(transactions.loadState.refresh) {
                     LoadState.Loading -> {
                     }
                     is LoadState.Error -> {
                     }
                     else -> {
                         LazyTransactionList(transactions, editCallback)
                     }
                 }
             }
         }
     )
 }

@Composable
fun LazyTransactionList(transactions: LazyPagingItems<TransactionWithCategory>,
                        editCallback: (String) -> Unit){
    LazyColumn(Modifier.padding(10.dp)) {
        items(count = transactions.itemCount) { index ->
            val item = transactions[index]
            TransactionCard(item!!, editCallback)
        }
    }
}

@Composable
fun TransactionCard(msg: TransactionWithCategory,
                    editCallback: (String) -> Unit) {
    Column(Modifier.padding(2.dp)) {
        Row (Modifier.clickable{ editCallback(msg.transaction.id.toString()) } ) {
            Text(
                text = msg.transaction.data,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = msg.categoryList.joinToString(
                    separator = ", ",
                    transform = { it.name } ),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            if(msg.transaction.income > 0) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "+" + msg.transaction.income.toString(), color = Color.Green)
            }

            if(msg.transaction.outcome > 0) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "-" + msg.transaction.outcome.toString(), color = Color.Red)
            }
        }
        Text(
            text = msg.transaction.description.toString(),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleSmall
        )
    }

    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
fun FilterDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    viewModel: TransactionListViewModel
) {
    val scrollState = rememberScrollState()
    val textFilter = viewModel.textFilter.collectAsState()
    val yearFilter = viewModel.yearFilter.collectAsState()

    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.filters))
        },
        text = {
            Column (
                Modifier
                    .verticalScroll(state = scrollState)
                    .fillMaxWidth()){

                DialogTextFilter(stringResource(
                    id = R.string.text_filter),
                    textFilter.value
                ) { viewModel.setTexFilter(it) }

                DialogTextFilter(stringResource(
                    id = R.string.year),
                    yearFilter.value
                ) { viewModel.setYearFilter(it) }

                val locale = LocaleList.getDefault().get(0);
                DropDownList(
                    Month.values().toList(),
                    viewModel.monthFilter.collectAsState().value,
                    {
                        it.getDisplayName(TextStyle.FULL, locale)
                    },
                    {
                        viewModel.setMonthFilter(it.getDisplayName(TextStyle.FULL, locale))
                        viewModel.setYearNumericFilter(it.value)
                    },
                    stringResource(id = R.string.month)
                )
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
                Text(stringResource(id = R.string.category_selection_ko_btn))
            }
        }
    )
}

@Composable
fun DialogTextFilter(
    label: String,
    textValue: String,
    onTextChange: (String) -> Unit){

    TextField(
        value = textValue,
        label = { Text(label) },
        onValueChange = { onTextChange(it) },
        modifier = Modifier.padding(vertical = 10.dp)
    )
}
