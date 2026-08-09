package com.erica.metas.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erica.metas.ui.theme.SuperMetaGold
import com.erica.metas.ui.theme.WaveGreenEnd
import com.erica.metas.ui.theme.WaveGreenStart
import com.erica.metas.utils.CurrencyUtils
import java.util.Locale

@Composable
fun LiquidProgressBar(
    targetAmount: Double,
    percentage: Double,
    isSuperMeta: Boolean,
    modifier: Modifier = Modifier
) {
    val animatedPercentage by animateFloatAsState(
        targetValue = Math.min(percentage.toFloat() / 100f, 1.0f),
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "fillPercentage"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "waveTransition")
    
    // Onda frontal
    val waveOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), RepeatMode.Restart),
        label = "wave1"
    )
    // Onda traseira (mais lenta e oposta)
    val waveOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = -2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing), RepeatMode.Restart),
        label = "wave2"
    )

    val formattedPercentage = String.format(Locale("pt", "BR"), "%.1f", percentage)
    val labelText = "${CurrencyUtils.formatBRL(targetAmount)} - $formattedPercentage%"
    
    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
    val textLayoutResult = textMeasurer.measure(labelText, textStyle)
    val onSurfaceColor = MaterialTheme.colorScheme.onBackground

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        ) {
            Text(
                text = if (isSuperMeta) "META: SUPER META ATIVA" else "PROGRESSO DA META",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.2.sp,
                color = if (isSuperMeta) SuperMetaGold else MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )
            if (isSuperMeta) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = SuperMetaGold,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Aumentado para melhor visualização
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 2.dp,
                    brush = if (isSuperMeta) {
                        Brush.linearGradient(listOf(SuperMetaGold, Color.Transparent, SuperMetaGold))
                    } else {
                        Brush.linearGradient(listOf(MaterialTheme.colorScheme.outline, Color.Transparent))
                    },
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val fillHeight = height * animatedPercentage
                val waveY = height - fillHeight
                val textOffset = Offset(
                    (width - textLayoutResult.size.width) / 2,
                    (height - textLayoutResult.size.height) / 2
                )

                // 1. Texto de fundo (quando está vazio)
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = textOffset,
                    color = onSurfaceColor.copy(alpha = 0.6f)
                )

                // Função para criar o caminho da onda
                fun createWavePath(offset: Float, amplitude: Float, lengthFactor: Float): Path {
                    return Path().apply {
                        moveTo(0f, height)
                        lineTo(0f, waveY)
                        val waveLength = width / lengthFactor
                        var x = 0f
                        while (x <= width) {
                            val y = waveY + (Math.sin((x / waveLength * 2 * Math.PI) + offset) * amplitude).toFloat()
                            lineTo(x, y)
                            x += 10f
                        }
                        lineTo(width, height)
                        close()
                    }
                }

                val primaryColor = if (isSuperMeta) SuperMetaGold else WaveGreenStart
                val secondaryColor = if (isSuperMeta) Color(0xFFD97706) else WaveGreenEnd

                // 2. Desenha onda traseira (mais clara)
                drawPath(
                    path = createWavePath(waveOffset2, 6f, 1.2f),
                    color = primaryColor.copy(alpha = 0.3f)
                )

                // 3. Desenha onda frontal (cheia)
                val wavePath1 = createWavePath(waveOffset1, 8f, 1.8f)
                drawPath(
                    path = wavePath1,
                    brush = Brush.verticalGradient(listOf(primaryColor, secondaryColor))
                )

                // 4. Efeito de Vidro/Brilho no topo
                drawRect(
                    brush = Brush.verticalGradient(
                        0.0f to Color.White.copy(alpha = 0.15f),
                        0.4f to Color.Transparent
                    ),
                    size = size
                )

                // 5. Texto Invertido (dentro da água)
                clipPath(wavePath1) {
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = textOffset,
                        color = if (isSuperMeta) Color(0xFF451A03) else Color.White
                    )
                }
            }
        }
    }
}
