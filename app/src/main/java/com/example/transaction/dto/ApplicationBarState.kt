package com.example.transaction.dto

import androidx.compose.runtime.Composable
import com.example.transaction.helper.AppBarState

data class ApplicationBarState(
    val changeAppBarOptionState: () -> AppBarState?,
    val canGoBack: Boolean,
    val title: String,
    val goBackCallback: () -> Unit,
    val changeDrawerCallback: () -> Unit
)