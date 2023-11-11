package com.example.transaction.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.transaction.dal.CategoryModel
import com.example.transaction.dal.TransactionDao
import com.example.transaction.dal.QueryBuilder
import com.example.transaction.dal.ReportModel
import com.example.transaction.dto.ReportEnum
import com.example.transaction.dto.ReportFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dao: TransactionDao
) : ViewModel() {

    suspend fun initAllCategory() {
        allCategories = dao.getAllCategories()
    }
    
    var showReportOption = savedStateHandle.getStateFlow("showReportOption", false)
        private set

    fun setShowReportOption(show: Boolean){
        savedStateHandle["showReportOption"] = show
    }

    var selectedReport: StateFlow<ReportEnum> = savedStateHandle.getStateFlow("selectedReport", ReportEnum.MonthlyNet)
        private set

    fun setReport(report: ReportEnum){
        savedStateHandle["selectedReport"] = report

        if(report == ReportEnum.ByCategory){
            setShowReportOption(true)
        }
        else{
            setShowReportOption(false)
        }
    }

    var groupByYear = savedStateHandle.getStateFlow("groupByYear", true)
        private set

    fun setGroupByYear(year: Boolean){
        savedStateHandle["groupByYear"] = year
    }

    var groupBySubcategory = savedStateHandle.getStateFlow("groupBySubcategory", false)
        private set

    fun setGroupBySubcategory(subcategory: Boolean){
        savedStateHandle["groupBySubcategory"] = subcategory
    }

    var categoryFilter = savedStateHandle.getStateFlow("categoryFilter", "")
        private set

    fun setCategoryFilter(filter: String){
        savedStateHandle["categoryFilter"] = filter
    }

    var allCategories: List<CategoryModel> = listOf()

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

    var totalByCategory = savedStateHandle.getStateFlow("totalByCategory", listOf<ReportModel>())
        private set

    suspend fun loadCategoryReport(){
        val filter = ReportFilter(groupByYear.value, groupBySubcategory.value, categoryFilter.value)
        val query = QueryBuilder.categoryReport(filter)
        savedStateHandle["totalByCategory"] = dao.reportTotalByCategory(query)
    }

    var monthlyNet = savedStateHandle.getStateFlow("monthlyNet", listOf<ReportModel>())
        private set

    suspend fun loadMonthlyNetReport(){
        savedStateHandle["monthlyNet"] =  dao.reportMonthlyNet()
    }

    var netByYear = savedStateHandle.getStateFlow("netByYear", listOf<ReportModel>())
        private set

    suspend fun loadNetByYearReport(){
        savedStateHandle["netByYear"] =  dao.reportNetByYear()
    }

    var avgMonthlyIncomeyByYear = savedStateHandle.getStateFlow("avgMonthlyIncomeyByYear", listOf<ReportModel>())
        private set

    suspend fun loadAvgMonthlyIncomeyByYearReport(){
        savedStateHandle["avgMonthlyIncomeyByYear"] =  dao.getAvgMonthIncomeByYear()
    }
}
