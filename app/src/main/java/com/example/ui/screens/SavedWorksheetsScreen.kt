package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.Worksheet
import com.example.model.WorksheetCategory
import com.example.util.WorksheetExporter
import com.example.viewmodel.AppUiState
import com.example.viewmodel.MainNavigationTab
import com.example.viewmodel.MathGrade3ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedWorksheetsScreen(
    viewModel: MathGrade3ViewModel,
    uiState: AppUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ទាំងអស់") }

    val filteredList = uiState.savedWorksheets.filter { ws ->
        val matchesSearch = ws.titleKhmer.contains(searchQuery, ignoreCase = true) ||
                ws.lessonNameKhmer.contains(searchQuery, ignoreCase = true)
        val matchesCategory = when (selectedFilter) {
            "កិច្ចការផ្ទះ" -> ws.category == WorksheetCategory.HOMEWORK || ws.category == WorksheetCategory.REINFORCEMENT
            "វិញ្ញាសាប្រឡង" -> ws.category == WorksheetCategory.MONTHLY_TEST ||
                    ws.category == WorksheetCategory.SEMESTER_EXAM_1 ||
                    ws.category == WorksheetCategory.SEMESTER_EXAM_2 ||
                    ws.category == WorksheetCategory.CHAPTER_TEST
            else -> true
        }
        matchesSearch && matchesCategory
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFD))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "កិច្ចការ និងវិញ្ញាសាដែលបានរក្សាទុក",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color(0xFF0F172A)
            )
        )

        // Search Bar
        PaddingValues(horizontal = 16.dp, vertical = 8.dp).let {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("ស្វែងរកតាមចំណងជើង ឬមេរៀន...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("search_worksheets_input"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        // Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("ទាំងអស់", "កិច្ចការផ្ទះ", "វិញ្ញាសាប្រឡង").forEach { cat ->
                val isSel = (selectedFilter == cat)
                FilterChip(
                    selected = isSel,
                    onClick = { selectedFilter = cat },
                    label = { Text(cat, fontSize = 12.sp) },
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FolderOpen,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (searchQuery.isNotEmpty()) "មិនមានលទ្ធផលស្វែងរកទេ" else "មិនទាន់មានសន្លឹកកិច្ចការដែលបានរក្សាទុកនៅឡើយទេ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "នៅពេលអ្នកបង្កើតសន្លឹកកិច្ចការ ឬវិញ្ញាសា អ្នកអាចចុចប៊ូតុងរក្សាទុកដើម្បីមើលឡើងវិញពេលក្រោយ។",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { ws ->
                    SavedWorksheetCard(
                        worksheet = ws,
                        onOpen = {
                            viewModel.setActiveWorksheet(ws)
                            if (ws.category == WorksheetCategory.HOMEWORK || ws.category == WorksheetCategory.REINFORCEMENT) {
                                viewModel.navigateToTab(MainNavigationTab.GENERATE_HOMEWORK)
                            } else {
                                viewModel.navigateToTab(MainNavigationTab.GENERATE_EXAM)
                            }
                        },
                        onCopy = {
                            val text = WorksheetExporter.formatStudentWorksheetText(ws)
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Worksheet", text)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "បានចម្លងអត្ថបទ!", Toast.LENGTH_SHORT).show()
                        },
                        onShare = {
                            val text = WorksheetExporter.formatStudentWorksheetText(ws)
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, text)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "ចែករំលែក"))
                        },
                        onDelete = { viewModel.deleteSavedWorksheet(ws.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedWorksheetCard(
    worksheet: Worksheet,
    onOpen: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onOpen() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isExam = worksheet.category == WorksheetCategory.MONTHLY_TEST ||
                        worksheet.category == WorksheetCategory.SEMESTER_EXAM_1 ||
                        worksheet.category == WorksheetCategory.SEMESTER_EXAM_2 ||
                        worksheet.category == WorksheetCategory.CHAPTER_TEST

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isExam) Color(0xFFE0F2F1) else Color(0xFFEFF6FF))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = when (worksheet.category) {
                            WorksheetCategory.HOMEWORK -> "កិច្ចការផ្ទះ"
                            WorksheetCategory.REINFORCEMENT -> "សន្លឹកពង្រឹង"
                            WorksheetCategory.MONTHLY_TEST -> "ប្រឡងប្រចាំខែ"
                            WorksheetCategory.SEMESTER_EXAM_1 -> "ប្រឡងឆមាសទី១"
                            WorksheetCategory.SEMESTER_EXAM_2 -> "ប្រឡងឆមាសទី២"
                            WorksheetCategory.CHAPTER_TEST -> "ប្រឡងជំពូក"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isExam) Color(0xFF00796B) else Color(0xFF1D4ED8)
                    )
                }

                Text(
                    text = "${worksheet.problems.size} លំហាត់ • ${worksheet.totalScore} ពិន្ទុ",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = worksheet.titleKhmer,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF0F172A)
            )

            if (worksheet.lessonNameKhmer.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = worksheet.lessonNameKhmer,
                    fontSize = 12.sp,
                    color = Color(0xFF475569)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCopy, modifier = Modifier.size(36.dp)) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color(0xFF475569), modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onShare, modifier = Modifier.size(36.dp)) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color(0xFF475569), modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(
                    onClick = onOpen,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("បើកមើល", fontSize = 12.sp)
                }
            }
        }
    }
}
