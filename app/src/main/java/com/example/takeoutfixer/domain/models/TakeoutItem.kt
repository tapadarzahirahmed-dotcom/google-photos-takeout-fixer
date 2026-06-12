package com.example.takeoutfixer.domain.models

import android.net.Uri

data class TakeoutItem(
    val id: String,
    val name: String,
    val fileUri: Uri,
    val jsonUri: Uri? = null,
    val type: ItemType,
    val size: Long = 0,
    val jsonSize: Long = 0,
    val originalTimestamp: Long? = null,
    val metadataTimestamp: Long? = null,
    val albumName: String? = null,
    val relativePath: List<String> = emptyList(),
    val isEdited: Boolean = false,
    val status: FixStatus = FixStatus.PENDING
)

enum class ItemType {
    IMAGE, VIDEO, UNKNOWN
}

enum class FixStatus {
    PENDING, MATCHED, UNMATCHED, FIXED, FAILED, SKIPPED
}
