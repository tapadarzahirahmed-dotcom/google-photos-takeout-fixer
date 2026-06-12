package com.example.takeoutfixer.presentation.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takeoutfixer.domain.repository.ScanProgress
import com.example.takeoutfixer.domain.usecase.ScanTakeoutUseCase
import com.example.takeoutfixer.presentation.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanUseCase: ScanTakeoutUseCase,
    // Note: In a real app, we'd share the URI via a SavedStateHandle or a SharedViewModel
) : ViewModel() {
    private val _progress = MutableStateFlow<ScanProgress?>(null)
    val progress = _progress.asStateFlow()

    fun startScan(uri: android.net.Uri) {
        android.util.Log.e("TakeoutScan", "ScanViewModel: Starting scan for $uri")
        viewModelScope.launch {
            scanUseCase(uri).collect {
                android.util.Log.e("TakeoutScan", "ScanViewModel: Progress update: $it")
                _progress.value = it
            }
        }
    }
}
