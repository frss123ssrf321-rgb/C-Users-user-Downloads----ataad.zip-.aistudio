package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AtaadRole
import com.example.ui.theme.AlertRed
import com.example.ui.theme.AlertRedBg
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardWhite
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldSurface
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.ReviewAmberBg
import com.example.ui.theme.ReviewAmberText
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOnNavy
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenBg
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun formatSar(amount: Int): String {
    val formatted = NumberFormat.getNumberInstance(Locale.US).format(amount)
    return "$formatted ر.س"
}

fun formatTimerHms(totalSeconds: Long): String {
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60
    return String.format(Locale.US, "%02d:%02d:%02d", h, m, s)
}

fun formatTimerHm(totalSeconds: Long): String {
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    return String.format(Locale.US, "%02d:%02d ساعة", h, m)
}

fun roleIcon(role: AtaadRole): ImageVector = when (role) {
    AtaadRole.CONTRACTOR -> Icons.Filled.Apartment
    AtaadRole.OWNER -> Icons.Filled.Build
    AtaadRole.OPERATOR -> Icons.Filled.Engineering
    AtaadRole.LOWBED -> Icons.Filled.LocalShipping
}

@Composable
fun AtaadHexagonLogo(
    size: Dp = 44.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f
        val rOuter = minOf(w, h) * 0.46f
        val rInner = rOuter * 0.68f
        val rCore = rOuter * 0.30f

        fun hexPath(radius: Float): Path {
            val path = Path()
            for (i in 0 until 6) {
                val angle = (PI / 3.0) * i - PI / 2.0
                val x = cx + (radius * cos(angle)).toFloat()
                val y = cy + (radius * sin(angle)).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            return path
        }

        val goldBrush = Brush.linearGradient(
            colors = listOf(GoldLight, GoldAccent, GoldDark, GoldLight),
            start = Offset(0f, 0f),
            end = Offset(w, h)
        )

        // Deep navy fill inside hexagon
        drawPath(path = hexPath(rOuter), color = NavyPrimary)
        // Outer metallic gold hexagonal bevel
        drawPath(
            path = hexPath(rOuter),
            brush = goldBrush,
            style = Stroke(width = w * 0.085f, cap = StrokeCap.Round, join = StrokeJoin.Miter)
        )
        // Inner architectural hexagon
        drawPath(
            path = hexPath(rInner),
            brush = goldBrush,
            style = Stroke(width = w * 0.045f, cap = StrokeCap.Round, join = StrokeJoin.Miter)
        )
        // Industrial structural cross-links
        for (i in 0 until 3) {
            val angle = (PI / 3.0) * (i * 2) - PI / 2.0
            val x1 = cx + (rCore * cos(angle)).toFloat()
            val y1 = cy + (rCore * sin(angle)).toFloat()
            val x2 = cx + (rInner * cos(angle)).toFloat()
            val y2 = cy + (rInner * sin(angle)).toFloat()
            drawLine(
                brush = goldBrush,
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = w * 0.055f,
                cap = StrokeCap.Round
            )
        }
        // Center gold core
        drawPath(path = hexPath(rCore), brush = goldBrush)
        drawCircle(color = NavyPrimary, radius = rCore * 0.42f, center = Offset(cx, cy))
    }
}

