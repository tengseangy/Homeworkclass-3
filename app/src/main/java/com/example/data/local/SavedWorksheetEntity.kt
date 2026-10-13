package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_worksheets")
data class SavedWorksheetEntity(
    @PrimaryKey
    val id: String,
    val titleKhmer: String,
    val categoryString: String,
    val gradeLevelKhmer: String,
    val subjectKhmer: String,
    val chapterNameKhmer: String,
    val lessonNameKhmer: String,
    val schoolNameKhmer: String,
    val teacherNameKhmer: String,
    val durationMinutes: Int,
    val totalScore: Int,
    val instructionsKhmer: String,
    val problemsJson: String,
    val createdAt: Long = System.currentTimeMillis()
)
