package com.erica.metas.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erica.metas.data.SalesViewModel
import com.erica.metas.ui.components.ConfettiEffect
import com.erica.metas.ui.components.EvolutionProgressBar
import com.erica.metas.ui.components.DailySalesItem
import com.erica.metas.ui.components.LiquidProgressBar
import com.erica.metas.utils.CurrencyUtils

@Composable
fun HomeScreen(
    viewModel: SalesViewModel,
    modifier: Modifier = Modifier
) {
    val goals by viewModel.goals.collectAsState()
    val daysList by viewModel.daysList.collectAsState()
    val showConfetti by viewModel.showConfetti.collectAsState()

    val totalSales = viewModel.getTotalSales()
    val requiredDailyAvg = viewModel.getRequiredDailyAverage()
    val activeTarget = viewModel.getActiveTargetValue()
    val activePercentage = viewModel.getActiveTargetPercentage()
    val isSuperMeta = viewModel.isSuperMetaActive()
    val monthYearLabel = viewModel.getMonthYearLabel()

    // Filtra os domingos da exibição principal
    val workingDaysList = daysList.filter { !it.isSunday }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 0. Navegação por Meses (Histórico)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                IconButton(onClick = { viewModel.previousMonth() }) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Mês Anterior",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = monthYearLabel,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(onClick = { viewModel.nextMonth() }) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Próximo Mês",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // 1. Cabeçalho "Olá Érica"
            Text(
                text = "Olá ${goals.userName}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 2. Campo que é APENAS texto sem box / container: ex "venda diária R$ 5.850,39"
            val formattedRequiredAvg = CurrencyUtils.formatBRL(requiredDailyAvg)
            Text(
                text = "venda diária $formattedRequiredAvg",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )


            Spacer(modifier = Modifier.height(16.dp))

            // 3. Barra de evolução de porcentagem com trilha horizontal (EvolutionProgressBar)
            EvolutionProgressBar(
                currentSales = totalSales,
                meta = goals.meta,
                superMeta = goals.superMeta
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Cabeçalho da lista de dias: Caixa mostrando o total vendido no mês
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Total Vendido no Mês",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                        Text(
                            text = CurrencyUtils.formatBRL(totalSales),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    val faltaMeta = (goals.meta - totalSales).coerceAtLeast(0.0)
                    val faltaSuperMeta = (goals.superMeta - totalSales).coerceAtLeast(0.0)

                    Column(horizontalAlignment = Alignment.End) {
                        if (faltaMeta > 0) {
                            Text(
                                text = "Falta Meta",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                            Text(
                                text = CurrencyUtils.formatBRL(faltaMeta),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        if (faltaSuperMeta > 0) {
                            if (faltaMeta > 0) Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Falta Super Meta",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                            Text(
                                text = CurrencyUtils.formatBRL(faltaSuperMeta),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Lista dos dias do mês (ignorando domingos)
            Text(
                text = "Lançamento Diário de Vendas",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(workingDaysList, key = { it.dayNumber }) { daySale ->
                    DailySalesItem(
                        daySale = daySale,
                        requiredDailyAvg = requiredDailyAvg,
                        onDigitsChange = { newDigits ->
                            viewModel.updateDayAmount(daySale.dayNumber, newDigits)
                        },
                        onHolidayToggle = { isHoliday ->
                            viewModel.toggleHoliday(daySale.dayNumber, isHoliday)
                        }
                    )
                }
            }
        }

        // Animação de Confetes para celebração de metas batidas!
        ConfettiEffect(
            isVisible = showConfetti,
            onAnimationEnd = { viewModel.dismissConfetti() }
        )
    }
}
