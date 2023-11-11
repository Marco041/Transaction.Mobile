package com.example.transaction.viewModels

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.transaction.R
import com.example.transaction.dal.CategoryModel
import com.example.transaction.dal.TransactionDao
import com.example.transaction.helper.StringResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryInputViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val stringResourcesProvider: StringResourcesProvider,
    private val dao: TransactionDao
) : ViewModel() {


    val nameInitState = ""
    var name = savedStateHandle.getStateFlow("name", nameInitState)
        private set

    fun setName(name: String) {
        savedStateHandle["name"] = name
    }

    val subcategoryInitState = false
    var subcategory = savedStateHandle.getStateFlow("subcategory", subcategoryInitState)
        private set

    fun setSubcategory(subcategory: Boolean) {
        savedStateHandle["subcategory"] = subcategory
    }

    val insertMessageInitState = ""
    var insertMessage = savedStateHandle.getStateFlow("insertMessage", insertMessageInitState)
        private set

    fun setInsertMessage(msg: String) {
        savedStateHandle["insertMessage"] = msg
    }

    val showMessageInitState = false
    var showMessage = savedStateHandle.getStateFlow("showMessage", showMessageInitState)
        private set

    fun setShowtMessage(show: Boolean) {
        savedStateHandle["showMessage"] = show
    }

    fun clearState(){
        setName(nameInitState)
        setSubcategory(subcategoryInitState)
    }

    suspend fun insertCategory(): Boolean{
        if(name.value == ""){
            setShowtMessage(true)
            return false
        }

        val categoryDbItem = dao.getCategoryByName(name.value)

        if(categoryDbItem != null){
            return false
        }

        val categoryModel = CategoryModel(
            0,
            name.value,
            if(subcategory.value) 0 else 1)

        return try {
            val id = dao.insertCategory(categoryModel)
            setInsertMessage(stringResourcesProvider.getString(R.string.insert_success))
            setShowtMessage(true)
            clearState()
            true
        } catch (e: Exception) {
            setShowtMessage(true)
            stringResourcesProvider.getString(R.string.insert_fail)
            false
        }
    }
}
