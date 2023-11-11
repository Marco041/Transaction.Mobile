package com.example.transaction.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.example.transaction.dal.TransactionDao
import com.example.transaction.TransactionPagingHelper
import com.example.transaction.dal.ReportModel
import com.example.transaction.dto.TransactionListFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dao: TransactionDao
) : ViewModel() {

    val PAGE_SIZE = 50

    var transactionPager = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        TransactionPagingHelper(null, dao)
    }.flow

    var textFilter = savedStateHandle.getStateFlow("textFilter", "")
        private set

    fun setTexFilter(texFilterValue: String){
        savedStateHandle["textFilter"] = texFilterValue
    }

    var monthFilter = savedStateHandle.getStateFlow("monthFilter", "")
        private set

    fun setMonthFilter(monthFilterValue: String){
        savedStateHandle["monthFilter"] = monthFilterValue
    }

    var yearFilter = savedStateHandle.getStateFlow("yearFilter", "")
        private set

    fun setYearFilter(yearFilterValue: String){
        savedStateHandle["yearFilter"] = yearFilterValue
    }

    var yearNumericFilter = savedStateHandle.getStateFlow("yearNumericFilter", null)
        private set

    fun setYearNumericFilter(yearFilterValue: Int?){
        savedStateHandle["yearNumericFilter"] = yearFilterValue
    }

    fun resetFilter(){
        setYearNumericFilter(null)
        setYearFilter("")
        setMonthFilter("")
        setTexFilter("")
        setPager(false)
    }

    fun setFilterTransaction(){
        try {
            setPager(true)
        }
        catch(e: Exception){
            savedStateHandle["filter"] = null
        }
    }

    fun setPager(withFilter: Boolean){

        transactionPager = if(withFilter){
            Pager(PagingConfig(pageSize = PAGE_SIZE)) {
                TransactionPagingHelper(TransactionListFilter(
                    textFilter.value,
                    if(yearNumericFilter.value != null)
                        yearNumericFilter.value.toString().padStart(2, '0')
                    else
                        null,
                    yearFilter.value
                ), dao)
            }.flow
        } else{
            Pager(PagingConfig(pageSize = PAGE_SIZE)) {
                TransactionPagingHelper(null, dao)
            }.flow
        }
    }
}