@Composable
fun AtaadCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    containerColor: Color = CardWhite,
    borderColor: Color = CardBorder,
    content: @Composable () -> Unit
) {
    val cardModifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier
    Card(
        modifier = cardModifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}

@Composable
fun EquipmentPhoto(
    imageKey: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val resId = when (imageKey) {
        "excavator" -> R.drawable.img_excavator_1795572993350
        "wheel_loader" -> R.drawable.img_wheel_loader_1795572991468
        "dump_truck" -> R.drawable.img_dump_truck_1795572994537
        else -> R.drawable.img_crane_lowbed_1795572993876
    }
    Image(
        painter = painterResource(id = resId),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun VerifiedBadge(text: String = "موثّق", modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = VerifiedGreenBg
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Verified,
                contentDescription = text,
                tint = VerifiedGreen,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = VerifiedGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Replacement24hBadge(text: String = "بديل 24 ساعة", modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = NavyPrimary.copy(alpha = 0.92f),
        border = BorderStroke(1.dp, GoldAccent)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Security,
                contentDescription = text,
                tint = GoldAccent,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = GoldLight,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StatusPill(status: String, modifier: Modifier = Modifier) {
    val (bg, fg) = when (status) {
        "متاحة", "نشط", "معتمد GPS", "مكتمل", "تم التسليم", "مسددة", "مقبول", "جاهز للتحويل", "تم التحويل" ->
            VerifiedGreenBg to VerifiedGreen
        "شغّالة", "قيد المراجعة", "محجوز بالضمان", "محمي بالضمان", "بانتظار اللوبد",
        "في الطريق للتحميل", "تم التحميل", "في الطريق للموقع", "طلب جديد", "قيد التشغيل", "متاح" ->
            ReviewAmberBg to ReviewAmberText
        else -> AlertRedBg to AlertRed
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = bg
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = fg,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DemoRoleSwitcherChip(
    currentRole: AtaadRole,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .testTag("demo_role_switcher_chip")
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = NavyLight,
        border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.85f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.SwapHoriz,
                contentDescription = "تبديل الدور التجريبي",
                tint = GoldAccent,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "وضع العرض: ${currentRole.shortLabelAr}",
                style = MaterialTheme.typography.labelMedium,
                color = GoldLight,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoRoleSwitcherSheet(
    currentRole: AtaadRole,
    onDismiss: () -> Unit,
    onSelectPortal: (AtaadRole) -> Unit,
    onStartRegistration: (AtaadRole) -> Unit,
    onReturnToRoleSelection: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CardWhite,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "مبدّل الأدوار السريع (للعرض التفاعلي)",
                        style = MaterialTheme.typography.titleMedium,
                        color = NavyPrimary
                    )
                    Text(
                        text = "تنقّل فورياً بين البوابات الأربعة ومسارات التسجيل الموثّقة",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
                AtaadHexagonLogo(size = 34.dp)
            }

            HorizontalDivider(color = CardBorder)

            AtaadRole.entries.forEach { role ->
                val isSelected = role == currentRole
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("switch_role_${role.name.lowercase()}"),
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) GoldSurface else CardWhite,
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) GoldAccent else CardBorder
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectPortal(role)
                                onDismiss()
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NavyPrimary else NavyPrimary.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = roleIcon(role),
                                contentDescription = role.titleAr,
                                tint = if (isSelected) GoldAccent else NavyPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = role.titleAr,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = "نشط",
                                        tint = VerifiedGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Text(
                                text = role.subtitleAr,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    onStartRegistration(role)
                                    onDismiss()
                                },
                            color = NavyPrimary.copy(alpha = 0.07f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.HowToReg,
                                    contentDescription = "تسجيل",
                                    tint = NavyPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "تسجيل",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NavyPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            TextButton(
                onClick = {
                    onReturnToRoleSelection()
                    onDismiss()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "العودة إلى شاشة اختيار نوع الحساب الرئيسية",
                    style = MaterialTheme.typography.labelLarge,
                    color = NavyPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun EmergencyGuaranteeBanner(
    isActive: Boolean,
    etaHours: Int,
    reason: String,
    onTriggerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isActive) {
        AtaadCard(
            modifier = modifier.fillMaxWidth(),
            containerColor = AlertRedBg,
            borderColor = AlertRed
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(AlertRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "حالة طوارئ نشطة",
                        tint = CardWhite
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "تم تفعيل ضمان البديل خلال 24 ساعة",
                        style = MaterialTheme.typography.titleSmall,
                        color = AlertRed
                    )
                    Text(
                        text = "السبب: $reason • الوصول المتوقع للبديل خلال $etaHours ساعة",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )
                }
            }
        }
    } else {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("emergency_breakdown_button")
                .clip(RoundedCornerShape(14.dp))
                .clickable(onClick = onTriggerClick),
            shape = RoundedCornerShape(14.dp),
            color = AlertRed
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "الإبلاغ عن عطل",
                    tint = CardWhite,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "الإبلاغ عن عطل وطلب بديل خلال 24 ساعة",
                    style = MaterialTheme.typography.labelLarge,
                    color = CardWhite,
                    fontSize = 14.sp
                )
            }
        }
    }
}
