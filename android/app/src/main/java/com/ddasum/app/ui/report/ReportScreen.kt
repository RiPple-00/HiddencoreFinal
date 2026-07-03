package com.ddasum.app.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ddasum.app.data.model.WeeklyReport

@Composable
fun ReportScreen(viewModel: ReportViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Text(
                    text = "AI가 주간 리포트를 생성 중입니다. 최대 2분 정도 걸릴 수 있어요.",
                    modifier = Modifier.padding(top = 12.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        uiState.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = uiState.error.orEmpty(), textAlign = TextAlign.Center, modifier = Modifier.padding(24.dp))
        }
        uiState.report != null -> ReportContent(uiState.report!!)
    }
}

@Composable
private fun ReportContent(report: WeeklyReport) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "${report.patientName}님 주간 리포트", style = MaterialTheme.typography.titleMedium)
                Text(text = "${report.periodStart} ~ ${report.periodEnd}")
                Text(
                    text = "전체 수행률 ${report.overallRate}% · ${report.riskLevel}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "요약", style = MaterialTheme.typography.titleMedium)
                Text(text = report.summaryText, modifier = Modifier.padding(top = 4.dp))
                if (report.checklistInsight.isNotBlank()) {
                    Text(text = report.checklistInsight, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "요일별 수행률", style = MaterialTheme.typography.titleMedium)
                report.dailyRates.forEach { daily ->
                    Text(text = "${daily.day}(${daily.date}) — ${daily.rate}%", modifier = Modifier.padding(top = 4.dp))
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "체크리스트", style = MaterialTheme.typography.titleMedium)
                report.checklistRows.forEach { row ->
                    Text(text = "${row.label} — ${row.percent}%", modifier = Modifier.padding(top = 4.dp))
                }
            }
        }

        if (report.aiComments.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "AI 코멘트", style = MaterialTheme.typography.titleMedium)
                    report.aiComments.forEach { comment ->
                        Text(text = "· $comment", modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }

        if (report.nextWeekTips.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "다음 주 제안", style = MaterialTheme.typography.titleMedium)
                    report.nextWeekTips.forEach { tip ->
                        Text(text = "· $tip", modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
    }
}
