package com.example.transaction.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object AppRoute {
    const val TRANSACTION_ROUTE = "transaction"
    const val TRANSACTION_INSERT_ROUTE = "transaction/insert"
    const val TRANSACTION_EDIT_ROUTE = "transaction/edit"
    const val SAVING_ACCOUNT_ROUTE = "saving_account"
    const val SAVING_ACCOUNT_INSERT_ROUTE = "saving_account/insert"
    const val SAVING_ACCOUNT_EDIT_ROUTE = "saving_account/edit"
    const val REPORT_ROUTE = "report"
}

class TransactionNavigation(navController: NavHostController) {
    val navigateToTransaction: () -> Unit = {
        navController.navigate(AppRoute.TRANSACTION_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToSavingAccount: () -> Unit = {
        navController.navigate(AppRoute.SAVING_ACCOUNT_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToReport: () -> Unit = {
        navController.navigate(AppRoute.REPORT_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}