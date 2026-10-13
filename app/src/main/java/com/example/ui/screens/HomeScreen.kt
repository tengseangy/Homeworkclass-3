package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.generator.CurriculumData
import com.example.model.WorksheetCategory
import com.example.viewmodel.AppUiState
import com.example.viewmodel.MainNavigationTab
import com.example.viewmodel.MathGrade3ViewModel

@Composable
fun HomeScreen(
    viewModel: MathGrade3ViewModel,
    uiState: AppUiState,
    modifier: Modifier = Modifier
) {
    var showSchoolDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFD)),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Header Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0x33FFFFFF))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "ក្រសួងអប់រំ យុវជន និងកីឡា • VVOB ២០២១",
                                    color = Color(0xFFFFD54F),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "គណិតវិទ្យា ថ្នាក់ទី៣",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                            )
                            Text(
                                text = "ជំនួយការគ្រូ៖ សន្លឹកកិច្ចការផ្ទះ & វិញ្ញាសាប្រឡង",
                                color = Color(0xFFE2E8F0),
                                fontSize = 14.sp
                            )
                        }

                        IconButton(
                            onClick = { showSchoolDialog = true },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0x22FFFFFF))
                                .testTag("school_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit School Info",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // School and Teacher Banner pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x22FFFFFF),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD54F),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = uiState.schoolName,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "គ្រូបង្រៀន៖ ${uiState.teacherName}",
                                        color = Color(0xFFCBD5E1),
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            // Khmer numerals toggle
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.toggleKhmerNumerals(!uiState.useKhmerNumerals) }
                                    .background(Color(0x33000000))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (uiState.useKhmerNumerals) "លេខខ្មែរ (១២៣)" else "លេខសកល (123)",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Title: Primary Creation Hub
        item {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp)) {
                Text(
                    text = "បង្កើតសន្លឹកកិច្ចការ និងវិញ្ញាសា",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        fontSize = 17.sp
                    )
                )
                Text(
                    text = "បង្កើតលំហាត់សិស្ស និងចម្លើយគ្រូតាមកម្មវិធីសិក្សាជាតិ",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp
                )
            }
        }

        // Action Hub Cards
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionCard(
                    title = "១. បង្កើតសន្លឹកកិច្ចការផ្ទះ (Homework)",
                    subtitle = "ជ្រើសរើសមេរៀន កម្រិត និងចំនួនលំហាត់ មានសន្លឹកសិស្ស និងគន្លឹះចម្លើយគ្រូ",
                    icon = Icons.Default.Assignment,
                    badgeText = "កិច្ចការផ្ទះ",
                    color = Color(0xFF1565C0),
                    containerColor = Color(0xFFEFF6FF),
                    testTag = "create_homework_card",
                    onClick = { viewModel.navigateToTab(MainNavigationTab.GENERATE_HOMEWORK) }
                )

                ActionCard(
                    title = "២. បង្កើតសន្លឹកវិញ្ញាសាប្រឡង (Exams)",
                    subtitle = "វិញ្ញាសាប្រចាំខែ ឆមាសទី១ ឆមាសទី២ និងតាមជំពូក មានកម្រងចម្លើយ និងពិន្ទុពេញ ១០០",
                    icon = Icons.Default.Description,
                    badgeText = "វិញ្ញាសាប្រឡង",
                    color = Color(0xFF00897B),
                    containerColor = Color(0xFFE0F2F1),
                    testTag = "create_exam_card",
                    onClick = { viewModel.navigateToTab(MainNavigationTab.GENERATE_EXAM) }
                )

                ActionCard(
                    title = "៣. សៀវភៅណែនាំគ្រូ និងវិធីសាស្ត្របង្រៀន",
                    subtitle = "វិធីសាស្ត្រ Polya ៤ ជំហាន, វិធីបំបែកនិងផ្តុំ, បន្ទាត់គ្មានលេខ, គំនិតភាន់ច្រឡំ",
                    icon = Icons.Default.MenuBook,
                    badgeText = "មគ្គុទ្ទេសក៍",
                    color = Color(0xFFF57F17),
                    containerColor = Color(0xFFFFFBEB),
                    testTag = "teaching_guide_card",
                    onClick = { viewModel.navigateToTab(MainNavigationTab.TEACHING_GUIDE) }
                )

                ActionCard(
                    title = "៤. កិច្ចការដែលបានរក្សាទុក (${uiState.savedWorksheets.size})",
                    subtitle = "រក្សាទុក ចម្លងអត្ថបទ ចែករំលែក ឬបោះពុម្ពសន្លឹកកិច្ចការ និងចម្លើយ",
                    icon = Icons.Default.FolderSpecial,
                    badgeText = "បណ្ណសារដ្ឋាន",
                    color = Color(0xFF6B21A8),
                    containerColor = Color(0xFFFAF5FF),
                    testTag = "saved_worksheets_card",
                    onClick = { viewModel.navigateToTab(MainNavigationTab.SAVED_WORKSHEETS) }
                )
            }
        }

        // Active Worksheet Preview (If Available)
        if (uiState.activeWorksheet != null) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "សន្លឹកកិច្ចការដែលកំពុងបើក",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Already in active worksheet, navigate to detail
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF1565C0))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFE0F2FE))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = when (uiState.activeWorksheet.category) {
                                            WorksheetCategory.HOMEWORK -> "សន្លឹកកិច្ចការផ្ទះ"
                                            WorksheetCategory.REINFORCEMENT -> "សន្លឹកពង្រឹង"
                                            WorksheetCategory.MONTHLY_TEST -> "ប្រឡងប្រចាំខែ"
                                            WorksheetCategory.SEMESTER_EXAM_1 -> "ឆមាសទី១"
                                            WorksheetCategory.SEMESTER_EXAM_2 -> "ឆមាសទី២"
                                            WorksheetCategory.CHAPTER_TEST -> "ប្រឡងជំពូក"
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0369A1)
                                    )
                                }

                                Text(
                                    text = "${uiState.activeWorksheet.problems.size} លំហាត់ • ${uiState.activeWorksheet.totalScore} ពិន្ទុ",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.activeWorksheet.titleKhmer,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = uiState.activeWorksheet.chapterNameKhmer.ifBlank { uiState.activeWorksheet.subjectKhmer },
                                fontSize = 13.sp,
                                color = Color(0xFF475569)
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.setDetailTab(com.example.viewmodel.WorksheetDetailTab.STUDENT_SHEET)
                                        viewModel.navigateToTab(MainNavigationTab.GENERATE_HOMEWORK)
                                    },
                                    modifier = Modifier.weight(1f).testTag("view_student_sheet_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("សន្លឹកសិស្ស", fontSize = 13.sp)
                                }

                                OutlinedButton(
                                    onClick = {
                                        viewModel.setDetailTab(com.example.viewmodel.WorksheetDetailTab.TEACHER_KEY)
                                        viewModel.navigateToTab(MainNavigationTab.GENERATE_HOMEWORK)
                                    },
                                    modifier = Modifier.weight(1f).testTag("view_teacher_key_button"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("ចម្លើយគ្រូ", fontSize = 13.sp, color = Color(0xFF1565C0))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Chapters Overview Section
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "មាតិកាកម្មវិធីសិក្សាគណិតវិទ្យា ថ្នាក់ទី៣",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                CurriculumData.chapters.forEach { chapter ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = chapter.titleKhmer,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = chapter.descriptionKhmer,
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${chapter.lessons.size} មេរៀនសំខាន់ៗ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF00897B)
                                )
                                TextButton(
                                    onClick = {
                                        viewModel.selectChapter(chapter.id)
                                        viewModel.navigateToTab(MainNavigationTab.GENERATE_HOMEWORK)
                                    }
                                ) {
                                    Text("បង្កើតលំហាត់ជំពូកនេះ →", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog for School & Teacher Name Setting
    if (showSchoolDialog) {
        var tempSchool by remember { mutableStateOf(uiState.schoolName) }
        var tempTeacher by remember { mutableStateOf(uiState.teacherName) }

        AlertDialog(
            onDismissRequest = { showSchoolDialog = false },
            title = {
                Text(
                    text = "កំណត់ព័ត៌មានសាលា និងគ្រូបង្រៀន",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "ព័ត៌មាននេះនឹងបង្ហាញលើក្បាលសន្លឹកកិច្ចការ និងវិញ្ញាសាប្រឡងទាំងអស់។",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                    OutlinedTextField(
                        value = tempSchool,
                        onValueChange = { tempSchool = it },
                        label = { Text("ឈ្មោះសាលាបឋមសិក្សា") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("school_name_input")
                    )
                    OutlinedTextField(
                        value = tempTeacher,
                        onValueChange = { tempTeacher = it },
                        label = { Text("ឈ្មោះលោកគ្រូ / អ្នកគ្រូ") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("teacher_name_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateSchoolName(tempSchool)
                        viewModel.updateTeacherName(tempTeacher)
                        showSchoolDialog = false
                    },
                    modifier = Modifier.testTag("save_school_button")
                ) {
                    Text("រក្សាទុក")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSchoolDialog = false }) {
                    Text("បោះបង់")
                }
            }
        )
    }
}

@Composable
private fun ActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeText: String,
    color: Color,
    containerColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF0F172A)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = Color(0xFF64748B)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
