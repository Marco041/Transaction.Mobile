package com.example.transaction.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.transaction.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ApplicationBarViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    var title = savedStateHandle.getStateFlow("title", R.string.app_name.toString())
        private set

    fun setTitle(title: String){
        savedStateHandle["title"] = title
    }

}