package com.example.transaction.dal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface SavingAccountDao {

    @Query("SELECT * FROM saving_accounts ORDER BY Id DESC")
    suspend fun getList() : List<SavingAccountModel>

    @Query("SELECT * FROM saving_accounts WHERE Id=:id")
    suspend fun get(id: Int) : SavingAccountModel

    @Update
    suspend fun updateSavingAccount(model: SavingAccountModel): Int

    @Insert
    suspend fun insertSavingAccount(model: SavingAccountModel): Long

    @Delete
    suspend fun deleteSavingAccount(toDelete: SavingAccountModel)
}