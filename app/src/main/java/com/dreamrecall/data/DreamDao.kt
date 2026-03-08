package com.dreamrecall.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DreamDao {
    @Query("SELECT * FROM dreams ORDER BY timestamp DESC")
    fun getAllDreams(): Flow<List<Dream>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDream(dream: Dream)

    @Query("DELETE FROM dreams WHERE id = :id")
    suspend fun deleteDream(id: Int)

    @Query("SELECT * FROM dreams WHERE title LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%'")
    fun searchDreams(query: String): Flow<List<Dream>>

    @Query("SELECT * FROM dreams ORDER BY lastShownMillis ASC LIMIT 1")
    suspend fun getLeastRecentlyShown(): Dream?

    @Query("UPDATE dreams SET lastShownMillis = :timestamp WHERE id = :id")
    suspend fun updateLastShown(id: Int, timestamp: Long)
}
