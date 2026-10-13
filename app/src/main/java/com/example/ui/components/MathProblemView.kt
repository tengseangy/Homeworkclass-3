package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*

@Composable
fun MathProblemView(
    index: Int,
    problem: MathProblem,
    showTeacherSolution: Boolean = false,
    useKhmerNumerals: Boolean = false,
    interactiveMode: Boolean = false,
    studentAnswer: String = "",
    selectedOption: String? = null,
    onAnswerChanged: (String) -> Unit = {},
    onOptionSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Exercise Number & Marks
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        val numLabel = if (useKhmerNumerals) KhmerNumberUtil.toKhmer(index) else index.toString()
                        Text(
                            text = "លំហាត់ទី $numLabel",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = problem.titleKhmer,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )
                    )
                }

                // Points Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    val ptsLabel = if (useKhmerNumerals) KhmerNumberUtil.toKhmer(problem.points) else problem.points.toString()
                    Text(
                        text = "$ptsLabel ពិន្ទុ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                }
            }

            if (problem.instructionKhmer.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = problem.instructionKhmer,
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Problem Content based on Type
            when (problem.problemType) {
                ProblemType.CALCULATION_VERTICAL -> {
                    VerticalCalculationDisplay(
                        problem = problem,
                        useKhmerNumerals = useKhmerNumerals,
                        showTeacherSolution = showTeacherSolution
                    )
                }
                ProblemType.MULTIPLE_CHOICE -> {
                    MultipleChoiceDisplay(
                        problem = problem,
                        selectedOption = selectedOption,
                        interactiveMode = interactiveMode,
                        showTeacherSolution = showTeacherSolution,
                        onOptionSelected = onOptionSelected
                    )
                }
                ProblemType.WORD_PROBLEM -> {
                    WordProblemDisplay(
                        problem = problem,
                        showTeacherSolution = showTeacherSolution
                    )
                }
                ProblemType.PLACE_VALUE_TABLE -> {
                    PlaceValueTableDisplay(
                        problem = problem,
                        useKhmerNumerals = useKhmerNumerals
                    )
                }
                ProblemType.FRACTION_CALCULATION, ProblemType.FRACTION_IDENTIFY -> {
                    FractionProblemDisplay(
                        problem = problem,
                        showTeacherSolution = showTeacherSolution
                    )
                }
                else -> {
                    // Standard question text
                    val qText = if (useKhmerNumerals) KhmerNumberUtil.toKhmer(problem.questionKhmer) else problem.questionKhmer
                    Text(
                        text = qText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1E293B)
                        )
                    )
                }
            }

            // Student writing space (in Student Sheet mode)
            if (!showTeacherSolution && problem.problemType != ProblemType.MULTIPLE_CHOICE) {
                Spacer(modifier = Modifier.height(12.dp))
                if (interactiveMode) {
                    OutlinedTextField(
                        value = studentAnswer,
                        onValueChange = onAnswerChanged,
                        label = { Text("សរសេរចម្លើយរបស់អ្នកទីនេះ...") },
                        placeholder = { Text(problem.studentAnswerPlaceholderKhmer) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = problem.problemType != ProblemType.WORD_PROBLEM
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = if (problem.problemType == ProblemType.WORD_PROBLEM)
                                "ដំណោះស្រាយ៖\n.......................................................................................\n.......................................................................................\nឆ្លើយថា៖ ........................................................................."
                            else
                                problem.studentAnswerPlaceholderKhmer,
                            fontSize = 13.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            // Teacher Solution Section (in Teacher Key mode)
            if (showTeacherSolution) {
                Spacer(modifier = Modifier.height(14.dp))
                TeacherSolutionBox(problem = problem, useKhmerNumerals = useKhmerNumerals)
            }
        }
    }
}

@Composable
private fun VerticalCalculationDisplay(
    problem: MathProblem,
    useKhmerNumerals: Boolean,
    showTeacherSolution: Boolean
) {
    val a = if (useKhmerNumerals) KhmerNumberUtil.toKhmer(problem.operandA) else problem.operandA
    val b = if (useKhmerNumerals) KhmerNumberUtil.toKhmer(problem.operandB) else problem.operandB
    val ans = if (useKhmerNumerals) KhmerNumberUtil.toKhmer(problem.teacherAnswerKhmer) else problem.teacherAnswerKhmer

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .width(180.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF1F5F9))
                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = a,
                fontSize = 22.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = problem.operationSymbol,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = b,
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }
            HorizontalDivider(
                color = Color(0xFF475569),
                thickness = 2.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = if (showTeacherSolution) ans else "..........",
                fontSize = 22.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = if (showTeacherSolution) Color(0xFF1565C0) else Color(0xFF64748B)
            )
        }
    }
}

