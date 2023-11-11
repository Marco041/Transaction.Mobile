package com.example.transaction.viewModels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.transaction.dal.TransactionDao
import com.example.transaction.dto.TransactionSetting
import com.example.transaction.helper.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class BackupOptionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val store: SettingsStore
) : ViewModel() {

    var zipPassword: StateFlow<String> = savedStateHandle.getStateFlow("zipPassword", "")
    var zipBackup: StateFlow<String> = savedStateHandle.getStateFlow("zipBackup", "true")

    fun setZipPassword(state: String){
        savedStateHandle["zipPassword"] = state
    }

    fun setZipBackup(state: String){
        savedStateHandle["zipBackup"] = state
    }

    suspend fun initState(context: Context){
        val state = store.getPreferenceValue(context)
        setZipPassword(state.zipPassword)
        setZipBackup( state.zipBackup)
    }

    suspend fun storeSettings(context: Context, setting: TransactionSetting){
        store.savePreferenceValue(context, setting)
    }

}
