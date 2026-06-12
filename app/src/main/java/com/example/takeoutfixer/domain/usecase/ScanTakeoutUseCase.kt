package com.example.takeoutfixer.domain.usecase

import android.net.Uri
import com.example.takeoutfixer.domain.repository.TakeoutRepository
import javax.inject.Inject

class ScanTakeoutUseCase @Inject constructor(
    private val repository: TakeoutRepository
) {
    operator fun invoke(rootUri: Uri) = repository.scanFolder(rootUri)
}
