package com.example.generator

import com.example.model.*
import kotlin.random.Random

object MathExerciseGenerator {

    /**
     * Generate exercises for a specific lesson
     */
    fun generateExercisesForLesson(
        lessonId: String,
        count: Int = 5,
        difficulty: String = "មធ្យម" // ងាយស្រួល, មធ្យម, កម្រិតខ្ពស់
    ): List<MathProblem> {
        val problems = mutableListOf<MathProblem>()
        var seed = System.currentTimeMillis()

        for (i in 1..count) {
            val random = Random(seed + i * 37)
            val problem = when (lessonId) {
                "L1_1" -> generateNumbersUpTo10000(i, random, difficulty)
                "L1_2" -> generatePlaceValueProblem(i, random, difficulty)
                "L1_3" -> generateCompareOrderPattern(i, random, difficulty)
                "L2_1" -> generateAdditionProblem(i, random, difficulty)
                "L2_2" -> generateSubtractionProblem(i, random, difficulty)
                "L3_1" -> generateMultiplicationProblem(i, random, difficulty)
                "L3_2" -> generateDivision2DigitProblem(i, random, difficulty)
                "L3_3" -> generateDivision3DigitProblem(i, random, difficulty)
                "L3_4" -> generateFractionProblem(i, random, difficulty)
                else -> generateAdditionProblem(i, random, difficulty)
            }
            problems.add(problem)
        }
        return problems
    }

    // --- LESSON 1.1: NUMBERS UP TO 10 000 ---
    private fun generateNumbersUpTo10000(index: Int, random: Random, difficulty: String): MathProblem {
        val subType = (index % 4)
        return when (subType) {
            0 -> {
                // Number to Khmer Words
                val num = (random.nextInt(10, 99) * 100) + (random.nextInt(1, 9) * 10) + random.nextInt(1, 9)
                val khmerWords = KhmerNumberUtil.toKhmerWords(num)
                val khmerNum = KhmerNumberUtil.toKhmer(num)
                MathProblem(
                    id = "L1_1_$index",
                    lessonId = "L1_1",
                    titleKhmer = "ការអាន និងសរសេរចំនួនជាពាក្យ",
                    instructionKhmer = "ចូរសរសេរចំនួនខាងក្រោមជាអក្សរ (ពាក្យ) ឱ្យបានត្រឹមត្រូវ៖",
                    questionKhmer = "សរសេរចំនួន $num ($khmerNum) ជាពាក្យ៖",
                    problemType = ProblemType.FILL_BLANK,
                    points = 10,
                    primaryNumber = num,
                    studentAnswerPlaceholderKhmer = "អានថា៖ .....................................................",
                    teacherAnswerKhmer = khmerWords,
                    teacherExplanationKhmer = "ចំនួន $num ($khmerNum) អានតាមខ្ទង់៖ $khmerWords។"
                )
            }
            1 -> {
                // Words to Digits
                val thousands = random.nextInt(1, 9)
                val hundreds = random.nextInt(0, 9)
                val tens = random.nextInt(1, 9)
                val ones = random.nextInt(1, 9)
                val num = thousands * 1000 + hundreds * 100 + tens * 10 + ones
                val words = KhmerNumberUtil.toKhmerWords(num)
                MathProblem(
                    id = "L1_1_$index",
                    lessonId = "L1_1",
                    titleKhmer = "ការសរសេរចំនួនជាលេខពីពាក្យ",
                    instructionKhmer = "ចូរសរសេរចំនួនជាលេខ ទៅតាមពាក្យដែលបានកំណត់ខាងក្រោម៖",
                    questionKhmer = "« $words »",
                    problemType = ProblemType.FILL_BLANK,
                    points = 10,
                    primaryNumber = num,
                    studentAnswerPlaceholderKhmer = "ជាលេខ៖ ............................",
                    teacherAnswerKhmer = "$num (ឬ ${KhmerNumberUtil.toKhmer(num)})",
                    teacherExplanationKhmer = "អក្សរ «$words» សរសេរជាលេខគឺ $num ឬជាលេខខ្មែរ ${KhmerNumberUtil.toKhmer(num)}។"
                )
            }
            2 -> {
                // Step counting by 100 or 1000
                val isBy1000 = random.nextBoolean()
                val step = if (isBy1000) 1000 else 100
                val start = random.nextInt(1, 5) * step
                val missingIndex = 2
                val seq = (0..4).map { start + it * step }
                val questionDisplay = seq.mapIndexed { idx, v -> if (idx == missingIndex) "___" else v.toString() }.joinToString(", ")
                val answerVal = seq[missingIndex]

                MathProblem(
                    id = "L1_1_$index",
                    lessonId = "L1_1",
                    titleKhmer = "ការរាប់បន្ថែមម្តង ${if (isBy1000) "១០០០" else "១០០"}",
                    instructionKhmer = "ចូរបំពេញចំនួនដែលបាត់ក្នុងចន្លោះនៃលំដាប់រាប់បន្ថែមខាងក្រោម៖",
                    questionKhmer = "$questionDisplay",
                    problemType = ProblemType.NUMBER_PATTERN,
                    points = 10,
                    primaryNumber = answerVal,
                    studentAnswerPlaceholderKhmer = "ចំនួនក្នុងចន្លោះគឺ៖ ............................",
                    teacherAnswerKhmer = "$answerVal",
                    teacherExplanationKhmer = "លំដាប់នេះគឺរាប់បន្ថែមម្តង $step ដូច្នេះចំនួនបន្តគឺ ${seq[missingIndex - 1]} + $step = $answerVal។"
                )
            }
            else -> {
                // Real-life numbers (phone, plate, year, house)
                val houseNum = random.nextInt(12, 98)
                val streetNum = random.nextInt(100, 399)
                MathProblem(
                    id = "L1_1_$index",
                    lessonId = "L1_1",
                    titleKhmer = "ការអានលេខក្នុងជីវភាពរស់នៅប្រចាំថ្ងៃ",
                    instructionKhmer = "ចូរបកស្រាយការអានលេខផ្ទះ និងលេខផ្លូវឱ្យបានត្រឹមត្រូវ៖",
                    questionKhmer = "ផ្ទះលេខ $houseNum ផ្លូវលេខ $streetNum",
                    problemType = ProblemType.FILL_BLANK,
                    points = 10,
                    studentAnswerPlaceholderKhmer = "អានថា៖ ផ្ទះលេខ ................ ផ្លូវលេខ ................",
                    teacherAnswerKhmer = "ផ្ទះលេខ ${KhmerNumberUtil.toKhmerWords(houseNum)} ផ្លូវលេខ ${KhmerNumberUtil.toKhmerWords(streetNum)}",
                    teacherExplanationKhmer = "តាមមេរៀនវិធីសាស្ត្របង្រៀនគណិតវិទ្យាថ្នាក់ទី៣៖ លេខផ្ទះ និងលេខផ្លូវអាចអានតាមខ្ទង់ ឬអានម្តងមួយតួ៖ ផ្ទះលេខ ${KhmerNumberUtil.toKhmerWords(houseNum)} ផ្លូវលេខ ${KhmerNumberUtil.toKhmerWords(streetNum)}។"
                )
            }
        }
    }

