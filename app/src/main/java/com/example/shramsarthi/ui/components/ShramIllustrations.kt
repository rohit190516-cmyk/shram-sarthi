package com.example.shramsarthi.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

/**
 * Clean, high-fidelity custom Canvas illustrations for Shram Sarthi
 * matching the visual style of the reference images.
 */

@Composable
fun WorkerAvatarIllustration(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFFFF7ED), Color(0xFFFFEDD5))
                )
            )
            .border(2.dp, Color(0xFFE2E8F0), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Background subtle gradient circle
            drawCircle(
                color = Color(0xFFFEF3C7),
                radius = w * 0.48f,
                center = Offset(w * 0.5f, h * 0.5f)
            )

            // Torso / Orange safety vest
            drawOval(
                color = Color(0xFFEA580C), // Orange safety vest
                topLeft = Offset(w * 0.15f, h * 0.62f),
                size = Size(w * 0.7f, h * 0.6f)
            )

            // Inner shirt
            drawOval(
                color = Color(0xFF1E3A8A), // Blue shirt
                topLeft = Offset(w * 0.32f, h * 0.68f),
                size = Size(w * 0.36f, h * 0.45f)
            )

            // Reflective stripes on vest
            drawLine(
                color = Color(0xFFFDE047),
                start = Offset(w * 0.28f, h * 0.68f),
                end = Offset(w * 0.28f, h),
                strokeWidth = w * 0.05f
            )
            drawLine(
                color = Color(0xFFFDE047),
                start = Offset(w * 0.72f, h * 0.68f),
                end = Offset(w * 0.72f, h),
                strokeWidth = w * 0.05f
            )

            // Neck
            drawRect(
                color = Color(0xFFD97706),
                topLeft = Offset(w * 0.4f, h * 0.48f),
                size = Size(w * 0.2f, h * 0.18f)
            )

            // Head / Face
            drawOval(
                color = Color(0xFFF59E0B),
                topLeft = Offset(w * 0.28f, h * 0.26f),
                size = Size(w * 0.44f, h * 0.36f)
            )

            // Yellow Safety Helmet
            val helmetPath = Path().apply {
                moveTo(w * 0.2f, h * 0.32f)
                cubicTo(w * 0.2f, h * 0.08f, w * 0.8f, h * 0.08f, w * 0.8f, h * 0.32f)
                lineTo(w * 0.85f, h * 0.34f)
                lineTo(w * 0.15f, h * 0.34f)
                close()
            }
            drawPath(helmetPath, color = Color(0xFFEAB308))

            // Helmet rim
            drawRect(
                color = Color(0xFFCA8A04),
                topLeft = Offset(w * 0.15f, h * 0.32f),
                size = Size(w * 0.7f, h * 0.05f)
            )
        }
    }
}

@Composable
fun RobotSaathiIllustration(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.White)
            .border(2.5.dp, Color(0xFF10B981), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Yellow Helmet on Robot
            val helmetPath = Path().apply {
                moveTo(w * 0.22f, h * 0.35f)
                cubicTo(w * 0.22f, h * 0.06f, w * 0.78f, h * 0.06f, w * 0.78f, h * 0.35f)
                lineTo(w * 0.84f, h * 0.38f)
                lineTo(w * 0.16f, h * 0.38f)
                close()
            }
            drawPath(helmetPath, color = Color(0xFFFACC15))

            // Helmet Ridge
            drawRect(
                color = Color(0xFFEAB308),
                topLeft = Offset(w * 0.46f, h * 0.1f),
                size = Size(w * 0.08f, h * 0.25f)
            )

            // Robot Head / Face Screen (Light Cyan/Teal)
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(w * 0.25f, h * 0.36f),
                size = Size(w * 0.5f, h * 0.34f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.1f)
            )

            // Bright Green Eyes
            drawCircle(
                color = Color(0xFF22C55E),
                radius = w * 0.06f,
                center = Offset(w * 0.38f, h * 0.5f)
            )
            drawCircle(
                color = Color(0xFF22C55E),
                radius = w * 0.06f,
                center = Offset(w * 0.62f, h * 0.5f)
            )

            // Robot Body (White & Teal)
            drawRoundRect(
                color = Color(0xFFE2E8F0),
                topLeft = Offset(w * 0.22f, h * 0.72f),
                size = Size(w * 0.56f, h * 0.32f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.08f)
            )

            // Center Badge
            drawCircle(
                color = Color(0xFF047857),
                radius = w * 0.07f,
                center = Offset(w * 0.5f, h * 0.84f)
            )
        }
    }
}

@Composable
fun BuildingThumbnail(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFBAE6FD), Color(0xFFE0F2FE), Color(0xFFCBD5E1))
                )
            )
    ) {
        val w = size.width
        val h = size.height

        // Ground
        drawRect(
            color = Color(0xFF94A3B8),
            topLeft = Offset(0f, h * 0.85f),
            size = Size(w, h * 0.15f)
        )

        // Building columns and frame (Concrete Construction)
        val colColor = Color(0xFF64748B)
        val beamColor = Color(0xFF475569)

        // Floor slabs
        for (i in 1..4) {
            val y = h * (0.15f + i * 0.16f)
            drawRect(
                color = beamColor,
                topLeft = Offset(w * 0.15f, y),
                size = Size(w * 0.7f, h * 0.04f)
            )
        }

        // Columns
        for (i in 0..4) {
            val x = w * (0.15f + i * 0.175f)
            drawRect(
                color = colColor,
                topLeft = Offset(x, h * 0.2f),
                size = Size(w * 0.035f, h * 0.65f)
            )
        }

        // Construction Crane
        drawLine(
            color = Color(0xFFF59E0B),
            start = Offset(w * 0.82f, h * 0.4f),
            end = Offset(w * 0.82f, h * 0.08f),
            strokeWidth = 3f
        )
        drawLine(
            color = Color(0xFFF59E0B),
            start = Offset(w * 0.65f, h * 0.12f),
            end = Offset(w * 0.95f, h * 0.12f),
            strokeWidth = 3f
        )
    }
}

