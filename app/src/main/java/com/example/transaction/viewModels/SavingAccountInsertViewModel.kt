package com.example.transaction.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.transaction.dal.SavingAccountDao
import com.example.transaction.dal.SavingAccountModel
import com.example.transaction.dal.TransactionCategoryModel
import com.example.transaction.dal.TransactionDao
import com.example.transaction.dal.TransactionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
open class SavingAccountInsertViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dao: SavingAccountDao
) : SavingAccountInputViewModel(savedStateHandle, dao) {

    suspend fun insertSavingAccount(): Boolean{
        if(name.value == ""){
            return false
        }

        return try {
            val model = SavingAccountModel(
                0,
                name.value,
                amount.value,
                description.value,
                yield.value)

            val id = dao.insertSavingAccount(model)
            true
        } catch (e: Exception) {
            false
        }
    }
}
