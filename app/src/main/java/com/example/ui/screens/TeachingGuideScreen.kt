package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TeachingHandbookData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeachingGuideScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Strategies, 1: Misconceptions, 2: Times Table

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFD))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "មគ្គុទ្ទេសក៍វិធីសាស្ត្របង្រៀន (VVOB & MoEYS)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color(0xFF0F172A)
            )
        )

        // Guide Tabs
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
                listOf(
                    0 to "យុទ្ធវិធីបង្រៀន (CPA & Polya)",
                    1 to "គំនិតភាន់ច្រឡំ & ការកែ",
                    2 to "តារាងមេគុណ ២-១០"
                ).forEach { (idx, title) ->
                    val isSel = (selectedTab == idx)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) Color.White else Color.Transparent)
                            .clickable { selectedTab = idx }
                            .padding(vertical = 8.dp)
                            .testTag("guide_tab_$idx"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSel) Color(0xFFF57F17) else Color(0xFF64748B),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    // Teaching Strategies
                    items(TeachingHandbookData.strategies) { strategy ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = Color(0xFFF57F17),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = strategy.titleKhmer,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color(0xFF0F172A)
                                        )
                                        Text(
                                            text = strategy.subtitleKhmer,
                                            fontSize = 12.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = strategy.descriptionKhmer,
                                    fontSize = 13.5.sp,
                                    lineHeight = 21.sp,
                                    color = Color(0xFF334155)
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = Color(0xFFF1F5F9))
                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "ជំហានអនុវត្តជាក់ស្តែងក្នុងថ្នាក់៖",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                strategy.stepsKhmer.forEachIndexed { i, step ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 3.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            text = "•",
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFF57F17),
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = step,
                                            fontSize = 13.sp,
                                            lineHeight = 19.sp,
                                            color = Color(0xFF475569)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFFFFBEB))
                                        .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(8.dp))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = "💡 គន្លឹះគ្រូ៖ ${strategy.tipsForTeacherKhmer}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF92400E),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Misconceptions
                    items(TeachingHandbookData.misconceptions) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = item.topicKhmer,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                // Wrong concept
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFFEF2F2))
                                        .border(1.dp, Color(0xFFFECACA), RoundedCornerShape(8.dp))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "❌ គំនិតភាន់ច្រឡំរបស់សិស្ស៖",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color(0xFFB91C1C)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.wrongIdeaKhmer,
                                            fontSize = 13.sp,
                                            color = Color(0xFF7F1D1D),
                                            lineHeight = 19.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Correct concept
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF0FDF4))
                                        .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(8.dp))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "✓ បញ្ញត្តិត្រឹមត្រូវ៖",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color(0xFF15803D)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.correctIdeaKhmer,
                                            fontSize = 13.sp,
                                            color = Color(0xFF14532D),
                                            lineHeight = 19.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "🛠️ វិធីកែសម្រួលគរុកោសល្យ៖ ${item.pedagogicalSolutionKhmer}",
                                    fontSize = 12.5.sp,
                                    color = Color(0xFF475569),
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
                2 -> {
                    // Times table
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "តារាងមេគុណ ២ ដល់ ១០ តាមកម្មវិធីសិក្សាជាតិ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF1E293B)
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                for (base in 2..10) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFF8FAFC))
                                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = "មេគុណ $base",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF1565C0)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                for (multiplier in 1..5) {
                                                    Text(
                                                        text = "$base × $multiplier = ${base * multiplier}",
                                                        fontSize = 13.sp,
                                                        color = Color(0xFF334155),
                                                        modifier = Modifier.padding(vertical = 1.dp)
                                                    )
                                                }
                                            }
                                            Column(modifier = Modifier.weight(1f)) {
                                                for (multiplier in 6..10) {
                                                    Text(
                                                        text = "$base × $multiplier = ${base * multiplier}",
                                                        fontSize = 13.sp,
                                                        color = Color(0xFF334155),
                                                        modifier = Modifier.padding(vertical = 1.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
