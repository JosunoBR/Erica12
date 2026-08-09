package com.erica.metas.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erica.metas.data.SalesViewModel
import com.erica.metas.ui.theme.GreenPrimary
import com.erica.metas.utils.CurrencyUtils

@Composable
fun ChartScreen(
    viewModel: SalesViewModel,
    modifier: Modifier = Modifier
) {
    val daysList by viewModel.daysList.collectAsState()
    val workingDays = daysList.filter { !it.isSunday }

    val maxAmount = Math.max(workingDays.maxOfOrNull { it.amount } ?: 1000.0, 1000.0)
    val requiredAvg = viewModel.getRequiredDailyAverage()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Evolução Diária das Vendas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Acompanhamento gráfico do valor vendido em cada dia útil do mês.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (workingDays.isEmpty()) {
                    Text(
                        text = "Nenhum dia disponível.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    val barColor = MaterialTheme.colorScheme.primary
                    val gridColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    val avgLineColor = Color(0xFFEF4444) // Red indicator line for daily goal

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height - 30f // Leave space for bottom day labels
                        val barCount = workingDays.size
                        val barWidth = (canvasWidth / barCount) * 0.6f
                        val barSpacing = (canvasWidth / barCount) * 0.4f

                        // Draw Grid Lines (3 reference horizontal lines)
                        for (i in 1..3) {
                            val y = canvasHeight * (i / 4f)
                            drawLine(
                                color = gridColor,
                                start = Offset(0f, y),
                                end = Offset(canvasWidth, y),
                                strokeWidth = 1f
                            )
                        }

                        // Draw Daily Average Goal Line (Dashed Red Line)
                        if (requiredAvg > 0 && requiredAvg <= maxAmount) {
                            val avgY = canvasHeight - ((requiredAvg / maxAmount) * canvasHeight).toFloat()
                            drawLine(
                                color = avgLineColor,
                                start = Offset(0f, avgY),
                                end = Offset(canvasWidth, avgY),
                                strokeWidth = 3f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                        }

                        // Draw Bars for each day
                        workingDays.forEachIndexed { index, daySale ->
                            val x = index * (barWidth + barSpacing) + (barSpacing / 2)
                            val barHeight = ((daySale.amount / maxAmount) * canvasHeight).toFloat()
                            val y = canvasHeight - barHeight

                            val barColorToUse = when {
                                daySale.isHoliday -> Color(0xFFD97706)
                                daySale.amount >= requiredAvg && requiredAvg > 0 -> GreenPrimary
                                else -> barColor
                            }

                            drawRoundRect(
                                color = barColorToUse,
                                topLeft = Offset(x, y),
                                size = Size(barWidth, Math.max(barHeight, 4f)),
                                cornerRadius = CornerRadius(6f, 6f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legenda do Gráfico
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
            )
            Text(
                text = "Vendas",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 4.dp, end = 8.dp)
            )

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Color(0xFFD97706), RoundedCornerShape(2.dp))
            )
            Text(
                text = "Feriado",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Meta Diária Média: ${CurrencyUtils.formatBRL(requiredAvg)}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF4444)
            )
        }

    }
}
