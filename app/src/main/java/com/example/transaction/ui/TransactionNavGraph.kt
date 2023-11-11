package com.example.transaction.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.transaction.R
import com.example.transaction.dto.ReportEnum
import com.example.transaction.helper.AppBarState
import com.example.transaction.helper.ComponentFactory
import com.example.transaction.viewModels.ApplicationBarViewModel


@Composable
fun TransactionNavGraph(
    componentFactory: ComponentFactory,
    navController: NavHostController,
    editNavBarCallback: (AppBarState?) -> Unit,
    changeNavBarTitle: (String) -> Unit,
    changeCanGoBack: (Boolean) -> Unit,
    startDestination: String = AppRoute.TRANSACTION_ROUTE,
) {
    val listLabel = stringResource(R.string.list_label)
    val insertLabel = stringResource(R.string.insert_label)
    val editLabel = stringResource(R.string.edit_label)
    val reportLabel = stringResource(R.string.report_label)
    val savingAccountLabel = stringResource(R.string.saving_account_label)
    val addSavingAccountLabel = stringResource(R.string.add_saving_account_label)
    val editSavingAccountLabel = stringResource(R.string.edit_saving_account_label)

    NavHost(
        navController = navController,
        startDestination = startDestination) {

        composable(route = AppRoute.TRANSACTION_ROUTE) {
            changeNavBarTitle(listLabel)
            componentFactory.CreateTransactionList(
                { openInsertCallback(changeCanGoBack, navController) },
                { openEditCallback(changeCanGoBack, navController, it) },
                editNavBarCallback
            )
        }

        composable(route = "${AppRoute.TRANSACTION_EDIT_ROUTE}/{id}") {
                backStackEntry ->
            changeNavBarTitle(editLabel)
            backStackEntry.arguments?.getString("id")?.let { it1 ->
                componentFactory.CreateTransactionEdit(it1.toInt(), editNavBarCallback)
            }
        }

        composable(AppRoute.TRANSACTION_INSERT_ROUTE) {
            changeNavBarTitle(insertLabel)
            componentFactory.CreateTransactionInsert()
        }


        composable(AppRoute.SAVING_ACCOUNT_ROUTE) {
            changeNavBarTitle(savingAccountLabel)
            componentFactory.CreateSavingAccount(
                { openAddSavingAccountCallback(changeCanGoBack, navController) },
                { openEditSavingAccountCallback(changeCanGoBack, navController, it.toString()) },
                editNavBarCallback)
        }

        composable(AppRoute.SAVING_ACCOUNT_INSERT_ROUTE) {
            changeNavBarTitle(addSavingAccountLabel)
            componentFactory.CreateAddAccount()
        }

        composable("${AppRoute.SAVING_ACCOUNT_EDIT_ROUTE}/{id}") {
                backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let { it1 ->
                changeNavBarTitle(editSavingAccountLabel)
                componentFactory.CreateEditAccount(it1, editNavBarCallback)
            }
        }

        composable(ReportEnum.MonthlyAvgByYear.toString()) {
            changeNavBarTitle(reportLabel)
            componentFactory.CreateReportAvgMonthlyIncomeyByYear()
        }

        composable(AppRoute.REPORT_ROUTE) {
            changeNavBarTitle(reportLabel)
            componentFactory.CreateReportSelection {
                openReportCallback(changeCanGoBack,  navController, it)
            }
        }

        composable(ReportEnum.ByCategory.toString()) {
            changeNavBarTitle(reportLabel)
            componentFactory.CreateReportCategoryByMonth()
        }

        composable(ReportEnum.MonthlyNet.toString()) {
            changeNavBarTitle(reportLabel)
            componentFactory.CreateReportMonthlyNet()
        }

        composable(ReportEnum.NetByYear.toString()) {
            changeNavBarTitle(reportLabel)
            componentFactory.CreateReportNetByYear()
        }

        composable(ReportEnum.MonthlyAvgByYear.toString()) {
            changeNavBarTitle(reportLabel)
            componentFactory.CreateReportAvgMonthlyIncomeyByYear()
        }
    }
}

fun openInsertCallback(canNavigateBackCallback: (Boolean) -> Unit,
                       navController: NavHostController) {
    canNavigateBackCallback(true)
    navController.navigate(AppRoute.TRANSACTION_INSERT_ROUTE)
}

fun openEditCallback(canNavigateBackCallback: (Boolean) -> Unit,
                     navController: NavHostController,
                     transactionId: String) {
        canNavigateBackCallback(true)
        navController.navigate("${AppRoute.TRANSACTION_EDIT_ROUTE}/$transactionId")
}

fun openReportCallback(canNavigateBackCallback: (Boolean) -> Unit,
                     navController: NavHostController,
                     reportName: ReportEnum) {
    canNavigateBackCallback(true)
    navController.navigate(reportName.toString())
}

fun openAddSavingAccountCallback(canNavigateBackCallback: (Boolean) -> Unit,
                                 navController: NavHostController) {
    canNavigateBackCallback(true)
    navController.navigate(AppRoute.SAVING_ACCOUNT_INSERT_ROUTE)
}

fun openEditSavingAccountCallback(canNavigateBackCallback: (Boolean) -> Unit,
                                  navController: NavHostController,
                                  accountId: String) {
    canNavigateBackCallback(true)
    navController.navigate("${AppRoute.SAVING_ACCOUNT_EDIT_ROUTE}/$accountId")
}