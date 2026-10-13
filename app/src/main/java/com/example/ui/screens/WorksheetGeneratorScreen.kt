package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.generator.CurriculumData
import com.example.ui.components.ExamHeaderView
import com.example.ui.components.MathProblemView
import com.example.util.WorksheetExporter
import com.example.viewmodel.AppUiState
import com.example.viewmodel.MathGrade3ViewModel
import com.example.viewmodel.WorksheetDetailTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorksheetGeneratorScreen(
    viewModel: MathGrade3ViewModel,
    uiState: AppUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showConfigSection by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFD))
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "បង្កើតសន្លឹកកិច្ចការផ្ទះ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color(0xFF0F172A)
            ),
            actions = {
                // Toggle Config Card
                IconButton(
                    onClick = { showConfigSection = !showConfigSection },
                    modifier = Modifier.testTag("toggle_config_button")
                ) {
                    Icon(
                        imageVector = if (showConfigSection) Icons.Default.Tune else Icons.Default.Settings,
                        contentDescription = "Options",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                // Save Worksheet Button
                IconButton(
                    onClick = { viewModel.saveCurrentWorksheet() },
                    modifier = Modifier.testTag("save_worksheet_top_button")
                ) {
                    Icon(
                        imageVector = if (uiState.isWorksheetSaved) Icons.Default.BookmarkAdded else Icons.Default.BookmarkBorder,
                        contentDescription = "Save",
                        tint = if (uiState.isWorksheetSaved) Color(0xFF16A34A) else MaterialTheme.colorScheme.primary
                    )
                }

                // Share / Export Button
                IconButton(
                    onClick = {
                        val active = uiState.activeWorksheet ?: return@IconButton
                        val shareText = if (uiState.detailTab == WorksheetDetailTab.TEACHER_KEY)
                            WorksheetExporter.formatTeacherAnswerKeyText(active)
                        else
                            WorksheetExporter.formatStudentWorksheetText(active)

                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "ចែករំលែកសន្លឹកកិច្ចការ"))
                    },
                    modifier = Modifier.testTag("share_worksheet_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color(0xFF475569)
                    )
                }
            }
        )

        // Generator Options Filter Box (collapsible or top card)
        if (showConfigSection) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "ជ្រើសរើសជំពូក និងមេរៀន",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Chapter Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CurriculumData.chapters.forEach { chap ->
                            val isSelected = (chap.id == uiState.selectedChapterId)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFF1F5F9))
                                    .clickable { viewModel.selectChapter(chap.id) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "ជំពូក ${chap.id}",
                                    color = if (isSelected) Color.White else Color(0xFF475569),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Lesson Selector Chips
                    val currentChapter = CurriculumData.chapters.find { it.id == uiState.selectedChapterId }
                    currentChapter?.lessons?.forEach { lesson ->
                        val isLessonSel = (lesson.id == uiState.selectedLessonId)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isLessonSel) Color(0xFFEFF6FF) else Color.Transparent)
                                .border(1.dp, if (isLessonSel) Color(0xFF3B82F6) else Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                .clickable { viewModel.selectLesson(lesson.id) }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isLessonSel,
                                onClick = { viewModel.selectLesson(lesson.id) }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = lesson.titleKhmer,
                                    fontSize = 13.sp,
                                    fontWeight = if (isLessonSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isLessonSel) Color(0xFF1E40AF) else Color(0xFF1E293B)
                                )
                                Text(
                                    text = lesson.subtitleKhmer,
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Question Count and Difficulty
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "ចំនួនលំហាត់៖", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(4, 6, 8, 10).forEach { cnt ->
                                val isCntSel = (cnt == uiState.questionCount)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isCntSel) MaterialTheme.colorScheme.primary else Color(0xFFF1F5F9))
                                        .clickable { viewModel.updateQuestionCount(cnt) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "$cnt",
                                        color = if (isCntSel) Color.White else Color(0xFF475569),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Generate Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.generateHomework()
                                showConfigSection = false
                            },
                            modifier = Modifier.weight(1f).testTag("generate_homework_action_button"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("បង្កើតលំហាត់ថ្មី", fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Segmented Tab Selector: Student Sheet vs Teacher Answer Key vs Interactive Practice
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabButton(
                    title = "សន្លឹកសិស្ស",
                    isSelected = uiState.detailTab == WorksheetDetailTab.STUDENT_SHEET,
                    testTag = "tab_student_sheet",
                    onClick = { viewModel.setDetailTab(WorksheetDetailTab.STUDENT_SHEET) }
                )
                TabButton(
                    title = "ចម្លើយគ្រូ",
                    isSelected = uiState.detailTab == WorksheetDetailTab.TEACHER_KEY,
                    testTag = "tab_teacher_key",
                    onClick = { viewModel.setDetailTab(WorksheetDetailTab.TEACHER_KEY) }
                )
                TabButton(
                    title = "សិស្សធ្វើផ្ទាល់",
                    isSelected = uiState.detailTab == WorksheetDetailTab.INTERACTIVE_PRACTICE,
                    testTag = "tab_interactive_practice",
                    onClick = { viewModel.setDetailTab(WorksheetDetailTab.INTERACTIVE_PRACTICE) }
                )
            }
        }

        // Active Worksheet Body
        val worksheet = uiState.activeWorksheet
        if (worksheet == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("មិនទាន់មានសន្លឹកកិច្ចការនៅឡើយទេ", color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
            ) {
                // Header (National Motto & Exam/Worksheet Info)
                item {
                    ExamHeaderView(
                        worksheet = worksheet,
                        isTeacherMode = (uiState.detailTab == WorksheetDetailTab.TEACHER_KEY)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Interactive Practice Score Summary Card if submitted
                if (uiState.detailTab == WorksheetDetailTab.INTERACTIVE_PRACTICE && uiState.practiceResult != null) {
                    item {
                        PracticeResultCard(
                            result = uiState.practiceResult,
                            onReset = { viewModel.resetStudentPractice() }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // Quick Action Toolbar above exercises
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "លំហាត់សរុប៖ ${worksheet.problems.size} (ពិន្ទុពេញ៖ ${worksheet.totalScore})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Copy Text Button
                            OutlinedButton(
                                onClick = {
                                    val text = if (uiState.detailTab == WorksheetDetailTab.TEACHER_KEY)
                                        WorksheetExporter.formatTeacherAnswerKeyText(worksheet)
                                    else
                                        WorksheetExporter.formatStudentWorksheetText(worksheet)

                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Worksheet", text)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "បានចម្លងអត្ថបទសន្លឹកកិច្ចការ!", Toast.LENGTH_SHORT).show()
                                },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("copy_text_button")
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ចម្លង", fontSize = 12.sp)
                            }

                            // Regenerate with different random numbers
                            Button(
                                onClick = { viewModel.generateHomework() },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                                modifier = Modifier.testTag("regenerate_button")
                            ) {
                                Icon(imageVector = Icons.Default.Autorenew, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ប្តូរលេខ", fontSize = 12.sp)
                            }
                        }
                    }
                }

                // Problem Cards
                itemsIndexed(worksheet.problems) { idx, prob ->
                    MathProblemView(
                        index = idx + 1,
                        problem = prob,
                        showTeacherSolution = (uiState.detailTab == WorksheetDetailTab.TEACHER_KEY),
                        useKhmerNumerals = uiState.useKhmerNumerals,
                        interactiveMode = (uiState.detailTab == WorksheetDetailTab.INTERACTIVE_PRACTICE),
                        studentAnswer = uiState.studentAnswers[prob.id] ?: "",
                        selectedOption = uiState.studentSelectedOptions[prob.id],
                        onAnswerChanged = { ans -> viewModel.onStudentAnswerInput(prob.id, ans) },
                        onOptionSelected = { opt -> viewModel.onStudentOptionSelected(prob.id, opt) }
                    )
                }

                // Submit Button for Interactive Practice
                if (uiState.detailTab == WorksheetDetailTab.INTERACTIVE_PRACTICE && uiState.practiceResult == null) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.submitStudentPractice() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_practice_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                        ) {
                            Icon(imageVector = Icons.Default.DoneAll, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "កែ និងវាយតម្លៃលទ្ធផលភ្លាមៗ (Check Answers)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabButton(
    title: String,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 8.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color(0xFF1565C0) else Color(0xFF64748B)
        )
    }
}

@Composable
private fun PracticeResultCard(
    result: com.example.model.StudentPracticeResult,
    onReset: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (result.passed) Color(0xFFDCFCE7) else Color(0xFFFEF2F2)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (result.passed) Color(0xFF22C55E) else Color(0xFFEF4444)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (result.passed) "អបអរសាទរ! អ្នកបានជាប់ការធ្វើតេស្ត 🎉" else "សូមព្យាយាមម្តងទៀត! 💪",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (result.passed) Color(0xFF166534) else Color(0xFF991B1B)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("ពិន្ទុទទួលបាន", fontSize = 12.sp, color = Color(0xFF475569))
                    Text(
                        text = "${result.totalScore} / ${result.maxScore}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("ភាគរយ", fontSize = 12.sp, color = Color(0xFF475569))
                    Text(
                        text = "${result.percentage.toInt()}%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (result.passed) Color(0xFF16A34A) else Color(0xFFDC2626)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("និទ្ទេស", fontSize = 12.sp, color = Color(0xFF475569))
                    Text(
                        text = result.gradeLetterKhmer,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = onReset,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("ធ្វើលំហាត់ឡើងវិញ (Reset)", fontSize = 13.sp)
            }
        }
    }
}
