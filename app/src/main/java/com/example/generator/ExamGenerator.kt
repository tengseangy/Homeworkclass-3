package com.example.generator

import com.example.model.*
import java.util.UUID
import kotlin.random.Random

object ExamGenerator {

    fun generateWorksheet(
        category: WorksheetCategory,
        lessonId: String? = null,
        chapterId: Int? = null,
        schoolName: String = "សាលាបឋមសិក្សា...",
        teacherName: String = "លោកគ្រូ / អ្នកគ្រូ...",
        difficulty: String = "មធ្យម",
        questionCount: Int = 8
    ): Worksheet {
        val id = UUID.randomUUID().toString()
        val random = Random(System.currentTimeMillis())

        return when (category) {
            WorksheetCategory.HOMEWORK -> {
                val lesson = lessonId?.let { CurriculumData.getLessonById(it) } ?: CurriculumData.chapters[1].lessons[0]
                val problems = MathExerciseGenerator.generateExercisesForLesson(lesson.id, questionCount, difficulty)
                Worksheet(
                    id = id,
                    titleKhmer = "សន្លឹកកិច្ចការផ្ទះ៖ ${lesson.titleKhmer}",
                    category = WorksheetCategory.HOMEWORK,
                    gradeLevelKhmer = "ថ្នាក់ទី ៣",
                    subjectKhmer = "គណិតវិទ្យា (ផ្នែកចំនួន)",
                    chapterNameKhmer = CurriculumData.getChapterByLessonId(lesson.id)?.titleKhmer ?: "",
                    lessonNameKhmer = lesson.titleKhmer,
                    schoolNameKhmer = schoolName,
                    teacherNameKhmer = teacherName,
                    durationMinutes = 30,
                    totalScore = problems.sumOf { it.points },
                    instructionsKhmer = "ចូរសិស្សអនុវត្តលំហាត់នីមួយៗឱ្យបានត្រឹមត្រូវ រួចបង្ហាញការគណនាក្នុងប្រអប់ដែលបានកំណត់។",
                    problems = problems
                )
            }
            WorksheetCategory.REINFORCEMENT -> {
                val lesson = lessonId?.let { CurriculumData.getLessonById(it) } ?: CurriculumData.chapters[0].lessons[1]
                val problems = MathExerciseGenerator.generateExercisesForLesson(lesson.id, questionCount, difficulty)
                Worksheet(
                    id = id,
                    titleKhmer = "សន្លឹកកិច្ចការពង្រឹងសមត្ថភាព៖ ${lesson.titleKhmer}",
                    category = WorksheetCategory.REINFORCEMENT,
                    gradeLevelKhmer = "ថ្នាក់ទី ៣",
                    subjectKhmer = "គណិតវិទ្យា (ផ្នែកចំនួន)",
                    chapterNameKhmer = CurriculumData.getChapterByLessonId(lesson.id)?.titleKhmer ?: "",
                    lessonNameKhmer = lesson.titleKhmer,
                    schoolNameKhmer = schoolName,
                    teacherNameKhmer = teacherName,
                    durationMinutes = 40,
                    totalScore = problems.sumOf { it.points },
                    instructionsKhmer = "សន្លឹកកិច្ចការនេះសម្រាប់ពង្រឹងការចងចាំ និងបំណិនគណិតវិទ្យាដើម្បីត្រៀមប្រឡង។",
                    problems = problems
                )
            }
            WorksheetCategory.MONTHLY_TEST -> {
                buildStandardExam(
                    id = id,
                    title = "វិញ្ញាសាប្រឡងគណិតវិទ្យាប្រចាំខែ",
                    category = WorksheetCategory.MONTHLY_TEST,
                    schoolName = schoolName,
                    teacherName = teacherName,
                    lessons = listOf("L1_1", "L1_2", "L1_3", "L2_1", "L2_2"),
                    random = random
                )
            }
            WorksheetCategory.SEMESTER_EXAM_1 -> {
                buildStandardExam(
                    id = id,
                    title = "វិញ្ញាសាប្រឡងឆមាសទី១ គណិតវិទ្យាថ្នាក់ទី៣",
                    category = WorksheetCategory.SEMESTER_EXAM_1,
                    schoolName = schoolName,
                    teacherName = teacherName,
                    lessons = listOf("L1_1", "L1_2", "L1_3", "L2_1", "L2_2"),
                    random = random
                )
            }
            WorksheetCategory.SEMESTER_EXAM_2 -> {
                buildStandardExam(
                    id = id,
                    title = "វិញ្ញាសាប្រឡងឆមាសទី២ / ចុងឆ្នាំ គណិតវិទ្យាថ្នាក់ទី៣",
                    category = WorksheetCategory.SEMESTER_EXAM_2,
                    schoolName = schoolName,
                    teacherName = teacherName,
                    lessons = listOf("L1_2", "L1_3", "L2_1", "L2_2", "L3_1", "L3_2", "L3_3", "L3_4"),
                    random = random
                )
            }
            WorksheetCategory.CHAPTER_TEST -> {
                val chapter = chapterId?.let { cid -> CurriculumData.chapters.find { it.id == cid } } ?: CurriculumData.chapters[0]
                val lessonIds = chapter.lessons.map { it.id }
                buildStandardExam(
                    id = id,
                    title = "វិញ្ញាសាប្រឡងបញ្ចប់៖ ${chapter.titleKhmer}",
                    category = WorksheetCategory.CHAPTER_TEST,
                    schoolName = schoolName,
                    teacherName = teacherName,
                    lessons = lessonIds,
                    random = random
                )
            }
        }
    }

