package com.example.transaction

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.transaction.helper.ComponentFactory
import com.example.transaction.services.DatabaseFileService
import com.example.transaction.ui.theme.TransactionTheme
import com.example.transaction.viewModels.ApplicationBarViewModel
import com.example.transaction.viewModels.BackupOptionViewModel
import com.example.transaction.viewModels.CategoryInputViewModel
import com.example.transaction.viewModels.ReportViewModel
import com.example.transaction.viewModels.SavingAccountEditViewModel
import com.example.transaction.viewModels.SavingAccountInsertViewModel
import com.example.transaction.viewModels.SavingAccountListViewModel
import com.example.transaction.viewModels.TransactionEditViewModel
import com.example.transaction.viewModels.TransactionInsertViewModel
import com.example.transaction.viewModels.TransactionListViewModel
import com.example.transaction.ui.TransactionAppUi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity  : ComponentActivity() {

    private val insertVm: TransactionInsertViewModel by viewModels()
    private val editVm: TransactionEditViewModel by viewModels()
    private val listVm: TransactionListViewModel by viewModels()
    private val reportSelectionVm: ReportViewModel by viewModels()
    private val barVm: ApplicationBarViewModel by viewModels()
    private val categoryVm: CategoryInputViewModel by viewModels()
    private val accountVm: SavingAccountListViewModel by viewModels()
    private val accountInsertVm: SavingAccountInsertViewModel by viewModels()
    private val accountEditVm: SavingAccountEditViewModel by viewModels()
    private val settingsVm: BackupOptionViewModel by viewModels()

    @Inject
    lateinit var backupService: DatabaseFileService

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        try {
            setContent {
                TransactionTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {

                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            permissions(),
                            1);

                        val componentFactory = ComponentFactory(
                            insertVm,
                            editVm,
                            listVm,
                            reportSelectionVm,
                            accountVm,
                            accountInsertVm,
                            accountEditVm
                        )

                        TransactionAppUi(componentFactory, backupService)
                    }
                }
            }
        }
        catch(e: Exception){
            e.printStackTrace()
        }
    }

    fun permissions(): Array<String> {
        val p: Array<String>
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storagePermissions33
        } else {
            p = storagePermissions
        }
        return p
    }

    var storagePermissions = arrayOf<String>(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    var storagePermissions33 = arrayOf<String>(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_VIDEO
    )
}

