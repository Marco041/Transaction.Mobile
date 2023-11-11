package com.example.transaction.dto

data class ReportFilter (
    val splitByYear: Boolean,
    val splitBySubcategory: Boolean,
    val categoryFilter: String)