package com.erica.metas.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erica.metas.ui.theme.SuperMetaGold
import com.erica.metas.ui.theme.WaveGreenEnd
import com.erica.metas.ui.theme.WaveGreenStart
import com.erica.metas.utils.CurrencyUtils
import java.util.Locale

@Composable
fun EvolutionProgressBar(
    currentSales: Double,
    meta: Double,
    superMeta: Double,
    modifier: Modifier = Modifier
) {
    val isMetaHit = currentSales >= meta
    
    // 1. Lógica de Porcentagem (Exatamente como solicitado)
    val displayPercentage = if (!isMetaHit) {
        (currentSales / meta.coerceAtLeast(1.0) * 100).coerceIn(0.0, 100.0)
    } else {
        (currentSales / superMeta.coerceAtLeast(1.0) * 100).coerceIn(0.0, 100.0)
    }

    // 2. Progresso Visual Sincronizado com a Porcentagem
    val animatedProgress by animateFloatAsState(
        targetValue = (displayPercentage / 100f).toFloat(),
        animationSpec = tween(1500, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "animations")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1f, targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "shimmer"
    )
    val particleProgress by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)),
        label = "particles"
    )

    val targetValue = if (isMetaHit) superMeta else meta
    val labelSuffix = if (isMetaHit) " Super Meta" else " Meta"
    val formattedPercentage = String.format(Locale("pt", "BR"), "%.1f", displayPercentage)
    val labelText = "${CurrencyUtils.formatBRL(targetValue)} - $formattedPercentage%$labelSuffix"


    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(22.dp))
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val scope = this
                val width = scope.maxWidth
                val fillWidth = width * animatedProgress

                // Barra Colorida
                Box(
                    modifier = Modifier
                        .width(fillWidth)
                        .fillMaxHeight()
                        .background(
                            brush = if (isMetaHit) {
                                Brush.horizontalGradient(listOf(SuperMetaGold, Color(0xFFD97706)))
                            } else {
                                Brush.horizontalGradient(listOf(WaveGreenStart, WaveGreenEnd))
                            }
                        )
                )

                // Flow Particles
                androidx.compose.foundation.Canvas(modifier = Modifier.width(fillWidth).fillMaxHeight()) {
                    val pColor = Color.White.copy(alpha = 0.25f)
                    for (i in 0 until 12) {
                        val xPos = (size.width * ((particleProgress + i/12f) % 1f))
                        val yPos = (i * 137L % 100) / 100f * size.height
                        drawCircle(color = pColor, radius = (i % 4 + 2).toFloat(), center = Offset(xPos, yPos))
                    }
                }

                // Shimmer Effect
                Box(
                    modifier = Modifier
                        .width(fillWidth)
                        .fillMaxHeight()
                        .background(
                            Brush.linearGradient(
                                0.0f to Color.Transparent,
                                0.5f to Color.White.copy(alpha = 0.2f),
                                1.0f to Color.Transparent,
                                start = Offset(shimmerOffset * 1000f, 0f),
                                end = Offset((shimmerOffset + 0.4f) * 1000f, 400f)
                            )
                        )
                )

                // Marcador de Meta (Linha branca sutil)
                if (isMetaHit) {
                    val metaPos = (meta / superMeta).toFloat()
                    Box(
                        modifier = Modifier
                            .offset(x = width * metaPos)
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(Color.White.copy(alpha = 0.5f))
                    )
                }
            }

            // Texto Central
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = labelText,
                    color = if (animatedProgress > 0.4f) Color.White else MaterialTheme.colorScheme.onBackground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = TextStyle(shadow = Shadow(Color.Black.copy(alpha = 0.2f), blurRadius = 4f))
                )
            }
        }

        // Comissões
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 4.dp, top = 8.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Comissão Meta (0,1%)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                Text(CurrencyUtils.formatBRL(currentSales * 0.001), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = WaveGreenEnd)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Comissão Super Meta (0,3%)", fontSize = 10.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                Text(CurrencyUtils.formatBRL(currentSales * 0.003), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SuperMetaGold)
            }
        }
    }
}
