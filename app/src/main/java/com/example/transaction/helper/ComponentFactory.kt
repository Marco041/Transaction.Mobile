package com.example.transaction.helper

import androidx.compose.runtime.Composable
import com.example.transaction.dto.ReportEnum
import com.example.transaction.ui.transaction.TransactionList
import com.example.transaction.viewModels.ReportViewModel
import com.example.transaction.viewModels.SavingAccountEditViewModel
import com.example.transaction.viewModels.SavingAccountInsertViewModel
import com.example.transaction.viewModels.SavingAccountListViewModel
import com.example.transaction.viewModels.TransactionEditViewModel
import com.example.transaction.viewModels.TransactionInsertViewModel
import com.example.transaction.viewModels.TransactionListViewModel
import com.example.transaction.ui.report.ReportAvgMonthlyIncomeyByYear
import com.example.transaction.ui.report.ReportCategoryByMonth
import com.example.transaction.ui.report.ReportMonthlyNet
import com.example.transaction.ui.report.ReportNetByYear
import com.example.transaction.ui.report.ReportSelection
import com.example.transaction.ui.savingaccount.SavingAccountEdit
import com.example.transaction.ui.savingaccount.SavingAccountInsert
import com.example.transaction.ui.savingaccount.SavingAccountView
import com.example.transaction.ui.transaction.TransactionEdit
import com.example.transaction.ui.transaction.TransactionInsert

class ComponentFactory (
    private val insertVm: TransactionInsertViewModel,
    private val editVm: TransactionEditViewModel,
    private val listVm: TransactionListViewModel,
    private val reportVm: ReportViewModel,
    private val savingAccountVm: SavingAccountListViewModel,
    private val savingAccountInsertVm: SavingAccountInsertViewModel,
    private val savingAccountEditVm: SavingAccountEditViewModel
) {

    @Composable
    fun CreateTransactionList( openInsertCallback: () -> Unit,
                               openEditCallback: (String) -> Unit,
                               changeBarStateCallback: (AppBarState) -> Unit) {
        return TransactionList(
            openInsertCallback,
            openEditCallback,
            { changeBarStateCallback(it) },
            listVm)
    }

    @Composable
    fun CreateTransactionEdit(id: Int, changeBarStateCallback: (AppBarState?) -> Unit) {
        return TransactionEdit(
            id,
            { changeBarStateCallback(it) },
            editVm)
    }

    @Composable
    fun CreateTransactionInsert() {
        return TransactionInsert(insertVm)
    }

    @Composable
    fun CreateSavingAccount(openInsertCallback: () -> Unit,
                            openEditCallback: (Int) -> Unit,
                            changeBarStateCallback: (AppBarState?) -> Unit){
        return SavingAccountView(
            openInsertCallback,
            openEditCallback,
            { changeBarStateCallback(it) },
            savingAccountVm
        )
    }

    @Composable
    fun CreateAddAccount(){
        SavingAccountInsert(savingAccountInsertVm)
    }

    @Composable
    fun CreateEditAccount(id: String,
                          changeBarStateCallback: (AppBarState?) -> Unit){
        SavingAccountEdit(
            id.toInt(),
            { changeBarStateCallback(null) },
            savingAccountEditVm)
    }

    @Composable
    fun CreateReportAvgMonthlyIncomeyByYear(){
        return ReportAvgMonthlyIncomeyByYear(reportVm)
    }

    @Composable
    fun CreateReportCategoryByMonth() {
        return ReportCategoryByMonth(reportVm)
    }

    @Composable
    fun CreateReportMonthlyNet() {
        return ReportMonthlyNet(reportVm)
    }

    @Composable
    fun CreateReportNetByYear() {
        return ReportNetByYear(reportVm)
    }

    @Composable
    fun CreateReportSelection(navigateToReportCallback: (ReportEnum) -> Unit) {
        return ReportSelection(navigateToReportCallback, reportVm)
    }
}