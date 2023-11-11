package com.example.transaction.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.transaction.dal.CategoryModel
import com.example.transaction.dal.TransactionDao
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime


open class TransactionInputViewModel constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dao: TransactionDao
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

    val dataInitState = LocalDateTime.now().toLocalDate().toString()
    var date = savedStateHandle.getStateFlow("date", dataInitState)
        private set

    fun setData(date: String) {
        savedStateHandle["date"] = date
    }

    val categoryInitState = ""
    var category = savedStateHandle.getStateFlow("category", categoryInitState)
        private set

    fun setCategory(category: String) {
        savedStateHandle["category"] = category
    }

    val subcategoryInitState = listOf<String>()
    var subcategory = savedStateHandle.getStateFlow("subcategory", subcategoryInitState)
        private set

    fun addSubCategory(toAdd: String) {
        val newList = subcategory.value + toAdd
        savedStateHandle["subcategory"] = newList
    }

    fun addNewSubCategory(toAdd: String) {
        if(toAdd.isNullOrEmpty()){
            savedStateHandle["subcategory"] = listOf<String>()
        }
        else {
            val trimmedList = toAdd.split(",").map { it.trim() }
            savedStateHandle["subcategory"] = trimmedList
        }
    }

    fun removeSubCategory(toRemove: String) {
        val newList = subcategory.value.filter { it != toRemove }
        savedStateHandle["subcategory"] = newList
    }

    fun removeAllCategories() {
        savedStateHandle["subcategory"] = listOf<String>()
    }

    val incomeInitState = 0.0
    var income = savedStateHandle.getStateFlow("income", incomeInitState)
        private set

    fun setIncome(income: String) {
        savedStateHandle["income"] = income.toDouble()
    }

    val outcomeInitState = 0.0
    var outcome = savedStateHandle.getStateFlow("outcome", outcomeInitState)
        private set

    fun setOutcome(outcome: String) {
        savedStateHandle["outcome"] = outcome.toDouble()
    }

    val descInitState = ""
    var description = savedStateHandle.getStateFlow("description", descInitState)
        private set

    fun setDescription(description: String?) {
        savedStateHandle["description"] = description
    }

    var allCategories: List<CategoryModel> = listOf()
    var allSubcategories: List<CategoryModel> = listOf()

    suspend fun initAllCategory() {
        allCategories = dao.getAllCategories()
        allSubcategories = dao.getCategories(0)
    }

    var categories = savedStateHandle.getStateFlow("categories", allCategories)
        private set

    var categoriesFilter = savedStateHandle.getStateFlow("categoriesFilter", "")
        private set

    fun filterCategories(search: String){
        savedStateHandle["categoriesFilter"] = search
        if(search == ""){
            savedStateHandle["categories"] = allCategories
        }
        else {
            savedStateHandle["categories"] = allCategories.filter {
                it.name.lowercase().contains(search.lowercase())
            }
        }
    }

    var subcategories = savedStateHandle.getStateFlow("subcategories", allSubcategories)
        private set

    suspend fun clearState(){
        setId(0)
        setData(dataInitState)
        setCategory(categoryInitState)
        removeAllCategories()
        setDescription(descInitState)
        setIncome(incomeInitState.toString())
        setOutcome((outcomeInitState.toString()))
        savedStateHandle["categories"] = dao.getCategories()
        savedStateHandle["subcategories"] = dao.getCategories(0)

    }
}
