package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NaihatiDao {
    @Query("SELECT * FROM app_content WHERE id = 0")
    fun observeContent(): Flow<AppContentEntity?>

    @Query("SELECT * FROM app_content WHERE id = 0")
    suspend fun getContent(): AppContentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertContent(entity: AppContentEntity)

    @Query("DELETE FROM app_content")
    suspend fun clearContent()

    @Query("SELECT * FROM feedback_reports ORDER BY createdAt DESC")
    fun observeFeedback(): Flow<List<FeedbackEntity>>

    @Query("SELECT * FROM feedback_reports WHERE synced = 0 ORDER BY createdAt ASC")
    suspend fun getUnsyncedFeedback(): List<FeedbackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(entity: FeedbackEntity): Long

    @Query("UPDATE feedback_reports SET synced = 1 WHERE id = :id")
    suspend fun markFeedbackSynced(id: Long)
}
