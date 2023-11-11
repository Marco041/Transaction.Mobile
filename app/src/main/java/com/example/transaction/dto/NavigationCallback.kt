package com.example.transaction.dto

data class NavigationCallback(
    val navigateToTransaction: () -> Unit,
    val navigateToSavingAccount: () -> Unit,
    val navigateToReport: () -> Unit
)