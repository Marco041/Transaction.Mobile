package com.example.transaction.init

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.transaction.dal.DbContext
import com.example.transaction.dal.SavingAccountDao
import com.example.transaction.dal.TransactionDao
import com.example.transaction.helper.SettingsStore
import com.example.transaction.services.DatabaseFileService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    companion object{
        fun  getDatabaseName(): String{
            return "TransactionDatabase_v1.db"
        }
    }

    @Provides
    @Singleton
    fun provideDataStore(): SettingsStore {
        return SettingsStore()
    }

    @Provides
    fun provideBackupService(appDatabase: DbContext, store: SettingsStore
    ): DatabaseFileService {
        return DatabaseFileService(appDatabase, store)
    }

    @Provides
    fun provideTransactionDao(appDatabase: DbContext): TransactionDao {
        return appDatabase.transactionDao()
    }

    @Provides
    fun provideAccountDao(appDatabase: DbContext): SavingAccountDao {
        return appDatabase.accountDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): DbContext {
        val db = Room.databaseBuilder(appContext, DbContext::class.java, getDatabaseName())
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)

        db.setQueryCallback(LoggerCallback(), Executors.newSingleThreadExecutor())
        return db.build()
    }
}

class LoggerCallback: RoomDatabase.QueryCallback{
    override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
        println("___QUERY___ callback -> $sqlQuery SQL Args: $bindArgs");
    }
}