    // --- LESSON 1.2: PLACE VALUE & EXPANDED FORM ---
    private fun generatePlaceValueProblem(index: Int, random: Random, difficulty: String): MathProblem {
        val sub = index % 3
        val thousands = random.nextInt(2, 9)
        val hundreds = random.nextInt(1, 9)
        val tens = random.nextInt(1, 9)
        val ones = random.nextInt(1, 9)
        val num = thousands * 1000 + hundreds * 100 + tens * 10 + ones

        return when (sub) {
            0 -> {
                // Expanded form (ទម្រង់ពង្រាយ)
                val expandedStr = "${thousands * 1000} + ${hundreds * 100} + ${tens * 10} + $ones"
                MathProblem(
                    id = "L1_2_$index",
                    lessonId = "L1_2",
                    titleKhmer = "ការសរសេរចំនួនជាទម្រង់ពង្រាយ",
                    instructionKhmer = "ចូរសរសេរចំនួនខាងក្រោមជាទម្រង់ពង្រាយតាមតម្លៃលេខនៃខ្ទង់នីមួយៗ៖",
                    questionKhmer = "$num = .................................................................",
                    problemType = ProblemType.PLACE_VALUE_EXPANDED,
                    points = 10,
                    primaryNumber = num,
                    studentAnswerPlaceholderKhmer = "$num = ...... + ...... + ...... + ......",
                    teacherAnswerKhmer = "$num = $expandedStr",
                    teacherExplanationKhmer = "ចំនួន $num មាន $thousands ពាន់ (${thousands * 1000}) បូក $hundreds រយ (${hundreds * 100}) បូក $tens ដប់ (${tens * 10}) បូក $ones រាយ ($ones) ស្មើ $expandedStr។"
                )
            }
            1 -> {
                // Place value identification
                val targets = listOf("ពាន់" to (thousands * 1000), "រយ" to (hundreds * 100), "ដប់" to (tens * 10), "រាយ" to ones)
                val target = targets[random.nextInt(targets.size)]
                val digit = when (target.first) {
                    "ពាន់" -> thousands
                    "រយ" -> hundreds
                    "ដប់" -> tens
                    else -> ones
                }
                MathProblem(
                    id = "L1_2_$index",
                    lessonId = "L1_2",
                    titleKhmer = "ការកំណត់តម្លៃលេខតាមខ្ទង់",
                    instructionKhmer = "ចូរឆ្លើយសំណួរអំពីតម្លៃលេខតាមខ្ទង់ខាងក្រោម៖",
                    questionKhmer = "ក្នុងចំនួន $num តើលេខ $digit ស្ថិតនៅខ្ទង់ណា និងមានតម្លៃលេខប៉ុន្មាន?",
                    problemType = ProblemType.PLACE_VALUE_TABLE,
                    points = 10,
                    studentAnswerPlaceholderKhmer = "ខ្ទង់៖ ................... តម្លៃ៖ ...................",
                    teacherAnswerKhmer = "ស្ថិតនៅខ្ទង់${target.first} មានតម្លៃ ${target.second}",
                    teacherExplanationKhmer = "លេខ $digit ស្ថិតនៅខ្ទង់${target.first} ដូច្នេះវាមានតម្លៃស្មើនឹង ${target.second}។"
                )
            }
            else -> {
                // Rounding (ការបង្គត់)
                val roundMode = random.nextInt(3) // 0: ខ្ទង់ដប់, 1: ខ្ទង់រយ, 2: ខ្ទង់ពាន់
                val roundPlace = when (roundMode) {
                    0 -> "ខ្ទង់ដប់"
                    1 -> "ខ្ទង់រយ"
                    else -> "ខ្ទង់ពាន់"
                }
                val roundedVal = when (roundMode) {
                    0 -> Math.round(num / 10.0).toInt() * 10
                    1 -> Math.round(num / 100.0).toInt() * 100
                    else -> Math.round(num / 1000.0).toInt() * 1000
                }
                MathProblem(
                    id = "L1_2_$index",
                    lessonId = "L1_2",
                    titleKhmer = "ការបង្គត់ចំនួនគត់",
                    instructionKhmer = "ចូរបង្គត់ចំនួនខាងក្រោមទៅតាមខ្ទង់ដែលបានកំណត់៖",
                    questionKhmer = "បង្គត់ចំនួន $num ត្រឹម$roundPlace ៖",
                    problemType = ProblemType.ROUNDING_ESTIMATION,
                    points = 10,
                    primaryNumber = num,
                    studentAnswerPlaceholderKhmer = "$num ≈ ............................",
                    teacherAnswerKhmer = "$num ≈ $roundedVal",
                    teacherExplanationKhmer = "វិធានបង្គត់៖ ប្រសិនបើតួលេខនៅខ្ទង់បន្ទាប់តូចជាង 5 (0,1,2,3,4) ត្រូវបង្គត់ចុះ។ ប្រសិនបើធំជាង ឬស្មើ 5 (5,6,7,8,9) ត្រូវបង្គត់ឡើង។ ដូចនេះ $num បង្គត់ត្រឹម$roundPlace បាន $roundedVal។"
                )
            }
        }
    }

