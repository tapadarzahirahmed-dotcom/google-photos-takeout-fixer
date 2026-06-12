package com.example.takeoutfixer.presentation.processing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takeoutfixer.domain.repository.FixOptions
import com.example.takeoutfixer.domain.repository.FixProgress
import com.example.takeoutfixer.domain.repository.TakeoutRepository
import com.example.takeoutfixer.domain.usecase.ApplyFixesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.takeoutfixer.data.notifications.FixNotificationManager

@HiltViewModel
class ProcessingViewModel @Inject constructor(
    private val applyFixesUseCase: ApplyFixesUseCase,
    private val repository: TakeoutRepository,
    private val notificationManager: FixNotificationManager
) : ViewModel() {
    private val _progress = MutableStateFlow<FixProgress?>(null)
    val progress = _progress.asStateFlow()

    fun startFixing(outputUri: android.net.Uri? = null) {
        viewModelScope.launch {
            val items = repository.getItems()
            applyFixesUseCase(items, FixOptions(outputFolderUri = outputUri)).collect {
                _progress.value = it
                if (it.isComplete) {
                    notificationManager.showComplete(it.totalItems)
                } else {
                    notificationManager.showProgress(it.processedItems, it.totalItems, it.currentFile)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        notificationManager.cancel()
    }
}
