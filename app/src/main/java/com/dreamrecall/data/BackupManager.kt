package com.dreamrecall.data

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter

class BackupManager(private val context: Context) {
    private val gson = Gson()

    suspend fun exportToUri(dreams: List<Dream>, uri: Uri) = withContext(Dispatchers.IO) {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                gson.toJson(dreams, writer)
            }
        }
    }

    suspend fun importFromUri(uri: Uri): List<Dream>? = withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val json = inputStream.bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Dream>>() {}.type
            gson.fromJson(json, type)
        }
    }
}