    // --- LESSON 1.3: COMPARING, ORDERING & ODD/EVEN ---
    private fun generateCompareOrderPattern(index: Int, random: Random, difficulty: String): MathProblem {
        val sub = index % 3
        return when (sub) {
            0 -> {
                // Comparison <, >, =
                val n1 = random.nextInt(1000, 9999)
                val diff = if (random.nextBoolean()) random.nextInt(1, 50) else 0
                val n2 = if (random.nextBoolean()) n1 + diff else n1 - diff
                val sign = when {
                    n1 < n2 -> "<"
                    n1 > n2 -> ">"
                    else -> "="
                }
                MathProblem(
                    id = "L1_3_$index",
                    lessonId = "L1_3",
                    titleKhmer = "ការប្រៀបធៀបចំនួនគត់",
                    instructionKhmer = "ចូរបំពេញសញ្ញា < , > ឬ = ក្នុងប្រអប់ [  ] ឱ្យបានត្រឹមត្រូវ៖",
                    questionKhmer = "$n1   [   ]   $n2",
                    problemType = ProblemType.COMPARE_SIGNS,
                    points = 10,
                    operandA = n1.toString(),
                    operandB = n2.toString(),
                    studentAnswerPlaceholderKhmer = "សញ្ញា៖ [   ]",
                    teacherAnswerKhmer = "$n1 $sign $n2",
                    teacherExplanationKhmer = "ប្រៀបធៀបខ្ទង់ពីឆ្វេងទៅស្តាំ៖ ខ្ទង់ពាន់ ខ្ទង់រយ ខ្ទង់ដប់ និងខ្ទង់រាយ។ ដូចនេះ $n1 $sign $n2។"
                )
            }
            1 -> {
                // Ordering (លំដាប់កើន ឬ លំដាប់ចុះ)
                val isAscending = random.nextBoolean()
                val list = (1..4).map { random.nextInt(1000, 9999) }.distinct()
                val sorted = if (isAscending) list.sorted() else list.sortedDescending()
                val orderName = if (isAscending) "ពីតូចទៅធំ (លំដាប់កើន)" else "ពីធំមកតូច (លំដាប់ចុះ)"
                MathProblem(
                    id = "L1_3_$index",
                    lessonId = "L1_3",
                    titleKhmer = "ការរៀបលំដាប់ចំនួនគត់",
                    instructionKhmer = "ចូររៀបលំដាប់ចំនួនខាងក្រោម $orderName ៖",
                    questionKhmer = list.joinToString(" , "),
                    problemType = ProblemType.ORDER_NUMBERS,
                    points = 10,
                    studentAnswerPlaceholderKhmer = "លំដាប់៖ ................................................................",
                    teacherAnswerKhmer = sorted.joinToString(" < ") { it.toString() }.let { if (isAscending) it else sorted.joinToString(" > ") },
                    teacherExplanationKhmer = "រៀបលំដាប់ $orderName៖ ${sorted.joinToString(", ")}។"
                )
            }
            else -> {
                // Odd and Even classification (ចំនួនគូ និងចំនួនសេស)
                val nums = listOf(
                    random.nextInt(100, 999) * 2,
                    random.nextInt(100, 999) * 2 + 1,
                    random.nextInt(1000, 4999) * 2,
                    random.nextInt(1000, 4999) * 2 + 1
                ).shuffled()
                val even = nums.filter { it % 2 == 0 }
                val odd = nums.filter { it % 2 != 0 }
                MathProblem(
                    id = "L1_3_$index",
                    lessonId = "L1_3",
                    titleKhmer = "ការសម្គាល់ចំនួនគត់គូ និងចំនួនគត់សេស",
                    instructionKhmer = "ចូរញែកចំនួនខាងក្រោមជាចំនួនគូ និងចំនួនសេស៖",
                    questionKhmer = nums.joinToString(", "),
                    problemType = ProblemType.FILL_BLANK,
                    points = 10,
                    studentAnswerPlaceholderKhmer = "ចំនួនគូ៖ .................... | ចំនួនសេស៖ ....................",
                    teacherAnswerKhmer = "ចំនួនគូ៖ ${even.joinToString(", ")} | ចំនួនសេស៖ ${odd.joinToString(", ")}",
                    teacherExplanationKhmer = "ចំនួនគូជាចំនួនដែលមានលេខចុងបញ្ចប់ 0, 2, 4, 6, 8 (ចែកដាច់នឹង 2)។ ចំនួនសេសជាចំនួនដែលមានលេខចុងបញ្ចប់ 1, 3, 5, 7, 9 (ចែកនឹង 2 មានសំណល់ 1)។"
                )
            }
        }
    }

