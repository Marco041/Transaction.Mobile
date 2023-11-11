package com.example.transaction.ui.report

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.transaction.R
import com.example.transaction.dal.CategoryModel
import com.example.transaction.dto.ReportEnum
import com.example.transaction.dto.ReportSelectionItem
import com.example.transaction.viewModels.ReportViewModel
import com.example.transaction.views.common.CategorySelectionDialog
import com.example.transaction.views.common.DropDownList

fun getReportDescription(report: ReportEnum): Int{
    return when(report){
        ReportEnum.ByCategory -> R.string.report_1
        ReportEnum.MonthlyNet -> R.string.report_2
        ReportEnum.NetByYear -> R.string.report_3
        ReportEnum.MonthlyAvgByYear -> R.string.report_4
    }
}

@Composable
fun ReportSelection(navigateToReportCallback: (ReportEnum) -> Unit,
                    viewModel: ReportViewModel){

    LaunchedEffect(key1 = true){
        viewModel.initAllCategory()
    }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        ReportDropDownSelection(viewModel)

        Spacer(Modifier.height(10.dp))

        val showReportOption by viewModel.showReportOption.collectAsState()

        if(showReportOption) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Row() {
                    Checkbox(
                        checked = viewModel.groupByYear.collectAsState().value,
                        onCheckedChange = {
                            viewModel.setGroupByYear(it)
                        }
                    )
                    Text(
                        stringResource(id = R.string.group_by_year_label),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Spacer(Modifier.height(5.dp))

                Row() {
                    Checkbox(
                        checked = viewModel.groupBySubcategory.collectAsState().value,
                        onCheckedChange = {
                            viewModel.setGroupBySubcategory(it)
                        }
                    )
                    Text(
                        stringResource(id = R.string.group_by_subcategory_label),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                val filter by viewModel.categoryFilter.collectAsState()
                val categories by viewModel.categories.collectAsState()
                val filterState by viewModel.categoriesFilter.collectAsState()
                var openCategoryDialog by remember { mutableStateOf(false) }

                if (openCategoryDialog) {
                    CategoryDialog(
                        categories,
                        filterState,
                        onDismissRequest = {
                            openCategoryDialog = false
                        },
                        onConfirmation = {
                            openCategoryDialog = false
                        },
                        viewModel)
                }

                Box(Modifier.fillMaxWidth()) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = filter,
                        onValueChange = { viewModel.setCategoryFilter(it) },
                        label = {
                            Text(
                                stringResource(R.string.category_filter)
                            )
                        }
                    )
                    Column(modifier = Modifier
                        .clickable { openCategoryDialog = true }
                        .align(Alignment.CenterEnd)
                        .padding(end = 10.dp)) {
                        Icon(
                            Icons.Filled.List,
                            contentDescription = stringResource(id = R.string.show_category_list),
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(15.dp))

        val selectedReport = viewModel.selectedReport.collectAsState()
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    navigateToReportCallback(selectedReport.value)
                },
                modifier = Modifier
                    .padding(vertical = 10.dp)
            ) {
                Text(text = stringResource(R.string.show_report_label))
            }
        }
    }
}

@Composable
fun ReportDropDownSelection(viewModel: ReportViewModel){
    val reports = listOf(
        ReportSelectionItem(
            stringResource(id = getReportDescription(ReportEnum.MonthlyNet) ),
            ReportEnum.MonthlyNet),
        ReportSelectionItem(
            stringResource(id = getReportDescription(ReportEnum.ByCategory) ),
            ReportEnum.ByCategory),
        ReportSelectionItem(
            stringResource(id = getReportDescription(ReportEnum.NetByYear) ),
            ReportEnum.NetByYear),
        ReportSelectionItem(
            stringResource(id = getReportDescription(ReportEnum.MonthlyAvgByYear) ),
            ReportEnum.MonthlyAvgByYear))


    DropDownList(
        reports,
        stringResource(id = getReportDescription(viewModel.selectedReport.collectAsState().value)),
        {
            it.text
        },
        {
            viewModel.setReport(it.value)
        },
        stringResource(id = R.string.report_selection_label)
    )
}

@Composable
fun CategoryDialog(categories: List<CategoryModel>,
                   filterState: String,
                   onDismissRequest: () -> Unit,
                   onConfirmation: () -> Unit,
                   viewModel: ReportViewModel){

    CategorySelectionDialog(
        categories,
        filterState,
        { viewModel.filterCategories(it) },
        { viewModel.setCategoryFilter(it) },
        onConfirmation,
        onDismissRequest)
}