    private fun buildStandardExam(
        id: String,
        title: String,
        category: WorksheetCategory,
        schoolName: String,
        teacherName: String,
        lessons: List<String>,
        random: Random
    ): Worksheet {
        val problems = mutableListOf<MathProblem>()

        // Section I: Multiple Choice Questions (2 questions, 10 pts each = 20 pts)
        problems.add(
            createMCQProblem(
                id = "EXAM_MCQ_1",
                lessonId = lessons.first(),
                random = random
            )
        )
        problems.add(
            createMCQProblem(
                id = "EXAM_MCQ_2",
                lessonId = if (lessons.size > 1) lessons[1] else lessons[0],
                random = random
            )
        )

        // Section II: Place Value & Expanded Form / Comparison (2 questions, 10 pts each = 20 pts)
        problems.add(
            MathExerciseGenerator.generateExercisesForLesson("L1_2", 1, "មធ្យម").first().copy(
                id = "EXAM_SEC2_1",
                points = 10,
                instructionKhmer = "ផ្នែកទី២ (ខ)៖ សរសេរចំនួនខាងក្រោមជាទម្រង់ពង្រាយ"
            )
        )
        problems.add(
            MathExerciseGenerator.generateExercisesForLesson("L1_3", 1, "មធ្យម").first().copy(
                id = "EXAM_SEC2_2",
                points = 10,
                instructionKhmer = "ផ្នែកទី២ (គ)៖ ប្រៀបធៀប និងបំពេញសញ្ញា < , > ឬ ="
            )
        )

        // Section III: Calculation operations (3 questions, 10 pts each = 30 pts)
        val calcLessons = lessons.filter { it.startsWith("L2_") || it.startsWith("L3_") }.ifEmpty { listOf("L2_1", "L2_2") }
        for (i in 1..3) {
            val targetLesson = calcLessons[(i - 1) % calcLessons.size]
            val prob = MathExerciseGenerator.generateExercisesForLesson(targetLesson, 1, "មធ្យម").first().copy(
                id = "EXAM_SEC3_$i",
                points = 10,
                instructionKhmer = "ផ្នែកទី៣ (លំហាត់គណនាទី $i)៖ គណនាប្រមាណវិធីខាងក្រោម"
            )
            problems.add(prob)
        }

        // Section IV: Real-world Word Problem (1 question = 30 pts with detailed grading rubric)
        val wpProblem = createComprehensiveWordProblem(random, lessons)
        problems.add(wpProblem)

        return Worksheet(
            id = id,
            titleKhmer = title,
            category = category,
            gradeLevelKhmer = "ថ្នាក់ទី ៣",
            subjectKhmer = "គណិតវិទ្យា (ផ្នែកចំនួន)",
            chapterNameKhmer = "វិញ្ញាសាសរុបតាមកម្មវិធីសិក្សាជាតិ",
            lessonNameKhmer = "មេរៀនរួម",
            schoolNameKhmer = schoolName,
            teacherNameKhmer = teacherName,
            durationMinutes = 45,
            totalScore = problems.sumOf { it.points },
            instructionsKhmer = "វិញ្ញាសាមាន ៤ផ្នែក។ សិស្សត្រូវអាន និងគិតឱ្យច្បាស់លាស់ មុននឹងសរសេរចម្លើយ។ ហាមប្រើម៉ាស៊ីនគិតលេខពេលប្រឡង!",
            problems = problems
        )
    }