    // --- LESSON 2.1: ADDITION UP TO 10 000 ---
    private fun generateAdditionProblem(index: Int, random: Random, difficulty: String): MathProblem {
        val isWordProb = (index % 4 == 3)
        if (isWordProb) {
            val a = random.nextInt(1200, 4500)
            val b = random.nextInt(800, 3200)
            val sum = a + b
            val items = listOf("កូនមាន់", "ផ្លែស្វាយ", "សៀវភៅ", "ដបទឹកបរិសុទ្ធ", "កញ្ចប់មី", "ម៉ែត្រក្រណាត់")
            val item = items[random.nextInt(items.size)]
            val unit = when (item) {
                "កូនមាន់" -> "ក្បាល"
                "ផ្លែស្វាយ" -> "ផ្លែ"
                "សៀវភៅ" -> "ក្បាល"
                "ដបទឹកបរិសុទ្ធ" -> "ដប"
                "កញ្ចប់មី" -> "កញ្ចប់"
                else -> "ម៉ែត្រ"
            }
            return MathProblem(
                id = "L2_1_WP_$index",
                lessonId = "L2_1",
                titleKhmer = "ចំណោទប្រមាណវិធីបូក",
                instructionKhmer = "ចូរអានប្រធានចំណោទខាងក្រោម គូសដ្យាក្រាម រួចដោះស្រាយឱ្យបានត្រឹមត្រូវ៖",
                questionKhmer = "កសិដ្ឋានមួយ លើកទីមួយប្រមូលផលបាន $item ចំនួន $a $unit។ លើកទីពីរប្រមូលផលបានចំនួន $b $unit បន្ថែមទៀត។ តើទាំងពីរលើក កសិដ្ឋាននោះប្រមូលផលបាន $item សរុបទាំងអស់ប៉ុន្មាន $unit?",
                problemType = ProblemType.WORD_PROBLEM,
                points = 15,
                operandA = a.toString(),
                operandB = b.toString(),
                operationSymbol = "+",
                studentAnswerPlaceholderKhmer = "របៀបឆ្លើយ៖\nរកចំនួន${item}សរុបទាំងពីរលើក៖ .................................\nប្រមាណវិធី៖ .....................................................\nឆ្លើយថា៖ .....................................................",
                teacherAnswerKhmer = "ទាំងពីរលើកប្រមូលផលបាន $sum $unit",
                teacherExplanationKhmer = "វិធីដោះស្រាយចំណោទតាមលោក Polya ៤ជំហាន៖\n១. យល់ដឹងពីបញ្ហា៖ លើកទី១ បាន $a $unit, លើកទី២ បាន $b $unit។ សួររក៖ ចំនួនសរុបទាំងពីរលើក។\n២. បង្កើតផែនការ៖ ធ្វើប្រមាណវិធីបូក ($a + $b)\n៣. អនុវត្តផែនការ៖ $a + $b = $sum\n៤. ផ្ទៀងផ្ទាត់៖ $sum - $b = $a (ត្រឹមត្រូវ)។",
                wordProblem = WordProblemDetail(
                    storyKhmer = "កសិដ្ឋានមួយ លើកទីមួយប្រមូលផលបាន $item ចំនួន $a $unit។ លើកទីពីរប្រមូលផលបានចំនួន $b $unit បន្ថែមទៀត។",
                    questionsKhmer = listOf("តើទាំងពីរលើក កសិដ្ឋាននោះប្រមូលផលបាន $item សរុបទាំងអស់ប៉ុន្មាន $unit?"),
                    answerStatementsKhmer = listOf("ចំនួន${item}សរុបទាំងពីរលើកគឺ៖"),
                    calculationFormulaKhmer = "$a + $b = $sum ($unit)",
                    verificationKhmer = "$sum - $b = $a ($unit) ត្រឹមត្រូវ",
                    finalAnswerKhmer = "ឆ្លើយថា៖ ប្រមូលផលបានសរុប $sum $unit"
                )
            )
        }

        // Standard calculation
        val hasCarry = (index % 2 == 1)
        val a = if (hasCarry) random.nextInt(2567, 5899) else random.nextInt(2120, 5230)
        val b = if (hasCarry) random.nextInt(1456, 3899) else random.nextInt(1120, 3420)
        val sum = a + b

        return MathProblem(
            id = "L2_1_CALC_$index",
            lessonId = "L2_1",
            titleKhmer = if (hasCarry) "វិធីបូកចំនួនគត់មានត្រាទុក" else "វិធីបូកចំនួនគត់គ្មានត្រាទុក",
            instructionKhmer = "ចូរគណនាផលបូកខាងក្រោមតាមជួរឈរឱ្យបានត្រឹមត្រូវ៖",
            questionKhmer = "$a + $b = ?",
            problemType = ProblemType.CALCULATION_VERTICAL,
            points = 10,
            operandA = a.toString(),
            operandB = b.toString(),
            operationSymbol = "+",
            primaryNumber = sum,
            studentAnswerPlaceholderKhmer = "ផលបូក = ............................",
            teacherAnswerKhmer = "$sum",
            teacherExplanationKhmer = "បូកតាមលំដាប់ខ្ទង់ពីស្តាំទៅឆ្វេង៖ ខ្ទង់រាយ បូកខ្ទង់រាយ, ខ្ទង់ដប់ បូកខ្ទង់ដប់, ខ្ទង់រយ បូកខ្ទង់រយ, ខ្ទង់ពាន់ បូកខ្ទង់ពាន់។ ផលបូកគឺ $sum។"
        )
    }

