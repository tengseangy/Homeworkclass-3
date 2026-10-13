package com.example.util

import com.example.model.MathProblem
import com.example.model.ProblemType
import com.example.model.Worksheet

object WorksheetExporter {

    fun formatStudentWorksheetText(worksheet: Worksheet): String {
        val sb = StringBuilder()
        sb.appendLine("==================================================")
        sb.appendLine("ព្រះរាជាណាចក្រកម្ពុជា")
        sb.appendLine("ជាតិ  សាសនា  ព្រះមហាក្សត្រ")
        sb.appendLine("--------------------------------------------------")
        sb.appendLine(worksheet.titleKhmer)
        sb.appendLine("សាលា៖ ${worksheet.schoolNameKhmer}   គ្រូ៖ ${worksheet.teacherNameKhmer}")
        sb.appendLine("ឈ្មោះសិស្ស៖ ................................. ថ្នាក់ទី៖ ៣......")
        sb.appendLine("កាលបរិច្ឆេទ៖ ........................... ពិន្ទុសរុប៖ ${worksheet.totalScore}")
        sb.appendLine("ការណែនាំ៖ ${worksheet.instructionsKhmer}")
        sb.appendLine("==================================================")
        sb.appendLine()

        worksheet.problems.forEachIndexed { idx, p ->
            sb.appendLine("លំហាត់ទី ${idx + 1} (${p.points} ពិន្ទុ)៖ ${p.titleKhmer}")
            if (p.instructionKhmer.isNotBlank()) sb.appendLine("  ${p.instructionKhmer}")
            sb.appendLine("  ${p.questionKhmer}")

            if (p.problemType == ProblemType.MULTIPLE_CHOICE) {
                p.options.forEach { opt ->
                    sb.appendLine("    ${opt.key}. ${opt.textKhmer}")
                }
                sb.appendLine("  ចម្លើយ៖ [    ]")
            } else if (p.problemType == ProblemType.WORD_PROBLEM) {
                sb.appendLine("  ដំណោះស្រាយ៖")
                sb.appendLine("  ...................................................................")
                sb.appendLine("  ...................................................................")
                sb.appendLine("  ឆ្លើយថា៖ ...........................................................")
            } else {
                sb.appendLine("  ${p.studentAnswerPlaceholderKhmer}")
            }
            sb.appendLine()
        }

        sb.appendLine("--- សូមជូនពរឱ្យទទួលបានលទ្ធផលល្អប្រសើរ! ---")
        return sb.toString()
    }

    fun formatTeacherAnswerKeyText(worksheet: Worksheet): String {
        val sb = StringBuilder()
        sb.appendLine("==================================================")
        sb.appendLine("សន្លឹកគន្លឹះចម្លើយ និងកម្រងពិន្ទុសម្រាប់គ្រូបង្រៀន")
        sb.appendLine(worksheet.titleKhmer)
        sb.appendLine("មុខវិជ្ជា៖ ${worksheet.subjectKhmer} (ពិន្ទុពេញ៖ ${worksheet.totalScore})")
        sb.appendLine("==================================================")
        sb.appendLine()

        worksheet.problems.forEachIndexed { idx, p ->
            sb.appendLine("លំហាត់ទី ${idx + 1} (${p.points} ពិន្ទុ)៖ ${p.titleKhmer}")
            sb.appendLine("  ប្រធាន៖ ${p.questionKhmer}")
            sb.appendLine("  ✓ ចម្លើយត្រឹមត្រូវ៖ ${p.teacherAnswerKhmer}")
            sb.appendLine("  ការបកស្រាយ/ដំណោះស្រាយ៖ ${p.teacherExplanationKhmer}")
            if (p.wordProblem != null) {
                sb.appendLine("  - ប្រមាណវិធី៖ ${p.wordProblem.calculationFormulaKhmer}")
                sb.appendLine("  - ផ្ទៀងផ្ទាត់៖ ${p.wordProblem.verificationKhmer}")
            }
            sb.appendLine("--------------------------------------------------")
        }
        return sb.toString()
    }
}
