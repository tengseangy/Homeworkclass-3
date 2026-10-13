package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorksheetDao {
    @Query("SELECT * FROM saved_worksheets ORDER BY createdAt DESC")
    fun getAllSavedWorksheets(): Flow<List<SavedWorksheetEntity>>

    @Query("SELECT * FROM saved_worksheets WHERE id = :id LIMIT 1")
    suspend fun getWorksheetById(id: String): SavedWorksheetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorksheet(entity: SavedWorksheetEntity)

    @Query("DELETE FROM saved_worksheets WHERE id = :id")
    suspend fun deleteWorksheetById(id: String)

    @Query("DELETE FROM saved_worksheets")
    suspend fun clearAll()
}