    // --- LESSON 2.2: SUBTRACTION UP TO 10 000 ---
    private fun generateSubtractionProblem(index: Int, random: Random, difficulty: String): MathProblem {
        val isWordProb = (index % 4 == 3)
        if (isWordProb) {
            val total = random.nextInt(3500, 8500)
            val spent = random.nextInt(1200, total - 500)
            val remain = total - spent
            return MathProblem(
                id = "L2_2_WP_$index",
                lessonId = "L2_2",
                titleKhmer = "ចំណោទប្រមាណវិធីដក",
                instructionKhmer = "ចូរអានប្រធានចំណោទ គូសដ្យាក្រាម រួចដោះស្រាយ និងផ្ទៀងផ្ទាត់៖",
                questionKhmer = "ពូសុខមានប្រាក់ចំនួន $total រៀល។ គាត់បានទិញសម្ភារៈសិក្សាអស់ $spent រៀល។ តើគាត់នៅសល់ប្រាក់ចំនួនប៉ុន្មានរៀល?",
                problemType = ProblemType.WORD_PROBLEM,
                points = 15,
                operandA = total.toString(),
                operandB = spent.toString(),
                operationSymbol = "-",
                studentAnswerPlaceholderKhmer = "របៀបឆ្លើយ៖\nរកប្រាក់ដែលពូសុខនៅសល់៖ .................................\nប្រមាណវិធី៖ .....................................................\nឆ្លើយថា៖ .....................................................",
                teacherAnswerKhmer = "គាត់នៅសល់ប្រាក់ $remain រៀល",
                teacherExplanationKhmer = "ដំណោះស្រាយតាម ៤ជំហាន Polya៖\n១. យល់ដឹង៖ មានប្រាក់ $total រៀល, ចំណាយ $spent រៀល។ សួររក៖ ប្រាក់នៅសល់។\n២. ផែនការ៖ ធ្វើប្រមាណវិធីដក ($total - $spent)\n៣. អនុវត្ត៖ $total - $spent = $remain\n៤. ផ្ទៀងផ្ទាត់៖ $remain + $spent = $total រៀល (ត្រឹមត្រូវ)។",
                wordProblem = WordProblemDetail(
                    storyKhmer = "ពូសុខមានប្រាក់ចំនួន $total រៀល។ គាត់បានទិញសម្ភារៈសិក្សាអស់ $spent រៀល។",
                    questionsKhmer = listOf("តើគាត់នៅសល់ប្រាក់ចំនួនប៉ុន្មានរៀល?"),
                    answerStatementsKhmer = listOf("ប្រាក់ដែលពូសុខនៅសល់គឺ៖"),
                    calculationFormulaKhmer = "$total - $spent = $remain (រៀល)",
                    verificationKhmer = "$remain + $spent = $total (រៀល) ត្រឹមត្រូវ",
                    finalAnswerKhmer = "ឆ្លើយថា៖ នៅសល់ប្រាក់ $remain រៀល"
                )
            )
        }

        val hasBorrow = (index % 2 == 1)
        val min = if (hasBorrow) 4200 else 3500
        val a = random.nextInt(min, 8999)
        val b = if (hasBorrow) random.nextInt(1450, a - 200) else random.nextInt(1100, 2400)
        val diff = a - b

        return MathProblem(
            id = "L2_2_CALC_$index",
            lessonId = "L2_2",
            titleKhmer = if (hasBorrow) "វិធីដកចំនួនគត់មានខ្ចី" else "វិធីដកចំនួនគត់គ្មានខ្ចី",
            instructionKhmer = "ចូរគណនាផលដកខាងក្រោមតាមជួរឈរ និងផ្ទៀងផ្ទាត់ឡើងវិញ៖",
            questionKhmer = "$a - $b = ?",
            problemType = ProblemType.CALCULATION_VERTICAL,
            points = 10,
            operandA = a.toString(),
            operandB = b.toString(),
            operationSymbol = "-",
            primaryNumber = diff,
            studentAnswerPlaceholderKhmer = "ផលដក = ............................",
            teacherAnswerKhmer = "$diff",
            teacherExplanationKhmer = "ដកតាមខ្ទង់ពីស្តាំទៅឆ្វេង (ខ្ទង់រាយ, ដប់, រយ, ពាន់)។ ផ្ទៀងផ្ទាត់៖ ផលដក + តួដក = $diff + $b = $a (ត្រឹមត្រូវ)។"
        )
    }

