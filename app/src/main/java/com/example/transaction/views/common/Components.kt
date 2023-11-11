package com.example.transaction.views.common

import android.os.LocaleList
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.transaction.R
import com.example.transaction.dal.CategoryModel
import com.example.transaction.ui.theme.ErrorColor
import kotlinx.coroutines.launch
import java.time.Month
import java.time.format.TextStyle

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
        modifier = Modifier.fillMaxSize().wrapContentHeight(Alignment.Bottom),
        hostState = snackState
    ) {
        Snackbar(
            snackbarData = it,
            contentColor = if (success) MaterialTheme.colorScheme.inversePrimary else ErrorColor
        )
    }

    if (showSb) {
        LaunchedEffect(Unit) {
            snackScope.launch { snackState.showSnackbar(message) }
            openSnackbar(false)
        }
    }
}

@Composable
fun <T> DropDownList(
    itemList: List<T>,
    defaultValue: String,
    getDisplayName: (T) -> String,
    selectionCallback: (T) -> Unit,
    label: String) {

    var mExpanded by remember { mutableStateOf(false) }
    var mSelectedText by remember { mutableStateOf(defaultValue) }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(10.dp)) {
        OutlinedTextField(
            value = defaultValue,
            onValueChange = { mSelectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded })
            }
        )

        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) {
                    mTextFieldSize.width.toDp()
                })
        ) {
            itemList.forEach { label ->
                DropdownMenuItem(
                    text = { Text(getDisplayName(label)) },
                    onClick = {
                        selectionCallback(label)
                        mExpanded = false
                    })
            }
        }
    }
}

@Composable
fun PlaceActionButton(fabPosition: FabPosition,
                    onClick: () -> Unit,
                    icon: @Composable () -> Unit,
                    content: @Composable () -> Unit
) {
    Scaffold(
        floatingActionButtonPosition = fabPosition,
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = onClick,
                content = { icon() },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary,
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                content()
            }
        }
    )
}

@Composable
fun CategorySelectionDialog(
    categories: List<CategoryModel>,
    filter: String,
    filterCallback: (String) -> Unit,
    onSelectCallback: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    val scrollState = rememberScrollState()

    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.category_label))
        },
        text = {
            Column (
                Modifier
                    .verticalScroll(state = scrollState)
                    .fillMaxWidth()){

                TextField(
                    value = filter,
                    onValueChange = filterCallback)

                categories.forEach { message ->
                    Row(
                    ) {
                        Text(
                            message.name,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    onSelectCallback(message.name)
                                    onConfirmation()
                                }
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }

        },
        onDismissRequest = {
        },
        confirmButton = {
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(id = R.string.close))
            }
        }
    )
}