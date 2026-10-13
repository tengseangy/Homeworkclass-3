package com.example.model

enum class ProblemType {
    FILL_BLANK,           // បំពេញចន្លោះ
    MULTIPLE_CHOICE,      // ជ្រើសរើសចម្លើយត្រឹមត្រូវ (ក, ខ, គ, ឃ)
    CALCULATION_VERTICAL, // ប្រមាណវិធីតាមជួរឈរ (បូក ដក គុណ ចែក)
    CALCULATION_HORIZONTAL, // ប្រមាណវិធីតាមជួរដេក
    PLACE_VALUE_EXPANDED, // សរសេរជាទម្រង់ពង្រាយ
    PLACE_VALUE_TABLE,    // បំពេញក្នុងតារាងតម្លៃលេខតាមខ្ទង់
    COMPARE_SIGNS,        // បំពេញសញ្ញា < , > , =
    ORDER_NUMBERS,        // រៀបលំដាប់កើន ឬលំដាប់ចុះ
    NUMBER_PATTERN,       // បំពេញលំនាំគំរូនីមួយៗ
    ROUNDING_ESTIMATION,  // ការបង្គត់ និងប៉ាន់ស្មាន
    FRACTION_IDENTIFY,    // កំណត់ភាគយក ភាគបែង និងប្រភាគ
    FRACTION_CALCULATION, // បូក ដក ប្រភាគភាគបែងដូចគ្នា
    WORD_PROBLEM          // ចំណោទជាក់ស្តែងតាមជីវភាព
}

data class ProblemOption(
    val key: String, // ក, ខ, គ, ឃ
    val textKhmer: String,
    val isCorrect: Boolean
)

data class SolutionStep(
    val stepTitleKhmer: String,
    val explanationKhmer: String,
    val mathEquationKhmer: String? = null
)

data class WordProblemDetail(
    val storyKhmer: String,
    val questionsKhmer: List<String>,
    val answerStatementsKhmer: List<String>,
    val calculationFormulaKhmer: String,
    val verificationKhmer: String,
    val finalAnswerKhmer: String
)

data class MathProblem(
    val id: String,
    val lessonId: String,
    val titleKhmer: String,
    val instructionKhmer: String,
    val questionKhmer: String,
    val problemType: ProblemType,
    val points: Int = 10,
    
    // Multiple Choice
    val options: List<ProblemOption> = emptyList(),
    
    // For Place Value / Numbers
    val primaryNumber: Int = 0,
    val secondaryNumber: Int = 0,
    val operandA: String = "",
    val operandB: String = "",
    val operationSymbol: String = "", // +, -, ×, ÷
    
    // Standard answers
    val studentAnswerPlaceholderKhmer: String = "ចម្លើយ៖ ............................",
    val teacherAnswerKhmer: String,
    val teacherExplanationKhmer: String,
    val steps: List<SolutionStep> = emptyList(),
    
    // For Word problems
    val wordProblem: WordProblemDetail? = null,
    
    // Visual aid tag (e.g., "bar_model", "place_value_chart", "fraction_circle", "pattern_blocks")
    val visualAidType: String? = null,
    val visualAidData: String? = null
)