@Composable
fun MasonryThumbnail(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE2E8F0))
    ) {
        val w = size.width
        val h = size.height
        val brickColor = Color(0xFFB91C1C) // Brick red
        val mortarColor = Color(0xFFE2E8F0) // Gray mortar

        val rows = 5
        val rowHeight = h / rows
        val brickWidth = w / 3.5f

        for (r in 0 until rows) {
            val offset = if (r % 2 == 1) brickWidth * 0.5f else 0f
            var x = -brickWidth + offset
            while (x < w + brickWidth) {
                drawRoundRect(
                    color = brickColor,
                    topLeft = Offset(x + 2f, r * rowHeight + 2f),
                    size = Size(brickWidth - 4f, rowHeight - 4f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f)
                )
                x += brickWidth
            }
        }
    }
}

@Composable
fun ElectricianThumbnail(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
    ) {
        val w = size.width
        val h = size.height

        // Distribution Panel
        drawRoundRect(
            color = Color(0xFF334155),
            topLeft = Offset(w * 0.15f, h * 0.15f),
            size = Size(w * 0.7f, h * 0.7f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
        )

        // Colorful Wires (Blue, Red, Yellow, Green)
        val wireColors = listOf(Color(0xFF3B82F6), Color(0xFFEF4444), Color(0xFFEAB308), Color(0xFF22C55E))
        wireColors.forEachIndexed { idx, color ->
            val startY = h * (0.28f + idx * 0.12f)
            val path = Path().apply {
                moveTo(w * 0.25f, startY)
                cubicTo(w * 0.45f, startY - 10f, w * 0.55f, startY + 20f, w * 0.75f, startY)
            }
            drawPath(path, color = color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
        }
    }
}

@Composable
fun PainterThumbnail(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F5F9))
    ) {
        val w = size.width
        val h = size.height

        // Painted section
        drawRect(
            color = Color(0xFF38BDF8), // Sky blue paint
            topLeft = Offset(0f, 0f),
            size = Size(w * 0.65f, h)
        )

        // Paint roller handle
        drawLine(
            color = Color(0xFF64748B),
            start = Offset(w * 0.65f, h * 0.4f),
            end = Offset(w * 0.85f, h * 0.75f),
            strokeWidth = 6f
        )

        // Paint Roller Cylinder
        drawRoundRect(
            color = Color(0xFF0284C7),
            topLeft = Offset(w * 0.58f, h * 0.25f),
            size = Size(w * 0.14f, h * 0.35f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f)
        )
    }
}

@Composable
fun PlasterThumbnail(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF94A3B8))
    ) {
        val w = size.width
        val h = size.height

        // Smooth texture waves
        for (i in 0..4) {
            drawLine(
                color = Color(0xFFCBD5E1),
                start = Offset(0f, h * (0.2f + i * 0.15f)),
                end = Offset(w, h * (0.15f + i * 0.15f)),
                strokeWidth = 12f
            )
        }

        // Trowel Tool
        val trowelPath = Path().apply {
            moveTo(w * 0.35f, h * 0.4f)
            lineTo(w * 0.75f, h * 0.3f)
            lineTo(w * 0.65f, h * 0.7f)
            lineTo(w * 0.25f, h * 0.8f)
            close()
        }
        drawPath(trowelPath, color = Color(0xFF475569))
    }
}

@Composable
fun LaborThumbnail(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A), Color(0xFFCBD5E1))
                )
            )
    ) {
        val w = size.width
        val h = size.height

        // Ground
        drawRect(
            color = Color(0xFF78716C),
            topLeft = Offset(0f, h * 0.82f),
            size = Size(w, h * 0.18f)
        )

        // Steel rebars
        for (i in 1..6) {
            val x = w * (0.12f + i * 0.13f)
            drawLine(
                color = Color(0xFF44403C),
                start = Offset(x, h * 0.82f),
                end = Offset(x, h * 0.2f),
                strokeWidth = 3f
            )
        }

        // Worker figures silhouettes
        drawCircle(
            color = Color(0xFFEA580C),
            radius = w * 0.06f,
            center = Offset(w * 0.35f, h * 0.55f)
        )
        drawRect(
            color = Color(0xFF1E3A8A),
            topLeft = Offset(w * 0.3f, h * 0.61f),
            size = Size(w * 0.1f, h * 0.21f)
        )

        drawCircle(
            color = Color(0xFFEA580C),
            radius = w * 0.06f,
            center = Offset(w * 0.65f, h * 0.52f)
        )
        drawRect(
            color = Color(0xFF1E3A8A),
            topLeft = Offset(w * 0.6f, h * 0.58f),
            size = Size(w * 0.1f, h * 0.24f)
        )
    }
}
