package com.example.transaction.viewModels

import androidx.lifecycle.SavedStateHandle
import com.example.transaction.dal.CategoryModel
import com.example.transaction.dal.TransactionCategoryModel
import com.example.transaction.dal.TransactionDao
import com.example.transaction.dal.TransactionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dao: TransactionDao
) : TransactionInputViewModel(savedStateHandle, dao) {


    suspend fun loadTransaction(id: Int){
        clearState()
        if(id > 0) {
            val transaction = dao.get(id)

            setId(transaction.transaction.id)
            setData(transaction.transaction.data)
            setCategory(transaction.transaction.category)
            setDescription(transaction.transaction.description)
            setIncome(transaction.transaction.income.toString())
            setOutcome(transaction.transaction.outcome.toString())

            for (item in transaction.categoryList)
                if(item.name != transaction.transaction.category) {
                    addSubCategory(item.name)
                }
        }
    }

    suspend fun editTransaction(): Boolean{
        if(date.value == "" || category.value == "" || id.value == 0){
            return false
        }

        val transactionDbItem = dao.get(id.value)

        val transactionModel = TransactionModel(
            id.value,
            date.value,
            category.value,
            income.value,
            outcome.value,
            description.value)

        try {
            dao.updateTransaction(transactionModel)
            for (item in transactionDbItem.categoryList) {
                dao.deleteCategoriesTransaction(
                    TransactionCategoryModel(
                        item.id,
                        id.value
                    )
                )
            }

            val categoryDbItem = dao.getCategoryByName(category.value)

            val categoryId =
                categoryDbItem?.id
                    ?: dao.insertCategory(CategoryModel(0, category.value, 1)).toInt()

            dao.insertCategoriesTransaction(TransactionCategoryModel(categoryId, id.value))

            for (item in subcategory.value){
                if(item != category.value) {
                    val subcategoryDbItem = dao.getCategoryByName(item)

                    val subcategoryId =
                        subcategoryDbItem?.id
                            ?: dao.insertCategory(CategoryModel(0, item, 0)).toInt()

                    dao.insertCategoriesTransaction(
                        TransactionCategoryModel(
                            subcategoryId,
                            id.value
                        )
                    )
                }
            }

            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun deleteTransaction(): Boolean{
        if(id.value == 0){
            return false
        }

        val categoryDbItem = dao.getCategoryByName(category.value)
        val transactionDbItem = dao.get(id.value)

        try {
            for (item in transactionDbItem.categoryList) {
                dao.deleteCategoriesTransaction(
                    TransactionCategoryModel(
                        item.id,
                        id.value
                    )
                )
            }
            dao.deleteTransaction(transactionDbItem.transaction)

            return true
        } catch (e: Exception) {
            return false
        }
    }
}
