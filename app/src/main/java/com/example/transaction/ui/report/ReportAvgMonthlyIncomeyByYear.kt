package com.example.transaction.ui.report

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.transaction.viewModels.ReportViewModel
import com.example.transaction.views.LoadData
import kotlinx.coroutines.launch


@Composable
fun ReportAvgMonthlyIncomeyByYear(viewModel: ReportViewModel){
    LoadData(
        { it.launch { viewModel.loadAvgMonthlyIncomeyByYearReport() } },
        viewModel.avgMonthlyIncomeyByYear.collectAsState())
}