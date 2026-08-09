package com.erica.metas.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.erica.metas.ui.theme.SuperMetaGold
import kotlin.random.Random

private data class Particle(
    val x: Float,
    var y: Float,
    val speed: Float,
    val size: Float,
    val color: Color,
    val rotationSpeed: Float
)

@Composable
fun ConfettiEffect(
    isVisible: Boolean,
    onAnimationEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isVisible) return

    val progress = remember { Animatable(0f) }

    val particles = remember {
        val colors = listOf(
            Color(0xFF10B981), Color(0xFF3B82F6), Color(0xFFF59E0B),
            Color(0xFFEC4899), Color(0xFF8B5CF6), SuperMetaGold
        )
        List(70) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat() * -0.5f,
                speed = Random.nextFloat() * 1.2f + 0.8f,
                size = Random.nextFloat() * 14f + 8f,
                color = colors.random(),
                rotationSpeed = Random.nextFloat() * 360f
            )
        }
    }

    LaunchedEffect(isVisible) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2500, easing = LinearEasing)
        )
        onAnimationEnd()
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val currentProgress = progress.value

        particles.forEach { particle ->
            val px = particle.x * canvasWidth
            val py = (particle.y + (currentProgress * particle.speed)) * canvasHeight

            drawRect(
                color = particle.color,
                topLeft = Offset(px, py),
                size = Size(particle.size, particle.size * 0.6f)
            )
        }
    }
}
