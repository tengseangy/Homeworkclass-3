package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.repository.WorksheetRepository
import com.example.generator.CurriculumData
import com.example.generator.ExamGenerator
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class MainNavigationTab {
    HOME,
    GENERATE_HOMEWORK,
    GENERATE_EXAM,
    TEACHING_GUIDE,
    SAVED_WORKSHEETS
}

enum class WorksheetDetailTab {
    STUDENT_SHEET,
    TEACHER_KEY,
    INTERACTIVE_PRACTICE
}

data class AppUiState(
    val currentTab: MainNavigationTab = MainNavigationTab.HOME,
    val activeWorksheet: Worksheet? = null,
    val detailTab: WorksheetDetailTab = WorksheetDetailTab.STUDENT_SHEET,
    val schoolName: String = "សាលាបឋមសិក្សា...",
    val teacherName: String = "លោកគ្រូ / អ្នកគ្រូ...",
    val useKhmerNumerals: Boolean = false,
    val selectedLessonId: String = "L1_1",
    val selectedChapterId: Int = 1,
    val difficulty: String = "មធ្យម",
    val questionCount: Int = 6,
    val savedWorksheets: List<Worksheet> = emptyList(),
    val isWorksheetSaved: Boolean = false,
    val snackbarMessage: String? = null,

    // Interactive Practice state
    val studentAnswers: Map<String, String> = emptyMap(),
    val studentSelectedOptions: Map<String, String> = emptyMap(),
    val practiceResult: StudentPracticeResult? = null
)