    // --- LESSON 3.1: MULTIPLICATION ---
    private fun generateMultiplicationProblem(index: Int, random: Random, difficulty: String): MathProblem {
        val sub = index % 3
        return when (sub) {
            0 -> {
                // 2-digit or 3-digit by 1-digit
                val a = random.nextInt(123, 456)
                val b = random.nextInt(3, 9)
                val prod = a * b
                MathProblem(
                    id = "L3_1_$index",
                    lessonId = "L3_1",
                    titleKhmer = "ប្រមាណវិធីគុណលេខ ៣ខ្ទង់ នឹង១ខ្ទង់",
                    instructionKhmer = "ចូរគណនាប្រមាណវិធីគុណខាងក្រោមតាមជួរឈរ៖",
                    questionKhmer = "$a × $b = ?",
                    problemType = ProblemType.CALCULATION_VERTICAL,
                    points = 10,
                    operandA = a.toString(),
                    operandB = b.toString(),
                    operationSymbol = "×",
                    primaryNumber = prod,
                    studentAnswerPlaceholderKhmer = "ផលគុណ = ............................",
                    teacherAnswerKhmer = "$prod",
                    teacherExplanationKhmer = "ជំហានទី១៖ គុណខ្ទង់រាយ $b × ${a % 10} \nជំហានទី២៖ គុណខ្ទង់ដប់ $b × ${(a / 10) % 10} បូកត្រាទុក \nជំហានទី៣៖ គុណខ្ទង់រយ $b × ${a / 100} បូកត្រាទុក។ ផលគុណសរុបគឺ $prod។"
                )
            }
            1 -> {
                // 2-digit by 2-digit
                val a = random.nextInt(15, 48)
                val b = random.nextInt(12, 35)
                val prod = a * b
                MathProblem(
                    id = "L3_1_$index",
                    lessonId = "L3_1",
                    titleKhmer = "ប្រមាណវិធីគុណលេខ ២ខ្ទង់ នឹង២ខ្ទង់",
                    instructionKhmer = "ចូរគណនាប្រមាណវិធីគុណខាងក្រោមតាមជួរឈរ៖",
                    questionKhmer = "$a × $b = ?",
                    problemType = ProblemType.CALCULATION_VERTICAL,
                    points = 10,
                    operandA = a.toString(),
                    operandB = b.toString(),
                    operationSymbol = "×",
                    primaryNumber = prod,
                    studentAnswerPlaceholderKhmer = "ផលគុណ = ............................",
                    teacherAnswerKhmer = "$prod",
                    teacherExplanationKhmer = "គុណ $a នឹងខ្ទង់រាយ ${b % 10} បាន ${a * (b % 10)}។ បន្ទាប់មកគុណ $a នឹងខ្ទង់ដប់ ${b / 10} បាន ${a * (b / 10)}0។ បូកបញ្ចូលគ្នាបាន $prod។"
                )
            }
            else -> {
                // Word problem
                val boxes = random.nextInt(4, 12)
                val perBox = random.nextInt(25, 60)
                val total = boxes * perBox
                MathProblem(
                    id = "L3_1_WP_$index",
                    lessonId = "L3_1",
                    titleKhmer = "ចំណោទប្រមាណវិធីគុណ",
                    instructionKhmer = "ចូរអានប្រធានចំណោទ រួចដោះស្រាយឱ្យបានត្រឹមត្រូវ៖",
                    questionKhmer = "ឡានមួយគ្រឿងដឹកកេសទឹកក្រូចចំនួន $boxes កេស។ ក្នុងមួយកេសៗមានទឹកក្រូច $perBox ដប។ តើឡាននោះដឹកទឹកក្រូចសរុបទាំងអស់ប៉ុន្មានដប?",
                    problemType = ProblemType.WORD_PROBLEM,
                    points = 15,
                    operandA = perBox.toString(),
                    operandB = boxes.toString(),
                    operationSymbol = "×",
                    studentAnswerPlaceholderKhmer = "របៀបឆ្លើយ៖\nរកចំនួនទឹកក្រូចសរុបទាំងអស់៖ .................................\nប្រមាណវិធី៖ .....................................................\nឆ្លើយថា៖ .....................................................",
                    teacherAnswerKhmer = "ដឹកទឹកក្រូចសរុបទាំងអស់បាន $total ដប",
                    teacherExplanationKhmer = "របៀបដោះស្រាយ៖ ចំនួនទឹកក្រូចសរុប = ចំនួនក្នុង១កេស ($perBox) × ចំនួនកេស ($boxes) = $total ដប។",
                    wordProblem = WordProblemDetail(
                        storyKhmer = "ឡានមួយគ្រឿងដឹកកេសទឹកក្រូចចំនួន $boxes កេស។ ក្នុងមួយកេសៗមានទឹកក្រូច $perBox ដប។",
                        questionsKhmer = listOf("តើឡាននោះដឹកទឹកក្រូចសរុបទាំងអស់ប៉ុន្មានដប?"),
                        answerStatementsKhmer = listOf("ចំនួនទឹកក្រូចសរុបទាំងអស់គឺ៖"),
                        calculationFormulaKhmer = "$perBox × $boxes = $total (ដប)",
                        verificationKhmer = "$total ÷ $boxes = $perBox (ដប) ត្រឹមត្រូវ",
                        finalAnswerKhmer = "ឆ្លើយថា៖ ដឹកទឹកក្រូចសរុប $total ដប"
                    )
                )
            }
        }
    }

