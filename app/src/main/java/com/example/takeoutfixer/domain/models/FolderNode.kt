package com.example.takeoutfixer.domain.models

data class FolderNode(
    val name: String,
    val isDirectory: Boolean,
    val children: List<FolderNode> = emptyList(),
    val type: ItemType = ItemType.UNKNOWN
)
