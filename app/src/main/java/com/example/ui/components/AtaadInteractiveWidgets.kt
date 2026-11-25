package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardWhite
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOnNavy
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenBg

/**
 * Custom minimalist Riyadh/Saudi industrial-tech GPS Tracking Map rendered with Canvas.
 * Displays animated gold route line, lowbed trailer progress, active site pin, and coordinates HUD.
 */
@Composable
fun LiveTrackingMapCanvas(
    cityLabel: String,
    routeStartLabel: String,
    routeEndLabel: String,
    truckProgress: Float,
    coordinatesText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(NavyDark, NavyPrimary, NavyLight)
                )
            )
            .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Subtle architectural city grid lines (Riyadh highway / blocks aesthetic)
            val gridColor = Color.White.copy(alpha = 0.07f)
            val arterialColor = Color.White.copy(alpha = 0.14f)

            for (i in 1..6) {
                val y = h * (i / 7f)
                drawLine(gridColor, Offset(0f, y), Offset(w, y), strokeWidth = 1.2f)
            }
            for (i in 1..7) {
                val x = w * (i / 8f)
                drawLine(gridColor, Offset(x, 0f), Offset(x, h), strokeWidth = 1.2f)
            }

            // Diagonal Saudi ring road & arterial avenues
            drawLine(arterialColor, Offset(0f, h * 0.72f), Offset(w * 0.85f, h * 0.12f), strokeWidth = 4f)
            drawLine(arterialColor, Offset(w * 0.12f, h * 0.15f), Offset(w * 0.92f, h * 0.85f), strokeWidth = 3f)

            // Active route points (Pickup -> Waypoint 1 -> Waypoint 2 -> Construction Site)
            val p0 = Offset(w * 0.16f, h * 0.74f)
            val p1 = Offset(w * 0.40f, h * 0.56f)
            val p2 = Offset(w * 0.64f, h * 0.44f)
            val p3 = Offset(w * 0.82f, h * 0.28f)

            val routePath = Path().apply {
                moveTo(p0.x, p0.y)
                cubicTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y)
            }

            // Soft gold outer glow
            drawPath(
                path = routePath,
                color = GoldAccent.copy(alpha = 0.25f),
                style = Stroke(width = 16f, cap = StrokeCap.Round)
            )
            // Crisp dashed gold route line
            drawPath(
                path = routePath,
                color = GoldAccent,
                style = Stroke(
                    width = 5f,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 10f), 0f)
                )
            )

            // Pickup hub dot
            drawCircle(color = CardWhite, radius = 11f, center = p0)
            drawCircle(color = GoldAccent, radius = 6f, center = p0)

            // Active construction site pulsing halo + green pin
            drawCircle(color = VerifiedGreen.copy(alpha = 0.22f), radius = 26f, center = p3)
            drawCircle(color = VerifiedGreen, radius = 12f, center = p3)
            drawCircle(color = CardWhite, radius = 5f, center = p3)

            // Interpolate moving Lowbed Truck along cubic Bezier
            val t = truckProgress.coerceIn(0.05f, 0.95f)
            val inv = 1f - t
            val tx = inv * inv * inv * p0.x + 3 * inv * inv * t * p1.x + 3 * inv * t * t * p2.x + t * t * t * p3.x
            val ty = inv * inv * inv * p0.y + 3 * inv * inv * t * p1.y + 3 * inv * t * t * p2.y + t * t * t * p3.y
            val truckPos = Offset(tx, ty)

            drawCircle(color = GoldLight.copy(alpha = 0.30f), radius = 24f, center = truckPos)
            drawCircle(color = GoldAccent, radius = 14f, center = truckPos)
            drawCircle(color = NavyPrimary, radius = 6f, center = truckPos)
        }

        // Top-right HUD: City & GPS Live badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = NavyDark.copy(alpha = 0.85f),
                border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.GpsFixed,
                        contentDescription = "تتبع حي",
                        tint = VerifiedGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "$cityLabel • GPS مباشر",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextOnNavy
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = NavyDark.copy(alpha = 0.78f)
            ) {
                Text(
                    text = coordinatesText,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldLight
                )
            }
        }

        // Bottom map legend showing Lowbed -> Active Site
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(12.dp),
            color = NavyDark.copy(alpha = 0.88f),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalShipping,
                        contentDescription = "نقطة الانطلاق",
                        tint = GoldAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = routeStartLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextOnNavy,
                        maxLines = 1
                    )
                }
                Text("←", color = GoldAccent, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "موقع المشروع",
                        tint = VerifiedGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = routeEndLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextOnNavy,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Deterministic ZATCA Phase-2 visual QR matrix rendered crisply in Canvas.
 */
@Composable
fun ZatcaQrCodeView(
    payload: String,
    size: Dp = 132.dp,
    modifier: Modifier = Modifier
) {
    val hash = remember(payload) { payload.hashCode() }
    Surface(
        modifier = modifier.size(size),
        shape = RoundedCornerShape(12.dp),
        color = CardWhite,
        border = BorderStroke(1.5.dp, NavyPrimary)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            val grid = 15
            val cell = this.size.width / grid

            fun drawFinder(col: Int, row: Int) {
                drawRect(NavyPrimary, Offset(col * cell, row * cell), Size(cell * 5, cell * 5))
                drawRect(CardWhite, Offset((col + 1) * cell, (row + 1) * cell), Size(cell * 3, cell * 3))
                drawRect(NavyPrimary, Offset((col + 2) * cell, (row + 2) * cell), Size(cell, cell))
            }

            drawFinder(0, 0)
            drawFinder(grid - 5, 0)
            drawFinder(0, grid - 5)

            for (r in 0 until grid) {
                for (c in 0 until grid) {
                    val inFinder = (r < 6 && c < 6) || (r < 6 && c >= grid - 6) || (r >= grid - 6 && c < 6)
                    if (!inFinder) {
                        val seed = ((r * 31 + c * 17) xor hash)
                        if ((seed and 1) == 0 || (r + c) % 4 == 0) {
                            drawRect(
                                color = if (r == 7 && c == 7) GoldAccent else NavyPrimary,
                                topLeft = Offset(c * cell, r * cell),
                                size = Size(cell * 0.92f, cell * 0.92f)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Interactive Touch E-Signature Canvas Pad for Lowbed Handover Confirmation.
 */
@Composable
fun SignaturePadCanvas(
    onSignatureChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val strokes = remember { mutableStateListOf<List<Offset>>() }
    var activeStroke by remember { mutableStateOf<List<Offset>>(emptyList()) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "التوقيع الإلكتروني للاستلام والتسليم",
                style = MaterialTheme.typography.titleSmall,
                color = NavyPrimary
            )
            TextButton(
                onClick = {
                    strokes.clear()
                    activeStroke = emptyList()
                    onSignatureChanged(false)
                }
            ) {
                Icon(Icons.Filled.Clear, contentDescription = "مسح التوقيع", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("مسح", style = MaterialTheme.typography.labelMedium)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFF8FAFC))
                .border(1.5.dp, if (strokes.isNotEmpty()) VerifiedGreen else CardBorder, RoundedCornerShape(14.dp))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            activeStroke = listOf(offset)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            activeStroke = activeStroke + change.position
                        },
                        onDragEnd = {
                            if (activeStroke.size > 1) {
                                strokes.add(activeStroke)
                                onSignatureChanged(true)
                            }
                            activeStroke = emptyList()
                        }
                    )
                }
        ) {
            if (strokes.isEmpty() && activeStroke.isEmpty()) {
                Text(
                    text = "وقّع هنا بإصبعك لتأكيد استلام المعدة",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                fun drawPoints(pts: List<Offset>) {
                    if (pts.size < 2) return
                    val path = Path().apply {
                        moveTo(pts.first().x, pts.first().y)
                        pts.drop(1).forEach { lineTo(it.x, it.y) }
                    }
                    drawPath(
                        path = path,
                        color = NavyPrimary,
                        style = Stroke(width = 6f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
                strokes.forEach { drawPoints(it) }
                drawPoints(activeStroke)
            }
        }
    }
}

/**
 * Zero-permission Android Photo Picker upload row + 1-tap demo attachment toggle for smooth pitches.
 */
@Composable
fun DocumentUploadRow(
    title: String,
    subtitle: String,
    isAttached: Boolean,
    pickedUri: Uri? = null,
    onPickedUri: (Uri?) -> Unit,
    onQuickDemoAttach: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) onPickedUri(uri)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isAttached) VerifiedGreenBg.copy(alpha = 0.45f) else CardWhite,
        border = BorderStroke(1.dp, if (isAttached) VerifiedGreen else CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (pickedUri != null) {
                AsyncImage(
                    model = pickedUri,
                    contentDescription = title,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isAttached) VerifiedGreen else NavyPrimary.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isAttached) Icons.Filled.CheckCircle else Icons.Filled.AddAPhoto,
                        contentDescription = title,
                        tint = if (isAttached) CardWhite else NavyPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary
                )
                Text(
                    text = if (isAttached) "تم الإرفاق بنجاح وجاهز للتوثيق" else subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isAttached) VerifiedGreen else TextMuted
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            pickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    shape = RoundedCornerShape(8.dp),
                    color = NavyPrimary
                ) {
                    Text(
                        text = if (isAttached) "تغيير" else "رفع صورة",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = CardWhite
                    )
                }
                if (!isAttached) {
                    Text(
                        text = "إرفاق سريع للعرض",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(onClick = onQuickDemoAttach)
                    )
                }
            }
        }
    }
}
