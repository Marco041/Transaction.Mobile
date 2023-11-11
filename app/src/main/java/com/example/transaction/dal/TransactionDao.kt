package com.example.transaction.dal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery


@Dao
interface TransactionDao {
    @RawQuery
    suspend fun checkpoint(supportSQLiteQuery: SupportSQLiteQuery): Int

    @Insert
    suspend fun insertCategoriesTransaction(input: TransactionCategoryModel): Long

    @Insert
    suspend fun insertTransaction(transactionModel: TransactionModel): Long

    @Update
    suspend fun updateTransaction(transactionModel: TransactionModel): Int

    @Insert
    suspend fun insertCategory(categoryModel: CategoryModel): Long

    @Delete
    suspend fun deleteCategoriesTransaction(toDelete: TransactionCategoryModel)

    @Delete
    suspend fun deleteTransaction(transactionModel: TransactionModel)

    @RawQuery
    suspend fun getAll(query: SupportSQLiteQuery): List<TransactionWithCategory>

    @Query("SELECT * FROM Transazioni WHERE Id<:lastId ORDER BY Id DESC LIMIT :take")
    suspend fun getList(take: Int, lastId: Int) : List<TransactionWithCategory>

    @Query("SELECT * FROM Transazioni WHERE Id=:id")
    suspend fun get(id: Int) : TransactionWithCategory

    @Query("SELECT MAX(Id) FROM Transazioni")
    suspend fun getLastTransactionId() : Int

    @Query("SELECT COUNT(*) FROM Transazioni")
    suspend fun count() : Int

    @Query("SELECT * FROM Categorie WHERE Primaria=:primary ORDER BY Nome")
    suspend fun getCategories(primary: Int = 1) : List<CategoryModel>

    @Query("SELECT * FROM Categorie ORDER BY Nome")
    suspend fun getAllCategories() : List<CategoryModel>

    @Query("SELECT * FROM Categorie WHERE Nome=:name ORDER BY Nome")
    suspend fun getCategoryByName(name: String) : CategoryModel?

    @RawQuery
    suspend fun reportTotalByCategory(query: SupportSQLiteQuery) : List<ReportModel>

    @Query("""
        SELECT 
            strftime('%Y', Data) AS year,
            strftime('%m', Data) as month, 
            SUM(Entrata) as income, 
            SUM(Uscita) as outflow, 
            SUM(Entrata)-SUM(Uscita) as net
        FROM Transazioni 
        GROUP BY strftime('%Y', Data),strftime('%m', Data)
        ORDER BY Id DESC
    """)
    suspend fun reportMonthlyNet() : List<ReportModel>

    @Query("""
        SELECT 
            SUM(Entrata) AS income,
            SUM(Uscita) AS outflow,
            SUM(Entrata)-sum(Uscita) AS net,
            strftime('%Y', Data) AS year
        FROM transazioni 
        GROUP BY strftime('%Y', Data)
        ORDER BY Year DESC
    """)
    suspend fun reportNetByYear() : List<ReportModel>

    @Query("""
        SELECT 
            Year AS year, 
            AVG(A.Net) AS net
        FROM (
            SELECT strftime('%Y', Data) AS Year,strftime('%m', Data) ,SUM(Entrata)-SUM(Uscita) AS Net
            FROM Transazioni 
            GROUP BY strftime('%Y', Data),strftime('%m', Data)
        ) A
        GROUP BY Year
        ORDER BY Year DESC
    """)
    suspend fun getAvgMonthIncomeByYear(): List<ReportModel>
}