    private fun createMCQProblem(id: String, lessonId: String, random: Random): MathProblem {
        val n = random.nextInt(1200, 9500)
        val rounded = Math.round(n / 100.0).toInt() * 100
        val wrong1 = rounded + 100
        val wrong2 = if (rounded > 100) rounded - 100 else rounded + 200
        val wrong3 = rounded + 10

        val options = listOf(
            ProblemOption("ក", "$rounded", true),
            ProblemOption("ខ", "$wrong1", false),
            ProblemOption("គ", "$wrong2", false),
            ProblemOption("ឃ", "$wrong3", false)
        ).shuffled(random).mapIndexed { idx, opt ->
            val key = when (idx) {
                0 -> "ក"
                1 -> "ខ"
                2 -> "គ"
                else -> "ឃ"
            }
            opt.copy(key = key)
        }

        val correct = options.first { it.isCorrect }

        return MathProblem(
            id = id,
            lessonId = lessonId,
            titleKhmer = "ផ្នែកទី១៖ ជ្រើសរើសចម្លើយត្រឹមត្រូវ (ពហុជ្រើសរើស)",
            instructionKhmer = "ចូរគូសរង្វង់ (○) ជុំវិញអក្សរនៃចម្លើយដែលត្រឹមត្រូវបំផុត៖",
            questionKhmer = "តើចំនួន $n បង្គត់ត្រឹមខ្ទង់រយស្មើនឹងប៉ុន្មាន?",
            problemType = ProblemType.MULTIPLE_CHOICE,
            points = 10,
            options = options,
            studentAnswerPlaceholderKhmer = "ចម្លើយត្រឹមត្រូវគឺ៖ [   ]",
            teacherAnswerKhmer = "${correct.key}. ${correct.textKhmer}",
            teacherExplanationKhmer = "ចំនួន $n មានខ្ទង់ដប់គឺ ${(n / 10) % 10} ដូច្នេះតាមវិធានបង្គត់ត្រឹមខ្ទង់រយ គឺស្មើ $rounded (ចម្លើយ ${correct.key})។"
        )
    }

