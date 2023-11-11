package com.example.transaction.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.transaction.dal.CategoryModel
import com.example.transaction.dal.TransactionCategoryModel
import com.example.transaction.dal.TransactionDao
import com.example.transaction.dal.TransactionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
open class TransactionInsertViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dao: TransactionDao
) : TransactionInputViewModel(savedStateHandle, dao) {

    suspend fun insertTransaction(): Boolean{
        if(date.value == "" || category.value == ""){
            return false
        }

        val categoryDbItem: CategoryModel? = dao.getCategoryByName(category.value)

        val categoryId =
            categoryDbItem?.id
            ?: dao.insertCategory(CategoryModel(0, category.value, 1)).toInt()


        val transactionModel = TransactionModel(
            0,
            date.value,
            category.value,
            income.value,
            outcome.value,
            description.value)

        try {
            val id = dao.insertTransaction(transactionModel)

            dao.insertCategoriesTransaction(TransactionCategoryModel(categoryId, id.toInt()))

            for (item in subcategory.value){
                if(item != category.value) {
                    var subcategoryDbItem = dao.getCategoryByName(item)

                    val subcategoryId =
                        subcategoryDbItem?.id
                            ?: dao.insertCategory(CategoryModel(0, item, 0)).toInt()

                    dao.insertCategoriesTransaction(
                        TransactionCategoryModel(
                            subcategoryId,
                            id.toInt()
                        )
                    )
                }
            }

            return true
        } catch (e: Exception) {
            return false
        }
    }
}
