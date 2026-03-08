package com.dreamrecall.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dreamrecall.data.BackupManager
import com.dreamrecall.data.Dream
import com.dreamrecall.data.DreamDatabase
import com.dreamrecall.data.DreamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DreamViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DreamRepository
    private val backupManager: BackupManager
    private val searchQuery = MutableStateFlow("")

    val dreams: StateFlow<List<Dream>>

    init {
        val dreamDao = DreamDatabase.getDatabase(application).dreamDao()
        repository = DreamRepository(dreamDao)
        backupManager = BackupManager(application)
        
        dreams = searchQuery.flatMapLatest { query ->
            if (query.isEmpty()) repository.allDreams else repository.search(query)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun addDream(title: String, body: String, tags: List<String>) {
        viewModelScope.launch {
            repository.insert(Dream(title = title, body = body, tags = tags))
        }
    }

    fun deleteDream(id: Int) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun exportDreams(uri: Uri) {
        viewModelScope.launch {
            backupManager.exportToUri(dreams.value, uri)
        }
    }

    fun importDreams(uri: Uri) {
        viewModelScope.launch {
            backupManager.importFromUri(uri)?.forEach { dream ->
                repository.insert(dream.copy(id = 0)) // New IDs locally
            }
        }
    }
}
