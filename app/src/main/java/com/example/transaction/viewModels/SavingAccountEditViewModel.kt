package com.example.transaction.viewModels

import androidx.lifecycle.SavedStateHandle
import com.example.transaction.dal.SavingAccountDao
import com.example.transaction.dal.SavingAccountModel
import com.example.transaction.dal.TransactionCategoryModel
import com.example.transaction.dal.TransactionDao
import com.example.transaction.dal.TransactionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavingAccountEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dao: SavingAccountDao
) : SavingAccountInputViewModel(savedStateHandle, dao) {


    suspend fun loadSavingAccount(id: Int){
        clearState()
        if(id > 0) {
            val account = dao.get(id)

            setId(account.id)
            setName(account.name)
            setYield(account.yield.toString())
            setAmount(account.amount.toString())
            setDescription(account.description)
        }
    }

    suspend fun editSavingAccount(): Boolean{
        if(name.value == ""){
            return false
        }

        val accountDbItem = dao.get(id.value)

        val model = SavingAccountModel(
            id.value,
            name.value,
            amount.value,
            description.value,
            yield.value)

        return try {
            dao.updateSavingAccount(model)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteSavingAccount(): Boolean{
        if(id.value == 0){
            return false
        }

        return try {
            val accountDbItem = dao.get(id.value)
            dao.deleteSavingAccount(accountDbItem)
            true
        } catch (e: Exception) {
            false
        }
    }
}
