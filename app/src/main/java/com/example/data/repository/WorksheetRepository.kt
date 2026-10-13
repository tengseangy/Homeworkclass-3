package com.example.data.repository

import com.example.data.local.SavedWorksheetEntity
import com.example.data.local.WorksheetDao
import com.example.data.local.WorksheetJsonAdapter
import com.example.model.Worksheet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorksheetRepository(private val worksheetDao: WorksheetDao) {

    val savedWorksheetsFlow: Flow<List<Worksheet>> = worksheetDao.getAllSavedWorksheets().map { list ->
        list.mapNotNull { entity ->
            WorksheetJsonAdapter.fromJson(entity.problemsJson)
        }
    }

    suspend fun saveWorksheet(worksheet: Worksheet) {
        val json = WorksheetJsonAdapter.toJson(worksheet)
        val entity = SavedWorksheetEntity(
            id = worksheet.id,
            titleKhmer = worksheet.titleKhmer,
            categoryString = worksheet.category.name,
            gradeLevelKhmer = worksheet.gradeLevelKhmer,
            subjectKhmer = worksheet.subjectKhmer,
            chapterNameKhmer = worksheet.chapterNameKhmer,
            lessonNameKhmer = worksheet.lessonNameKhmer,
            schoolNameKhmer = worksheet.schoolNameKhmer,
            teacherNameKhmer = worksheet.teacherNameKhmer,
            durationMinutes = worksheet.durationMinutes,
            totalScore = worksheet.totalScore,
            instructionsKhmer = worksheet.instructionsKhmer,
            problemsJson = json,
            createdAt = worksheet.createdAt
        )
        worksheetDao.insertWorksheet(entity)
    }

    suspend fun deleteWorksheet(id: String) {
        worksheetDao.deleteWorksheetById(id)
    }

    suspend fun getWorksheetById(id: String): Worksheet? {
        val entity = worksheetDao.getWorksheetById(id) ?: return null
        return WorksheetJsonAdapter.fromJson(entity.problemsJson)
    }
}
