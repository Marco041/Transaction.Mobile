package com.example.transaction.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.transaction.dto.ApplicationBarState
import com.example.transaction.dto.NavigationCallback
import com.example.transaction.helper.AppBarState
import com.example.transaction.helper.ComponentFactory
import com.example.transaction.services.DatabaseFileService
import com.example.transaction.ui.topbar.ApplicationBar
import com.example.transaction.viewModels.BackupOptionViewModel
import kotlinx.coroutines.launch

@Composable
fun TransactionAppUi(
    componentFactory: ComponentFactory,
    databaseFileService: DatabaseFileService
) {
    val settingsViewModel: BackupOptionViewModel = viewModel()

    var appBarState: AppBarState? by remember { mutableStateOf(AppBarState()) }

    val changeAppBarAction = { it: AppBarState? ->
        appBarState = it
    }

    var appBarTitle by remember { mutableStateOf("") }

    val changeAppBarTitle = { it: String ->
        appBarTitle = it
    }

    var enabledDrawerGesture by remember { mutableStateOf(true)  }
    var canGoBack by remember { mutableStateOf(false) }

    val changeCanGoBack = { it: Boolean ->
        canGoBack = it
        enabledDrawerGesture = !canGoBack
    }

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        TransactionNavigation(navController)
    }

    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
        ?: AppRoute.TRANSACTION_ROUTE

    val sizeAwareDrawerState =  rememberDrawerState(DrawerValue.Closed)

    val navigationCallbackDto = NavigationCallback(
        navigationActions.navigateToTransaction,
        navigationActions.navigateToSavingAccount,
        navigationActions.navigateToReport)

    val barState = ApplicationBarState(
        { appBarState },
        canGoBack,
        appBarTitle,
        { navController.navigateUp(); canGoBack = false },
        { coroutineScope.launch { sizeAwareDrawerState.open() } })

    var showSettingsDialog by remember { mutableStateOf(false)  }
    val changeShowSettingDialog = { it: Boolean -> showSettingsDialog = it }

    Scaffold(
        topBar = {
            ApplicationBar(
                barState = barState,
                databaseFileService = databaseFileService,
                changeShowSettingDialog
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ModalNavigationDrawer(
                drawerContent = {
                    AppDrawer(
                        currentRoute = currentRoute,
                        navigationCallback = navigationCallbackDto,
                        closeDrawerCallback = { coroutineScope.launch { sizeAwareDrawerState.close() } }
                    )
                },
                drawerState = sizeAwareDrawerState,
                gesturesEnabled = enabledDrawerGesture
            ) {
                TransactionNavGraph(
                    componentFactory,
                    navController,
                    changeAppBarAction,
                    changeAppBarTitle,
                    changeCanGoBack
                )

                if(showSettingsDialog) {
                    AppSettings(
                        settingsViewModel,
                        changeShowSettingDialog
                    )
                }
            }
        }
    }

}