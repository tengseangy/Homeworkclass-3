package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Worksheet
import com.example.model.WorksheetCategory

@Composable
fun ExamHeaderView(
    worksheet: Worksheet,
    isTeacherMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Kingdom National Motto Header (MoEYS Standard)
        Text(
            text = "ព្រះរាជាណាចក្រកម្ពុជា",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color(0xFF1E293B)
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "ជាតិ  សាសនា  ព្រះមហាក្សត្រ",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF475569)
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "❖ ❖ ❖",
            color = Color(0xFFF57F17),
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Title
        Text(
            text = worksheet.titleKhmer,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = Color(0xFF0F172A)
            ),
            textAlign = TextAlign.Center
        )

        if (isTeacherMode) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFFEF3C7))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "★ សន្លឹកចម្លើយ និងកម្រងពិន្ទុសម្រាប់គ្រូ (Teacher Key & Rubric) ★",
                    color = Color(0xFF92400E),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // School and Teacher details row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1.2f)) {
                Text(
                    text = "សាលា៖ ${worksheet.schoolNameKhmer.ifBlank { "................................" }}",
                    fontSize = 13.sp,
                    color = Color(0xFF334155),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "បង្រៀនដោយ៖ ${worksheet.teacherNameKhmer.ifBlank { "................................" }}",
                    fontSize = 13.sp,
                    color = Color(0xFF334155),
                    fontWeight = FontWeight.Medium
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "មុខវិជ្ជា៖ ${worksheet.subjectKhmer}",
                    fontSize = 13.sp,
                    color = Color(0xFF334155),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "កាលបរិច្ឆេទ៖ ...........................",
                    fontSize = 13.sp,
                    color = Color(0xFF334155),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Student Info & Score Box Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF8FAFC))
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ឈ្មោះសិស្ស៖ .................................................",
                    fontSize = 13.sp,
                    color = Color(0xFF0F172A),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "ថ្នាក់ទី៖ ៣......",
                        fontSize = 13.sp,
                        color = Color(0xFF334155)
                    )
                    Text(
                        text = "លេខកូដ/លេខរៀង៖ .......",
                        fontSize = 13.sp,
                        color = Color(0xFF334155)
                    )
                }
            }

            // Score Box
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White)
                    .border(1.5.dp, Color(0xFF1565C0), RoundedCornerShape(6.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ពិន្ទុ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                    Text(
                        text = "..... / ${worksheet.totalScore}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
            }
        }

        if (worksheet.instructionsKhmer.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ការណែនាំ៖ ${worksheet.instructionsKhmer}",
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
