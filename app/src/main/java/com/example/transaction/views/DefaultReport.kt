package com.example.transaction.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.transaction.dal.ReportModel
import com.example.transaction.viewModels.ReportViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat


@Composable
fun ReportItemCard(item: ReportModel) {

    if(item.category.isNullOrEmpty() && item.year.isNullOrEmpty()){
        return
    }

    Column(Modifier.padding(top = 6.dp, start = 8.dp, end = 3.dp)) {

        if(!item.year.isNullOrEmpty() && !item.month.isNullOrEmpty()) {
            Row {
                Text(
                    item.month.toString() + "/" + item.year.toString(),
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
        Row(Modifier.fillMaxWidth().padding(1.dp)) {
            if (!item.category.isNullOrEmpty()) {
                Text(item.category.toString())
            }
            if (!item.year.isNullOrEmpty() && item.month.isNullOrEmpty()) {
                Text(item.year, modifier = Modifier.padding(start = 10.dp))
            }
        }
        Row(Modifier.fillMaxWidth().padding(1.dp)) {
            if(item.income != null) {
                Text(
                    "+" + DecimalFormat("#,##0.00").format(item.income),
                    Modifier.padding(start = 10.dp),
                    color = Color.Green
                )
            }
            if(item.outflow != null) {
                Text(
                    "-" + DecimalFormat("#,##0.00").format(item.outflow),
                    Modifier.padding(start = 10.dp),
                    color = Color.Red
                )
            }

            if(item.net != null) {
                Text(
                    DecimalFormat("#,##0.00").format(item.net),
                    Modifier.padding(start = 10.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LoadData(loadDataCallback: (CoroutineScope) -> Unit, data: State<List<ReportModel>>){

    val composableScope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        composableScope.launch {
            loadDataCallback(composableScope)
        }
    }

    LazyColumn {
        items(data.value) {
            ReportItemCard(item = it)
        }
    }
}