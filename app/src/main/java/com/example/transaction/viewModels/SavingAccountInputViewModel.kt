package com.example.transaction.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.transaction.dal.SavingAccountDao
import com.example.transaction.dal.TransactionDao
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime


open class SavingAccountInputViewModel constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dao: SavingAccountDao
) : ViewModel() {


    var openSnackbar  = savedStateHandle.getStateFlow("openSnackbar", false)

    fun setOpenSnackbar(result: Boolean){
        savedStateHandle["openSnackbar"] = result
    }

    var snackBarMessage  = savedStateHandle.getStateFlow("snackBarMessage", "")

    fun setSnackBarMessaget(msg: String){
        savedStateHandle["snackBarMessage"] = msg
    }

    var saveResult  = savedStateHandle.getStateFlow("saveResult", false)

    fun setSaveResult(result: Boolean){
        savedStateHandle["saveResult"] = result
    }

    var id: StateFlow<Int> = savedStateHandle.getStateFlow("id", 0)
        private set

    fun setId(transactionId: Int){
        savedStateHandle["id"] = transactionId
    }

    val nameInitState = ""
    var name = savedStateHandle.getStateFlow("name", nameInitState)
        private set

    fun setName(accountName: String) {
        savedStateHandle["name"] = accountName
    }

    val amountInitState = 0.0
    var amount = savedStateHandle.getStateFlow("amount", amountInitState)
        private set

    fun setAmount(accountAmount: String) {
        savedStateHandle["amount"] = accountAmount.toDouble()
    }

    val yieldInitState = 0.0
    var yield = savedStateHandle.getStateFlow("yield", yieldInitState)
        private set

    fun setYield(accountYield: String) {
        savedStateHandle["yield"] = accountYield.toDouble()
    }

    val descInitState = ""
    var description = savedStateHandle.getStateFlow("description", descInitState)
        private set

    fun setDescription(description: String?) {
        savedStateHandle["description"] = description
    }

    fun clearState(){
        setId(0)
        setAmount(amountInitState.toString())
        setYield(yieldInitState.toString())
        setName(nameInitState)
        setDescription(descInitState)

    }
}
