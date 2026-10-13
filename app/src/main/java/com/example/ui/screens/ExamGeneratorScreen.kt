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
import com.example.model.WorksheetCategory
import com.example.ui.components.ExamHeaderView
import com.example.ui.components.MathProblemView
import com.example.util.WorksheetExporter
import com.example.viewmodel.AppUiState
import com.example.viewmodel.MathGrade3ViewModel
import com.example.viewmodel.WorksheetDetailTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamGeneratorScreen(
    viewModel: MathGrade3ViewModel,
    uiState: AppUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(WorksheetCategory.MONTHLY_TEST) }
    var selectedChapterForExam by remember { mutableStateOf(1) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFD))
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "បង្កើតវិញ្ញាសាប្រឡងថ្នាក់ទី៣",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color(0xFF0F172A)
            ),
            actions = {
                // Save Exam
                IconButton(
                    onClick = { viewModel.saveCurrentWorksheet() },
                    modifier = Modifier.testTag("save_exam_top_button")
                ) {
                    Icon(
                        imageVector = if (uiState.isWorksheetSaved) Icons.Default.BookmarkAdded else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Exam",
                        tint = if (uiState.isWorksheetSaved) Color(0xFF16A34A) else MaterialTheme.colorScheme.primary
                    )
                }

                // Share Exam
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
                        context.startActivity(Intent.createChooser(sendIntent, "ចែករំលែកវិញ្ញាសាប្រឡង"))
                    },
                    modifier = Modifier.testTag("share_exam_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color(0xFF475569)
                    )
                }
            }
        )

        // Exam Category Selection Card
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
                    text = "ជ្រើសរើសប្រភេទវិញ្ញាសា (ពិន្ទុពេញ ១០០)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ExamPresetPill(
                        title = "ប្រចាំខែ",
                        isSelected = selectedCategory == WorksheetCategory.MONTHLY_TEST,
                        modifier = Modifier.weight(1f),
                        testTag = "preset_monthly_exam"
                    ) {
                        selectedCategory = WorksheetCategory.MONTHLY_TEST
                        viewModel.generateExam(WorksheetCategory.MONTHLY_TEST)
                    }

                    ExamPresetPill(
                        title = "ឆមាសទី១",
                        isSelected = selectedCategory == WorksheetCategory.SEMESTER_EXAM_1,
                        modifier = Modifier.weight(1f),
                        testTag = "preset_semester1_exam"
                    ) {
                        selectedCategory = WorksheetCategory.SEMESTER_EXAM_1
                        viewModel.generateExam(WorksheetCategory.SEMESTER_EXAM_1)
                    }

                    ExamPresetPill(
                        title = "ឆមាសទី២",
                        isSelected = selectedCategory == WorksheetCategory.SEMESTER_EXAM_2,
                        modifier = Modifier.weight(1f),
                        testTag = "preset_semester2_exam"
                    ) {
                        selectedCategory = WorksheetCategory.SEMESTER_EXAM_2
                        viewModel.generateExam(WorksheetCategory.SEMESTER_EXAM_2)
                    }

                    ExamPresetPill(
                        title = "តាមជំពូក",
                        isSelected = selectedCategory == WorksheetCategory.CHAPTER_TEST,
                        modifier = Modifier.weight(1f),
                        testTag = "preset_chapter_exam"
                    ) {
                        selectedCategory = WorksheetCategory.CHAPTER_TEST
                        viewModel.generateExam(WorksheetCategory.CHAPTER_TEST, selectedChapterForExam)
                    }
                }

                if (selectedCategory == WorksheetCategory.CHAPTER_TEST) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "ជ្រើសរើសជំពូកប្រឡង៖",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            1 to "ជំពូក១ (ចំនួនគត់)",
                            2 to "ជំពូក២ (បូក-ដក)",
                            3 to "ជំពូក៣ (គុណ-ចែក-ប្រភាគ)"
                        ).forEach { (cid, label) ->
                            val isSel = (selectedChapterForExam == cid)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Color(0xFF00897B) else Color(0xFFF1F5F9))
                                    .clickable {
                                        selectedChapterForExam = cid
                                        viewModel.generateExam(WorksheetCategory.CHAPTER_TEST, cid)
                                    }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) Color.White else Color(0xFF334155),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Toggle Tabs: Student Paper vs Teacher Grading Key vs Practice
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabButtonExam(
                    title = "សន្លឹកវិញ្ញាសាសិស្ស",
                    isSelected = uiState.detailTab == WorksheetDetailTab.STUDENT_SHEET,
                    testTag = "exam_tab_student",
                    onClick = { viewModel.setDetailTab(WorksheetDetailTab.STUDENT_SHEET) }
                )
                TabButtonExam(
                    title = "កម្រងចម្លើយ & ពិន្ទុគ្រូ",
                    isSelected = uiState.detailTab == WorksheetDetailTab.TEACHER_KEY,
                    testTag = "exam_tab_teacher",
                    onClick = { viewModel.setDetailTab(WorksheetDetailTab.TEACHER_KEY) }
                )
                TabButtonExam(
                    title = "សិស្សសាកល្បងប្រឡង",
                    isSelected = uiState.detailTab == WorksheetDetailTab.INTERACTIVE_PRACTICE,
                    testTag = "exam_tab_practice",
                    onClick = { viewModel.setDetailTab(WorksheetDetailTab.INTERACTIVE_PRACTICE) }
                )
            }
        }

        // Exam Content
        val worksheet = uiState.activeWorksheet
        if (worksheet == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("មិនទាន់មានវិញ្ញាសានៅឡើយទេ", color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp)
            ) {
                // Header (National Motto & Exam Info)
                item {
                    ExamHeaderView(
                        worksheet = worksheet,
                        isTeacherMode = (uiState.detailTab == WorksheetDetailTab.TEACHER_KEY)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Interactive Score Result
                if (uiState.detailTab == WorksheetDetailTab.INTERACTIVE_PRACTICE && uiState.practiceResult != null) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (uiState.practiceResult.passed) Color(0xFFDCFCE7) else Color(0xFFFEF2F2)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (uiState.practiceResult.passed) Color(0xFF22C55E) else Color(0xFFEF4444)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (uiState.practiceResult.passed) "លទ្ធផល៖ ជាប់ការប្រឡង 🎉" else "លទ្ធផល៖ ធ្លាក់ការប្រឡង ⚠️",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = if (uiState.practiceResult.passed) Color(0xFF166534) else Color(0xFF991B1B)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "ពិន្ទុសរុប៖ ${uiState.practiceResult.totalScore} / ${uiState.practiceResult.maxScore} (${uiState.practiceResult.percentage.toInt()}%) • ${uiState.practiceResult.gradeLetterKhmer}",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(onClick = { viewModel.resetStudentPractice() }) {
                                    Text("ប្រឡងឡើងវិញ")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                // Toolbar Actions
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "រចនាសម្ព័ន្ធវិញ្ញាសា៖ ៤ផ្នែក (១០០ ពិន្ទុ)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF334155)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Copy Text
                            OutlinedButton(
                                onClick = {
                                    val text = if (uiState.detailTab == WorksheetDetailTab.TEACHER_KEY)
                                        WorksheetExporter.formatTeacherAnswerKeyText(worksheet)
                                    else
                                        WorksheetExporter.formatStudentWorksheetText(worksheet)

                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Exam", text)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "បានចម្លងអត្ថបទវិញ្ញាសា!", Toast.LENGTH_SHORT).show()
                                },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ចម្លង", fontSize = 12.sp)
                            }

                            // Regenerate
                            Button(
                                onClick = {
                                    viewModel.generateExam(selectedCategory, selectedChapterForExam)
                                },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                            ) {
                                Icon(imageVector = Icons.Default.Autorenew, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("វិញ្ញាសាថ្មី", fontSize = 12.sp)
                            }
                        }
                    }
                }

                // Problems list
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

                // Practice Submission
                if (uiState.detailTab == WorksheetDetailTab.INTERACTIVE_PRACTICE && uiState.practiceResult == null) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.submitStudentPractice() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_exam_practice_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                        ) {
                            Icon(imageVector = Icons.Default.FactCheck, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ដាក់ស្នើ និងកាត់ពិន្ទុភ្លាមៗ (Grade Exam)",
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
private fun ExamPresetPill(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFF00897B) else Color(0xFFF1F5F9))
            .clickable { onClick() }
            .padding(vertical = 8.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF334155)
        )
    }
}

@Composable
private fun RowScope.TabButtonExam(
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
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color(0xFF00897B) else Color(0xFF64748B)
        )
    }
}
