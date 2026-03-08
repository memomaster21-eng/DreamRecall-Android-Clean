package com.dreamrecall.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dreams")
data class Dream(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val body: String,
    val timestamp: Long = System.currentTimeMillis(),
    val tags: List<String> = emptyList(),
    val lastShownMillis: Long = 0 // Track when it was last shown in a popup
)
