package com.erica.metas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erica.metas.data.DaySale
import com.erica.metas.ui.theme.GreenLightBg
import com.erica.metas.ui.theme.GreenPrimary

@Composable
fun DailySalesItem(
    daySale: DaySale,
    requiredDailyAvg: Double,
    onDigitsChange: (String) -> Unit,
    onHolidayToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isHoliday = daySale.isHoliday
    val isGoalMet = !isHoliday && daySale.amount >= requiredDailyAvg && requiredDailyAvg > 0 && daySale.amount > 0

    val cardBg = when {
        isHoliday -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        isGoalMet -> GreenLightBg.copy(alpha = 0.6f)
        else -> MaterialTheme.colorScheme.surface
    }

    val cardBorderColor = when {
        isHoliday -> Color.Gray.copy(alpha = 0.2f)
        isGoalMet -> GreenPrimary
        else -> MaterialTheme.colorScheme.outline
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Esquerda: Círculo do Dia + Nome do Dia + Badges
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                color = if (isHoliday) Color.LightGray.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                    ) {
                        Text(
                            text = "${daySale.dayNumber}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isHoliday) Color.Gray else MaterialTheme.colorScheme.primary
                        )
                    }

                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(
                            text = daySale.dayOfWeekName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isHoliday) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onBackground
                        )

                        // Indicadores Visuais de Desempenho
                        if (isHoliday) {
                            Text(
                                text = "🏖️ Feriado",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD97706)
                            )
                        } else if (isGoalMet) {
                            Text(
                                text = "🎯 Meta Batida!",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary
                            )
                        }
                    }
                }

                // Direita: Checkbox Feriado + Campo de Entrada Moeda
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Checkbox(
                            checked = isHoliday,
                            onCheckedChange = onHolidayToggle,
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFD97706)
                            )
                        )
                        Text(
                            text = "Feriado",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                    }

                    CurrencyAmountInput(
                        label = "Venda",
                        rawDigits = if (isHoliday) "0" else daySale.digits,
                        onDigitsChange = if (isHoliday) { _ -> } else onDigitsChange,
                        enabled = !isHoliday,
                        modifier = Modifier.width(140.dp)
                    )
                }
            }
        }
    }
}
