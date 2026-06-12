package com.example.takeoutfixer.presentation.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takeoutfixer.domain.models.TakeoutItem
import com.example.takeoutfixer.domain.repository.TakeoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.takeoutfixer.domain.models.FolderNode

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val repository: TakeoutRepository
) : ViewModel() {
    private val _items = MutableStateFlow<List<TakeoutItem>>(emptyList())
    val items = _items.asStateFlow()

    private val _folderStructure = MutableStateFlow<FolderNode?>(null)
    val folderStructure = _folderStructure.asStateFlow()

    init {
        viewModelScope.launch {
            _items.value = repository.getItems()
            _folderStructure.value = repository.getFolderStructure()
        }
    }
}