class MathGrade3ViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WorksheetRepository

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = WorksheetRepository(db.worksheetDao())

        // Initial default worksheet
        generateHomework()

        // Collect saved worksheets from Room DB
        viewModelScope.launch {
            repository.savedWorksheetsFlow.collect { savedList ->
                _uiState.update { state ->
                    val isCurrentSaved = state.activeWorksheet?.let { cur ->
                        savedList.any { it.id == cur.id }
                    } ?: false
                    state.copy(savedWorksheets = savedList, isWorksheetSaved = isCurrentSaved)
                }
            }
        }
    }

    fun navigateToTab(tab: MainNavigationTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setDetailTab(detailTab: WorksheetDetailTab) {
        _uiState.update { it.copy(detailTab = detailTab) }
    }

    fun updateSchoolName(name: String) {
        _uiState.update { it.copy(schoolName = name) }
    }

    fun updateTeacherName(name: String) {
        _uiState.update { it.copy(teacherName = name) }
    }

    fun toggleKhmerNumerals(enabled: Boolean) {
        _uiState.update { it.copy(useKhmerNumerals = enabled) }
    }

    fun selectLesson(lessonId: String) {
        _uiState.update { it.copy(selectedLessonId = lessonId) }
    }

    fun selectChapter(chapterId: Int) {
        val chapter = CurriculumData.chapters.find { it.id == chapterId }
        val firstLesson = chapter?.lessons?.firstOrNull()?.id ?: "L1_1"
        _uiState.update { it.copy(selectedChapterId = chapterId, selectedLessonId = firstLesson) }
    }

    fun updateDifficulty(difficulty: String) {
        _uiState.update { it.copy(difficulty = difficulty) }
    }

    fun updateQuestionCount(count: Int) {
        _uiState.update { it.copy(questionCount = count) }
    }

    fun generateHomework() {
        val state = _uiState.value
        val worksheet = ExamGenerator.generateWorksheet(
            category = WorksheetCategory.HOMEWORK,
            lessonId = state.selectedLessonId,
            schoolName = state.schoolName,
            teacherName = state.teacherName,
            difficulty = state.difficulty,
            questionCount = state.questionCount
        )
        setActiveWorksheet(worksheet)
    }

    fun generateExam(category: WorksheetCategory, chapterId: Int? = null) {
        val state = _uiState.value
        val worksheet = ExamGenerator.generateWorksheet(
            category = category,
            chapterId = chapterId,
            schoolName = state.schoolName,
            teacherName = state.teacherName,
            difficulty = state.difficulty,
            questionCount = state.questionCount
        )
        setActiveWorksheet(worksheet)
    }

    fun setActiveWorksheet(worksheet: Worksheet) {
        val isSaved = _uiState.value.savedWorksheets.any { it.id == worksheet.id }
        _uiState.update {
            it.copy(
                activeWorksheet = worksheet,
                isWorksheetSaved = isSaved,
                detailTab = WorksheetDetailTab.STUDENT_SHEET,
                studentAnswers = emptyMap(),
                studentSelectedOptions = emptyMap(),
                practiceResult = null
            )
        }
    }

    fun saveCurrentWorksheet() {
        val current = _uiState.value.activeWorksheet ?: return
        viewModelScope.launch {
            repository.saveWorksheet(current)
            _uiState.update {
                it.copy(
                    isWorksheetSaved = true,
                    snackbarMessage = "បានរក្សាទុកសន្លឹកកិច្ចការដោយជោគជ័យ!"
                )
            }
        }
    }

    fun deleteSavedWorksheet(id: String) {
        viewModelScope.launch {
            repository.deleteWorksheet(id)
            _uiState.update { it.copy(snackbarMessage = "បានលុបសន្លឹកកិច្ចការរួចរាល់") }
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    // --- Student Practice Mode & Auto Grading ---
    fun onStudentAnswerInput(problemId: String, text: String) {
        _uiState.update {
            it.copy(studentAnswers = it.studentAnswers + (problemId to text))
        }
    }

    fun onStudentOptionSelected(problemId: String, optionKey: String) {
        _uiState.update {
            it.copy(studentSelectedOptions = it.studentSelectedOptions + (problemId to optionKey))
        }
    }

    fun submitStudentPractice() {
        val current = _uiState.value.activeWorksheet ?: return
        var correctCount = 0
        var totalScore = 0

        for (problem in current.problems) {
            when (problem.problemType) {
                ProblemType.MULTIPLE_CHOICE -> {
                    val selected = _uiState.value.studentSelectedOptions[problem.id]
                    val correct = problem.options.firstOrNull { it.isCorrect }?.key
                    if (selected != null && selected == correct) {
                        correctCount++
                        totalScore += problem.points
                    }
                }
                else -> {
                    val input = _uiState.value.studentAnswers[problem.id]?.trim() ?: ""
                    val targetNum = problem.primaryNumber.toString()
                    val teacherAnswer = problem.teacherAnswerKhmer
                    // Check if input matches numeric or teacher answer
                    if (input.isNotBlank() && (input.contains(targetNum) || teacherAnswer.contains(input))) {
                        correctCount++
                        totalScore += problem.points
                    }
                }
            }
        }

        val maxScore = current.totalScore
        val percentage = if (maxScore > 0) (totalScore.toFloat() / maxScore.toFloat()) * 100f else 0f
        val passed = percentage >= 50f
        val gradeKhmer = when {
            percentage >= 85f -> "ល្អណាស់ (Very Good)"
            percentage >= 70f -> "ល្អ (Good)"
            percentage >= 50f -> "មធ្យម (Fair)"
            else -> "ត្រូវខិតខំបន្ថែម (Needs Improvement)"
        }

        val result = StudentPracticeResult(
            totalQuestions = current.problems.size,
            correctCount = correctCount,
            totalScore = totalScore,
            maxScore = maxScore,
            percentage = percentage,
            passed = passed,
            gradeLetterKhmer = gradeKhmer
        )

        _uiState.update { it.copy(practiceResult = result) }
    }

    fun resetStudentPractice() {
        _uiState.update {
            it.copy(
                studentAnswers = emptyMap(),
                studentSelectedOptions = emptyMap(),
                practiceResult = null
            )
        }
    }
}
