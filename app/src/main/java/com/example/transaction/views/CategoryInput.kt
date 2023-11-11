package com.example.transaction.views

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.transaction.R
import com.example.transaction.ui.theme.ErrorColor
import com.example.transaction.viewModels.CategoryInputViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class CategoryInput(
    private val viewModel: CategoryInputViewModel
)
{

    @Composable
    fun Render(){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            viewModel.clearState()
            InsertCategoryForm()

        }
    }

    @Composable
    fun InsertCategoryForm() {

        val coroutineScope = rememberCoroutineScope()

        Column(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()) {

            val name by viewModel.name.collectAsState()
            TextField(
                value = name,
                onValueChange = { viewModel.setName(it) },
                label = {
                    Text(
                        text = stringResource(R.string.name),
                        color = Color.Black
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            val showSb = viewModel.showMessage.collectAsState()
            val sbMessage = viewModel.insertMessage.collectAsState()
            var insertResult by remember { mutableStateOf(false) }
            
            Button(onClick = {
                coroutineScope.launch {
                        insertResult = viewModel.insertCategory()
                    }
            },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 10.dp)) {
                Text(text = stringResource(R.string.insert_label))
            }

            SnackbarWithoutScaffold(
                message = sbMessage.value,
                showSb = showSb.value,
                openSnackbar = { viewModel.setShowtMessage(it) },
                insertResult)
        }
    }

    @Composable
    fun SnackbarWithoutScaffold(
        message: String,
        showSb: Boolean,
        openSnackbar: (Boolean) -> Unit,
        success: Boolean
    ) {

        val snackState = remember { SnackbarHostState() }
        val snackScope = rememberCoroutineScope()
        SnackbarHost(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(Alignment.Bottom),
            hostState = snackState
        ){
            Snackbar(
                snackbarData = it,
                contentColor = if(success) Color.White else ErrorColor
            )
        }

        if (showSb){
            LaunchedEffect(Unit) {
                snackScope.launch { snackState.showSnackbar(message) }
                openSnackbar(false)
            }
        }
    }
}