package com.example.takeoutfixer.domain.usecase

import com.example.takeoutfixer.domain.models.TakeoutItem
import com.example.takeoutfixer.domain.repository.FixOptions
import com.example.takeoutfixer.domain.repository.TakeoutRepository
import javax.inject.Inject

class ApplyFixesUseCase @Inject constructor(
    private val repository: TakeoutRepository
) {
    suspend operator fun invoke(items: List<TakeoutItem>, options: FixOptions) =
        repository.applyFixes(items, options)
}