    // --- LESSON 3.2: DIVISION 2-DIGIT BY 1-DIGIT ---
    private fun generateDivision2DigitProblem(index: Int, random: Random, difficulty: String): MathProblem {
        val hasRemainder = (index % 2 == 1)
        val divisor = random.nextInt(2, 9)
        val quotient = random.nextInt(10, 45)
        val rem = if (hasRemainder) random.nextInt(1, divisor) else 0
        val dividend = quotient * divisor + rem

        return MathProblem(
            id = "L3_2_$index",
            lessonId = "L3_2",
            titleKhmer = if (hasRemainder) "វិធីចែកមានសំណល់ (២ខ្ទង់ នឹង១ខ្ទង់)" else "វិធីចែកដាច់ (២ខ្ទង់ នឹង១ខ្ទង់)",
            instructionKhmer = "ចូរគណនាប្រមាណវិធីចែកខាងក្រោម និងផ្ទៀងផ្ទាត់ចម្លើយ៖",
            questionKhmer = "$dividend ÷ $divisor = ?",
            problemType = ProblemType.CALCULATION_VERTICAL,
            points = 10,
            operandA = dividend.toString(),
            operandB = divisor.toString(),
            operationSymbol = "÷",
            studentAnswerPlaceholderKhmer = if (hasRemainder) "ផលចែក = ...... សំណល់ = ......" else "ផលចែក = ......",
            teacherAnswerKhmer = if (hasRemainder) "ផលចែក = $quotient សំណល់ = $rem" else "ផលចែក = $quotient",
            teacherExplanationKhmer = "តំណាងចែក = $dividend, តួចែក = $divisor។ \nចែកបានផលចែក $quotient ${if (hasRemainder) "និងសល់សំណល់ $rem" else "គ្មានសំណល់"}។ \nផ្ទៀងផ្ទាត់៖ ($quotient × $divisor) + $rem = $dividend (ត្រឹមត្រូវ)។"
        )
    }

    // --- LESSON 3.3: DIVISION 3-DIGIT BY 1-DIGIT ---
    private fun generateDivision3DigitProblem(index: Int, random: Random, difficulty: String): MathProblem {
        val divisor = random.nextInt(3, 9)
        val quotient = random.nextInt(105, 320)
        val rem = if (random.nextBoolean()) random.nextInt(1, divisor) else 0
        val dividend = quotient * divisor + rem

        return MathProblem(
            id = "L3_3_$index",
            lessonId = "L3_3",
            titleKhmer = "វិធីចែកចំនួន ៣ខ្ទង់ នឹង១ខ្ទង់",
            instructionKhmer = "ចូរគណនាប្រមាណវិធីចែកខាងក្រោម និងផ្ទៀងផ្ទាត់ចម្លើយ៖",
            questionKhmer = "$dividend ÷ $divisor = ?",
            problemType = ProblemType.CALCULATION_VERTICAL,
            points = 10,
            operandA = dividend.toString(),
            operandB = divisor.toString(),
            operationSymbol = "÷",
            studentAnswerPlaceholderKhmer = if (rem > 0) "ផលចែក = ...... សំណល់ = ......" else "ផលចែក = ......",
            teacherAnswerKhmer = if (rem > 0) "ផលចែក = $quotient សំណល់ = $rem" else "ផលចែក = $quotient",
            teacherExplanationKhmer = "ចែកតាមលំដាប់ខ្ទង់៖ ចែកខ្ទង់រយ បន្ទាប់មកខ្ទង់ដប់ និងខ្ទង់រាយ។ \nផលចែកគឺ $quotient ${if (rem > 0) "សំណល់ $rem" else "ចែកដាច់"}។ \nផ្ទៀងផ្ទាត់៖ ($quotient × $divisor) + $rem = $dividend។"
        )
    }

