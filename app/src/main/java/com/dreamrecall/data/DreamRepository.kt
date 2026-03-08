package com.dreamrecall.data

import com.dreamrecall.data.Dream
import com.dreamrecall.data.DreamDao
import kotlinx.coroutines.flow.Flow

class DreamRepository(private val dreamDao: DreamDao) {
    val allDreams: Flow<List<Dream>> = dreamDao.getAllDreams()

    suspend fun insert(dream: Dream) {
        dreamDao.insertDream(dream)
    }

    suspend fun delete(id: Int) {
        dreamDao.deleteDream(id)
    }

    fun search(query: String): Flow<List<Dream>> {
        return dreamDao.searchDreams(query)
    }

    suspend fun getLeastRecentlyShownDream(): Dream? {
        return dreamDao.getLeastRecentlyShown()
    }

    suspend fun markDreamAsShown(id: Int) {
        dreamDao.updateLastShown(id, System.currentTimeMillis())
    }
}