    private fun createComprehensiveWordProblem(random: Random, lessons: List<String>): MathProblem {
        val hasMultiplication = lessons.contains("L3_1")
        if (hasMultiplication) {
            val bags = random.nextInt(4, 8)
            val applesPerBag = random.nextInt(15, 25)
            val extraApples = random.nextInt(10, 30)
            val subTotal = bags * applesPerBag
            val total = subTotal + extraApples

            return MathProblem(
                id = "EXAM_SEC4_WP",
                lessonId = "L3_1",
                titleKhmer = "ផ្នែកទី៤៖ ដោះស្រាយចំណោទជាក់ស្តែង (៣០ ពិន្ទុ)",
                instructionKhmer = "ចូរអានប្រធានចំណោទខាងក្រោម គូសដ្យាក្រាម រួចដោះស្រាយតាម ៤ជំហាន Polya៖",
                questionKhmer = "ពូមានបានទិញផ្លែប៉ោមចំនួន $bags ថង់ ដោយក្នុងមួយថង់ៗមានផ្លែប៉ោម $applesPerBag ផ្លែ។ បន្ទាប់មក មីងទូចបានថែមផ្លែប៉ោមឱ្យគាត់ចំនួន $extraApples ផ្លែបន្ថែមទៀត។ \nក. តើផ្លែប៉ោមដែលពូមានទិញក្នុង $bags ថង់មានសរុបប៉ុន្មានផ្លែ? (១៥ ពិន្ទុ) \nខ. តើពូមានមានផ្លែប៉ោមសរុបទាំងអស់ប៉ុន្មានផ្លែ? (១៥ ពិន្ទុ)",
                problemType = ProblemType.WORD_PROBLEM,
                points = 30,
                studentAnswerPlaceholderKhmer = "របៀបឆ្លើយ៖\nក. រកចំនួនផ្លែប៉ោមក្នុង $bags ថង់៖ ...........................\nប្រមាណវិធី៖ ................................................\nខ. រកចំនួនផ្លែប៉ោមសរុបទាំងអស់៖ ...........................\nប្រមាណវិធី៖ ................................................\nឆ្លើយថា៖ ................................................",
                teacherAnswerKhmer = "ក. $subTotal ផ្លែ | ខ. $total ផ្លែ",
                teacherExplanationKhmer = "បំណែងចែកពិន្ទុតាមដំណាក់កាល (Marking Rubric)៖\n• ក. រកចំនួនផ្លែប៉ោមក្នុង $bags ថង់៖ $applesPerBag × $bags = $subTotal (ផ្លែ) -> ១៥ ពិន្ទុ\n• ខ. រកចំនួនផ្លែប៉ោមសរុបទាំងអស់៖ $subTotal + $extraApples = $total (ផ្លែ) -> ១៥ ពិន្ទុ\n• ផ្ទៀងផ្ទាត់៖ $total - $extraApples = $subTotal ផ្លែ (ត្រឹមត្រូវ)។",
                wordProblem = WordProblemDetail(
                    storyKhmer = "ពូមានបានទិញផ្លែប៉ោមចំនួន $bags ថង់ ដោយក្នុងមួយថង់ៗមានផ្លែប៉ោម $applesPerBag ផ្លែ។ បន្ទាប់មក មីងទូចបានថែមផ្លែប៉ោមឱ្យគាត់ចំនួន $extraApples ផ្លែបន្ថែមទៀត។",
                    questionsKhmer = listOf(
                        "ក. តើផ្លែប៉ោមដែលពូមានទិញក្នុង $bags ថង់មានសរុបប៉ុន្មានផ្លែ?",
                        "ខ. តើពូមានមានផ្លែប៉ោមសរុបទាំងអស់ប៉ុន្មានផ្លែ?"
                    ),
                    answerStatementsKhmer = listOf(
                        "ក. ចំនួនផ្លែប៉ោមក្នុង $bags ថង់គឺ៖",
                        "ខ. ចំនួនផ្លែប៉ោមសរុបទាំងអស់គឺ៖"
                    ),
                    calculationFormulaKhmer = "ក. $applesPerBag × $bags = $subTotal (ផ្លែ)\nខ. $subTotal + $extraApples = $total (ផ្លែ)",
                    verificationKhmer = "$total - $extraApples = $subTotal ផ្លែ (ត្រឹមត្រូវ)",
                    finalAnswerKhmer = "ឆ្លើយថា៖ គាត់មានផ្លែប៉ោមសរុប $total ផ្លែ"
                )
            )
        } else {
            val chickens = random.nextInt(2350, 4200)
            val ducks = random.nextInt(1200, 2800)
            val sold = random.nextInt(850, 1600)
            val totalBefore = chickens + ducks
            val remain = totalBefore - sold

            return MathProblem(
                id = "EXAM_SEC4_WP",
                lessonId = "L2_2",
                titleKhmer = "ផ្នែកទី៤៖ ដោះស្រាយចំណោទជាក់ស្តែង (៣០ ពិន្ទុ)",
                instructionKhmer = "ចូរអានប្រធានចំណោទខាងក្រោម គូសដ្យាក្រាម រួចដោះស្រាយតាម ៤ជំហាន Polya៖",
                questionKhmer = "កសិដ្ឋានចិញ្ចឹមសត្វមួយមានមាន់ចំនួន $chickens ក្បាល និងទាចំនួន $ducks ក្បាល។ ម្ចាស់កសិដ្ឋានបានលក់សត្វចេញអស់ចំនួន $sold ក្បាល។ \nក. តើកសិដ្ឋាននោះមានសត្វ (មាន់ និងទា) សរុបទាំងអស់ប៉ុន្មានក្បាល? (១៥ ពិន្ទុ) \nខ. តើកសិដ្ឋាននោះនៅសល់សត្វប៉ុន្មានក្បាល? (១៥ ពិន្ទុ)",
                problemType = ProblemType.WORD_PROBLEM,
                points = 30,
                studentAnswerPlaceholderKhmer = "របៀបឆ្លើយ៖\nក. រកចំនួនសត្វសរុបទាំងអស់៖ ................................\nប្រមាណវិធី៖ ................................................\nខ. រកចំនួនសត្វដែលនៅសល់៖ .................................\nប្រមាណវិធី៖ ................................................\nឆ្លើយថា៖ ................................................",
                teacherAnswerKhmer = "ក. $totalBefore ក្បាល | ខ. $remain ក្បាល",
                teacherExplanationKhmer = "បំណែងចែកពិន្ទុតាមដំណាក់កាល (Marking Rubric)៖\n• ក. រកចំនួនសត្វសរុបទាំងអស់៖ $chickens + $ducks = $totalBefore (ក្បាល) -> ១៥ ពិន្ទុ\n• ខ. រកចំនួនសត្វដែលនៅសល់៖ $totalBefore - $sold = $remain (ក្បាល) -> ១៥ ពិន្ទុ\n• ផ្ទៀងផ្ទាត់៖ $remain + $sold = $totalBefore ក្បាល (ត្រឹមត្រូវ)។",
                wordProblem = WordProblemDetail(
                    storyKhmer = "កសិដ្ឋានចិញ្ចឹមសត្វមួយមានមាន់ចំនួន $chickens ក្បាល និងទាចំនួន $ducks ក្បាល។ ម្ចាស់កសិដ្ឋានបានលក់សត្វចេញអស់ចំនួន $sold ក្បាល។",
                    questionsKhmer = listOf(
                        "ក. តើកសិដ្ឋាននោះមានសត្វសរុបទាំងអស់ប៉ុន្មានក្បាល?",
                        "ខ. តើកសិដ្ឋាននោះនៅសល់សត្វប៉ុន្មានក្បាល?"
                    ),
                    answerStatementsKhmer = listOf(
                        "ក. ចំនួនសត្វ (មាន់ និងទា) សរុបទាំងអស់គឺ៖",
                        "ខ. ចំនួនសត្វដែលនៅសល់គឺ៖"
                    ),
                    calculationFormulaKhmer = "ក. $chickens + $ducks = $totalBefore (ក្បាល)\nខ. $totalBefore - $sold = $remain (ក្បាល)",
                    verificationKhmer = "$remain + $sold = $totalBefore ក្បាល (ត្រឹមត្រូវ)",
                    finalAnswerKhmer = "ឆ្លើយថា៖ នៅសល់សត្វសរុប $remain ក្បាល"
                )
            )
        }
    }
}