    // --- LESSON 3.4: FRACTIONS ---
    private fun generateFractionProblem(index: Int, random: Random, difficulty: String): MathProblem {
        val sub = index % 3
        return when (sub) {
            0 -> {
                // Fraction identification or definition
                val denom = listOf(3, 4, 5, 6, 8, 10).random(random)
                val num = random.nextInt(1, denom)
                MathProblem(
                    id = "L3_4_$index",
                    lessonId = "L3_4",
                    titleKhmer = "សញ្ញាណប្រភាគ (ភាគយក និងភាគបែង)",
                    instructionKhmer = "ចូរពិនិត្យប្រភាគខាងក្រោម រួចកំណត់ភាគយក និងភាគបែង៖",
                    questionKhmer = "ក្នុងប្រភាគ  $num/$denom  (អានថា «$num ភាគ $denom») ៖",
                    problemType = ProblemType.FRACTION_IDENTIFY,
                    points = 10,
                    studentAnswerPlaceholderKhmer = "ភាគយកគឺ៖ ............ | ភាគបែងគឺ៖ ............",
                    teacherAnswerKhmer = "ភាគយកគឺ $num , ភាគបែងគឺ $denom",
                    teacherExplanationKhmer = "ភាគយក ($num) តំណាងឱ្យចំនួនចំណែកដែលបានយក ឬផាត់ពណ៌។ ភាគបែង ($denom) តំណាងឱ្យចំនួនចំណែកស្មើៗគ្នាទាំងអស់នៃវត្ថុទាំងមូល។"
                )
            }
            1 -> {
                // Compare fractions with like denominators
                val denom = listOf(5, 7, 8, 9, 10).random(random)
                val n1 = random.nextInt(1, denom)
                var n2 = random.nextInt(1, denom)
                if (n1 == n2) n2 = if (n1 < denom - 1) n1 + 1 else n1 - 1
                val sign = if (n1 > n2) ">" else "<"
                MathProblem(
                    id = "L3_4_$index",
                    lessonId = "L3_4",
                    titleKhmer = "ការប្រៀបធៀបប្រភាគមានភាគបែងដូចគ្នា",
                    instructionKhmer = "ចូរបំពេញសញ្ញា < ឬ > ក្នុងប្រអប់ [  ] ឱ្យបានត្រឹមត្រូវ៖",
                    questionKhmer = "$n1/$denom   [   ]   $n2/$denom",
                    problemType = ProblemType.COMPARE_SIGNS,
                    points = 10,
                    studentAnswerPlaceholderKhmer = "សញ្ញា៖ [   ]",
                    teacherAnswerKhmer = "$n1/$denom $sign $n2/$denom",
                    teacherExplanationKhmer = "វិធាន៖ ប្រភាគពីរដែលមានភាគបែងដូចគ្នា ប្រភាគណាមានភាគយកធំជាង ប្រភាគនោះធំជាង។ ដោយ $n1 $sign $n2 ដូចនេះ $n1/$denom $sign $n2/$denom។"
                )
            }
            else -> {
                // Addition / Subtraction with like denominators
                val denom = listOf(4, 5, 6, 7, 8, 9, 10).random(random)
                val isAddition = random.nextBoolean()
                if (isAddition) {
                    val n1 = random.nextInt(1, denom - 2)
                    val n2 = random.nextInt(1, denom - n1)
                    val res = n1 + n2
                    MathProblem(
                        id = "L3_4_$index",
                        lessonId = "L3_4",
                        titleKhmer = "វិធីបូកប្រភាគមានភាគបែងដូចគ្នា",
                        instructionKhmer = "ចូរគណនាផលបូកនៃប្រភាគខាងក្រោម៖",
                        questionKhmer = "$n1/$denom + $n2/$denom = ?",
                        problemType = ProblemType.FRACTION_CALCULATION,
                        points = 10,
                        studentAnswerPlaceholderKhmer = "ផលបូក = ...... / ......",
                        teacherAnswerKhmer = "$res/$denom",
                        teacherExplanationKhmer = "វិធាន៖ ដើម្បីបូកប្រភាគដែលមានភាគបែងដូចគ្នា យើងត្រូវបូកភាគយក និងភាគយក ($n1 + $n2 = $res) ហើយរក្សាភាគបែងទុកនៅដដែល ($denom)។ ដូចនេះបាន $res/$denom។"
                    )
                } else {
                    val n1 = random.nextInt(3, denom)
                    val n2 = random.nextInt(1, n1)
                    val res = n1 - n2
                    MathProblem(
                        id = "L3_4_$index",
                        lessonId = "L3_4",
                        titleKhmer = "វិធីដកប្រភាគមានភាគបែងដូចគ្នា",
                        instructionKhmer = "ចូរគណនាផលដកនៃប្រភាគខាងក្រោម៖",
                        questionKhmer = "$n1/$denom - $n2/$denom = ?",
                        problemType = ProblemType.FRACTION_CALCULATION,
                        points = 10,
                        studentAnswerPlaceholderKhmer = "ផលដក = ...... / ......",
                        teacherAnswerKhmer = "$res/$denom",
                        teacherExplanationKhmer = "វិធាន៖ ដើម្បីដកប្រភាគដែលមានភាគបែងដូចគ្នា យើងត្រូវដកភាគយក និងភាគយក ($n1 - $n2 = $res) ហើយរក្សាភាគបែងទុកនៅដដែល ($denom)។ ដូចនេះបាន $res/$denom។"
                    )
                }
            }
        }
    }
}
