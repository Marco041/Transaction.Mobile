package com.example.transaction.ui.transaction

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.transaction.R
import com.example.transaction.viewModels.TransactionInputViewModel
import com.example.transaction.views.common.CategorySelectionDialog
import com.example.transaction.views.common.SnackbarWithoutScaffold
import java.time.LocalDateTime


@Composable
fun TransactionInput(formCustomBtn:  @Composable () -> Unit,
           viewModel: TransactionInputViewModel){
    LaunchedEffect(key1 = true){
        viewModel.initAllCategory()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        InputTransactionForm(formCustomBtn, viewModel)
    }
}

@Composable
fun ShowTimePicker(context: Context,
                   viewModel: TransactionInputViewModel) {

    val initDate = LocalDateTime.now()
    val timePickerDialog = DatePickerDialog(
        context,
        { dp, year: Int, month : Int, day: Int ->
            viewModel.setData("$year-${month+1}-${day.toString().padStart(2, '0')}")
        },
        initDate.year, initDate.monthValue - 1, initDate.dayOfMonth
    )

    val date by viewModel.date.collectAsState()

    TextField(
        value = date,
        onValueChange = {  },
        label = { Text(
            text = stringResource(R.string.data_label) + " (*)")
        },
        readOnly = true,
        enabled = false,
        modifier = Modifier
            .clickable { timePickerDialog.show() }
            .fillMaxWidth()
    )
}


@Composable
fun InputTransactionForm(formCustomBtn:  @Composable () -> Unit,
                         viewModel: TransactionInputViewModel) {

    Column(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()) {

        ShowTimePicker(LocalContext.current, viewModel)

        val income by viewModel.income.collectAsState()
        TextField(
            value = income.toString(),
            onValueChange = { viewModel.setIncome(it) },
            label = { Text(stringResource(R.string.income_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )

        val outcome by viewModel.outcome.collectAsState()
        TextField(
            value = outcome.toString(),
            onValueChange = { viewModel.setOutcome(it) },
            label = { Text(stringResource(R.string.outcome_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )

        var openCategoryDialog by remember {
            mutableStateOf(false)
        }

        val category by viewModel.category.collectAsState()
        Box(Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = category,
                onValueChange = { viewModel.setCategory(it) },
                label = {
                    Text(
                        stringResource(R.string.category_label) + " (*)"
                    )
                }
            )
            Column(modifier = Modifier
                .clickable { openCategoryDialog = true }
                .align(CenterEnd)
                .padding(end = 10.dp)) {
                Icon(
                    Icons.Filled.List,
                    contentDescription = stringResource(id = R.string.show_category_list),
                )
            }
        }

        var openSubCategoryDialog by remember {
            mutableStateOf(false)
        }

        val subcategory by viewModel.subcategory.collectAsState()
        Box(Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = subcategory.joinToString(","),
                onValueChange = { viewModel.addNewSubCategory(it) },
                label = {
                    Text(
                        stringResource(R.string.subcategory_label)
                    )
                }
            )

            Column(modifier = Modifier
                .clickable { openSubCategoryDialog = true }
                .align(CenterEnd)
                .padding(end = 10.dp)) {
                Icon(
                    Icons.Filled.List,
                    contentDescription = stringResource(id = R.string.show_category_list),
                )
            }
        }

        if (openSubCategoryDialog) {
            SubCategoryMultiselectionDialog(
                onDismissRequest = {
                    viewModel.removeAllCategories()
                    openSubCategoryDialog = false
                },
                onConfirmation = {
                    openSubCategoryDialog = false
                },
                viewModel)
        }

        if (openCategoryDialog) {
            CategorySelectionDialog(
                onDismissRequest = {
                    openCategoryDialog = false
                },
                onConfirmation = {
                    openCategoryDialog = false
                },
                viewModel)
        }

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

@Composable
fun SubCategoryMultiselectionDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    viewModel: TransactionInputViewModel
) {

    val subcategories by viewModel.subcategory.collectAsState()
    val categories by viewModel.subcategories.collectAsState()
    val scrollState = rememberScrollState()
    val filterState by viewModel.categoriesFilter.collectAsState()

    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.subcategory_label))
        },
        text = {
            Column (
                Modifier
                    .verticalScroll(state = scrollState)
                    .fillMaxWidth()){

                TextField(
                    value = filterState,
                    onValueChange = { viewModel.filterCategories(it) })

                categories.forEach { message ->
                    val checkedState = remember { mutableStateOf(false) }
                    Row(
                    ) {
                        Checkbox(
                            checked = subcategories.contains(message.name),
                            onCheckedChange = {
                                checkedState.value = it
                                if (it) {
                                    viewModel.addSubCategory(message.name)
                                } else {
                                    viewModel.removeSubCategory(message.name)
                                }
                            }
                        )
                        Text(
                            message.name,
                            modifier = Modifier.align(CenterVertically)
                        )
                    }
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
                Text(stringResource(id = R.string.category_selection_ko_btn))
            }
        }
    )
}

@Composable
fun CategorySelectionDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    viewModel: TransactionInputViewModel
) {
    val categories by viewModel.categories.collectAsState()
    val filterState by viewModel.categoriesFilter.collectAsState()

    CategorySelectionDialog(
        categories,
        filterState,
        { viewModel.filterCategories(it) },
        { viewModel.setCategory(it) },
        onConfirmation,
        onDismissRequest)
}