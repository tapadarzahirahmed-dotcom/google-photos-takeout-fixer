package com.example.takeoutfixer.presentation.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _selectedFolderUri = MutableStateFlow<Uri?>(null)
    val selectedFolderUri = _selectedFolderUri.asStateFlow()

    private val _outputFolderUri = MutableStateFlow<Uri?>(null)
    val outputFolderUri = _outputFolderUri.asStateFlow()

    fun onFolderSelected(uri: Uri) {
        _selectedFolderUri.value = uri
    }

    fun onOutputFolderSelected(uri: Uri) {
        _outputFolderUri.value = uri
    }
}