@Composable
private fun MultipleChoiceDisplay(
    problem: MathProblem,
    selectedOption: String?,
    interactiveMode: Boolean,
    showTeacherSolution: Boolean,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = problem.questionKhmer,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.height(10.dp))

        problem.options.forEach { opt ->
            val isSelected = (selectedOption == opt.key)
            val isCorrect = opt.isCorrect

            val bgColor = when {
                showTeacherSolution && isCorrect -> Color(0xFFDCFCE7)
                interactiveMode && isSelected -> MaterialTheme.colorScheme.primaryContainer
                else -> Color(0xFFF8FAFC)
            }

            val borderColor = when {
                showTeacherSolution && isCorrect -> Color(0xFF22C55E)
                interactiveMode && isSelected -> MaterialTheme.colorScheme.primary
                else -> Color(0xFFE2E8F0)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(bgColor)
                    .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                    .clickable(enabled = interactiveMode) { onOptionSelected(opt.key) }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected || (showTeacherSolution && isCorrect)) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = opt.key,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isSelected || (showTeacherSolution && isCorrect)) Color.White else Color(0xFF475569)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = opt.textKhmer,
                    fontSize = 15.sp,
                    color = Color(0xFF1E293B),
                    fontWeight = if (showTeacherSolution && isCorrect) FontWeight.Bold else FontWeight.Normal
                )
                if (showTeacherSolution && isCorrect) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "✓ ត្រឹមត្រូវ",
                        color = Color(0xFF166534),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun WordProblemDisplay(
    problem: MathProblem,
    showTeacherSolution: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFEFF6FF))
                .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(8.dp))
                .padding(14.dp)
        ) {
            Text(
                text = problem.questionKhmer,
                fontSize = 15.sp,
                lineHeight = 23.sp,
                color = Color(0xFF1E3A8A)
            )
        }
    }
}

@Composable
private fun PlaceValueTableDisplay(
    problem: MathProblem,
    useKhmerNumerals: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = problem.questionKhmer,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(Color(0xFF1565C0))
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("ខ្ទង់ពាន់", "ខ្ទង់រយ", "ខ្ទង់ដប់", "ខ្ទង់រាយ").forEach { col ->
                Text(
                    text = col,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        // Table Cells
        val thousands = (problem.primaryNumber / 1000).let { if (useKhmerNumerals) KhmerNumberUtil.toKhmer(it) else it.toString() }
        val hundreds = ((problem.primaryNumber / 100) % 10).let { if (useKhmerNumerals) KhmerNumberUtil.toKhmer(it) else it.toString() }
        val tens = ((problem.primaryNumber / 10) % 10).let { if (useKhmerNumerals) KhmerNumberUtil.toKhmer(it) else it.toString() }
        val ones = (problem.primaryNumber % 10).let { if (useKhmerNumerals) KhmerNumberUtil.toKhmer(it) else it.toString() }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                .background(Color(0xFFF8FAFC))
                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(thousands, hundreds, tens, ones).forEach { v ->
                Text(
                    text = v,
                    color = Color(0xFF0F172A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FractionProblemDisplay(
    problem: MathProblem,
    showTeacherSolution: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = problem.questionKhmer,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1E293B)
        )
    }
}

@Composable
private fun TeacherSolutionBox(
    problem: MathProblem,
    useKhmerNumerals: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF0FDF4))
            .border(1.dp, Color(0xFF86EFAC), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "✓ គន្លឹះចម្លើយ និងដំណោះស្រាយរបស់គ្រូ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF166534)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${problem.points} ពិន្ទុ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF15803D)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "ចម្លើយត្រឹមត្រូវ៖ ${problem.teacherAnswerKhmer}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = problem.teacherExplanationKhmer,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = Color(0xFF334155)
            )

            if (problem.wordProblem != null) {
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(color = Color(0xFFBBF7D0), thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "ប្រមាណវិធី៖ ${problem.wordProblem.calculationFormulaKhmer}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF15803D)
                )
                Text(
                    text = "ផ្ទៀងផ្ទាត់៖ ${problem.wordProblem.verificationKhmer}",
                    fontSize = 12.sp,
                    color = Color(0xFF166534)
                )
            }
        }
    }
}
