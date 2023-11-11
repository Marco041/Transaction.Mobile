package com.example.transaction.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.transaction.dal.SavingAccountDao
import com.example.transaction.dal.SavingAccountModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class SavingAccountListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dao: SavingAccountDao
) : ViewModel() {

    var accountList = savedStateHandle.getStateFlow("accountList", listOf<SavingAccountModel>())
        private set

    var total: StateFlow<Double?> = savedStateHandle.getStateFlow("total", null)
        private set

    var totalYieldPercentage: StateFlow<Double?> = savedStateHandle.getStateFlow("totalYieldPercentage", null)
        private set

    var totalYieldAmount: StateFlow<Double?> = savedStateHandle.getStateFlow("totalYieldAmount", null)
        private set

    suspend fun loadData(){
        val list = dao.getList()
        savedStateHandle["accountList"] = list

        var totalAmount: Double = 0.0
        var yieldPercentage: Double = 0.0
        var yieldAmount: Double = 0.0
        list.forEach {
            totalAmount += it.amount
            yieldAmount += it.amount * it.yield / 100
        }

        savedStateHandle["total"] = totalAmount
        savedStateHandle["totalYieldAmount"] = yieldAmount
        savedStateHandle["totalYieldPercentage"] = yieldAmount / totalAmount * 100
    }
}
