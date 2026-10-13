package com.example.model

enum class WorksheetCategory {
    HOMEWORK,       // សន្លឹកកិច្ចការផ្ទះ
    REINFORCEMENT,  // សន្លឹកលំហាត់ពង្រឹងសមត្ថភាព
    MONTHLY_TEST,   // វិញ្ញាសាប្រឡងប្រចាំខែ
    SEMESTER_EXAM_1,// វិញ្ញាសាប្រឡងឆមាសទី១
    SEMESTER_EXAM_2,// វិញ្ញាសាប្រឡងឆមាសទី២ / ចុងឆ្នាំ
    CHAPTER_TEST    // វិញ្ញាសាតាមជំពូក
}

data class Worksheet(
    val id: String,
    val titleKhmer: String,
    val category: WorksheetCategory,
    val gradeLevelKhmer: String = "ថ្នាក់ទី ៣",
    val subjectKhmer: String = "គណិតវិទ្យា (ផ្នែកចំនួន)",
    val chapterNameKhmer: String = "",
    val lessonNameKhmer: String = "",
    val schoolNameKhmer: String = "សាលាបឋមសិក្សា...",
    val teacherNameKhmer: String = "លោកគ្រូ / អ្នកគ្រូ...",
    val durationMinutes: Int = 45,
    val totalScore: Int = 100,
    val instructionsKhmer: String = "ចូរសិស្សអានប្រធានលំហាត់នីមួយៗឱ្យបានម៉ត់ចត់ រួចគណនា និងសរសេរចម្លើយឱ្យបានត្រឹមត្រូវ។",
    val problems: List<MathProblem> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

data class StudentAnswerState(
    val problemId: String,
    val studentInput: String = "",
    val selectedOptionKey: String? = null,
    val isAnswered: Boolean = false,
    val isCorrect: Boolean? = null,
    val scoreAwarded: Int = 0
)

data class StudentPracticeResult(
    val totalQuestions: Int,
    val correctCount: Int,
    val totalScore: Int,
    val maxScore: Int,
    val percentage: Float,
    val passed: Boolean,
    val gradeLetterKhmer: String // ល្អណាស់, ល្អ, មធ្យម, ត្រូវខិតខំបន្ថែម
)
