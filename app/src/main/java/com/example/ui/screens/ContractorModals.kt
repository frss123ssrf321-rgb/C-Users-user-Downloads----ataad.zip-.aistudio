package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EquipmentEntity
import com.example.data.ZatcaInvoiceEntity
import com.example.ui.components.EquipmentPhoto
import com.example.ui.components.Replacement24hBadge
import com.example.ui.components.StatusPill
import com.example.ui.components.VerifiedBadge
import com.example.ui.components.ZatcaQrCodeView
import com.example.ui.components.formatSar
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BgLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardWhite
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldSurface
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenBg
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentDetailBookingSheet(
    equipment: EquipmentEntity,
    onDismiss: () -> Unit,
    onConfirmBooking: (contractType: String, units: Int, includeLowbed: Boolean, siteLocation: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedType by remember { mutableStateOf("عقد مشروع شهري") }
    var units by remember { mutableIntStateOf(1) }
    var includeLowbed by remember { mutableStateOf(equipment.lowbedAvailable) }
    var siteLocation by remember { mutableStateOf("${equipment.city} — موقع مشروع البنية التحتية") }

    val baseCost = when (selectedType) {
        "يومي" -> equipment.dailyPriceSar * units
        "أسبوعي" -> (equipment.dailyPriceSar * 6) * units
        else -> equipment.monthlyPriceSar * units
    }
    val lowbedCost = if (includeLowbed) 1250 else 0
    val subtotal = baseCost + lowbedCost
    val vat = (subtotal * 0.15).roundToInt()
    val grandTotal = subtotal + vat

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CardWhite,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Photo + badges overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                EquipmentPhoto(
                    imageKey = equipment.imageKey,
                    contentDescription = equipment.name,
                    modifier = Modifier.matchParentSize()
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VerifiedBadge("موثّق من عَتاد")
                    Replacement24hBadge("ضمان بديل خلال 24 ساعة")
                }
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = NavyPrimary.copy(alpha = 0.88f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(15.dp))
                        Text(
                            text = "${equipment.rating} (${equipment.reviewCount} تقييم)",
                            style = MaterialTheme.typography.labelSmall,
                            color = CardWhite
                        )
                    }
                }
            }

            // Title & specs
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "${equipment.name} — موديل ${equipment.year}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = NavyPrimary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                    Text(
                        text = "${equipment.district} • ${equipment.ownerName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            // Key specification chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpecMiniBox(label = "الماركة والموديل", value = "${equipment.brand} ${equipment.model}", modifier = Modifier.weight(1f))
                SpecMiniBox(label = "القدرة / الحمولة", value = equipment.capacity, modifier = Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpecMiniBox(label = "السعر اليومي", value = formatSar(equipment.dailyPriceSar), modifier = Modifier.weight(1f))
                SpecMiniBox(label = "السعر الشهري", value = formatSar(equipment.monthlyPriceSar), modifier = Modifier.weight(1f))
                SpecMiniBox(
                    label = "حالة المشغّل",
                    value = if (equipment.hasOperator) "مع مشغّل" else "بدون مشغّل",
                    modifier = Modifier.weight(1f)
                )
            }

            // Contract Duration Type Selector
            Text(
                text = "اختر نوع التعاقد والمدة",
                style = MaterialTheme.typography.titleSmall,
                color = NavyPrimary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("يومي", "أسبوعي", "عقد مشروع شهري").forEach { type ->
                    val selected = selectedType == type
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                selectedType = type
                                units = 1
                            },
                        shape = RoundedCornerShape(10.dp),
                        color = if (selected) NavyPrimary else BgLight,
                        border = BorderStroke(1.dp, if (selected) GoldAccent else CardBorder)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (type == "عقد مشروع شهري") "شهري" else type,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selected) GoldAccent else TextPrimary
                            )
                        }
                    }
                }
            }

            // Units stepper
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (selectedType) {
                        "يومي" -> "عدد الأيام"
                        "أسبوعي" -> "عدد الأسابيع"
                        else -> "عدد الأشهر"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { if (units > 1) units-- },
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("-") }
                    Text("$units", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
                    OutlinedButton(
                        onClick = { if (units < 12) units++ },
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("+") }
                }
            }

            // One-tap Lowbed transport option
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (includeLowbed) GoldSurface else BgLight,
                border = BorderStroke(1.dp, if (includeLowbed) GoldAccent else CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalShipping,
                        contentDescription = "نقل باللوبد",
                        tint = NavyPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "إضافة نقل باللوبد حتى باب الموقع",
                            style = MaterialTheme.typography.titleSmall,
                            color = NavyPrimary
                        )
                        Text(
                            text = "شاحنة لوبد مرخّصة من TGA (+1,250 ر.س)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                    Switch(
                        checked = includeLowbed,
                        onCheckedChange = { includeLowbed = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CardWhite,
                            checkedTrackColor = GoldAccent
                        )
                    )
                }
            }

            OutlinedTextField(
                value = siteLocation,
                onValueChange = { siteLocation = it },
                label = { Text("موقع المشروع / التسليم") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Escrow Financial Breakdown
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = BgLight,
                border = BorderStroke(1.dp, CardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PriceRow("قيمة تأجير المعدة", formatSar(baseCost))
                    if (includeLowbed) {
                        PriceRow("نقل باللوبد (توصيل للموقع)", formatSar(lowbedCost))
                    }
                    PriceRow("ضريبة القيمة المضافة (15% ZATCA)", formatSar(vat))
                    HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("الإجمالي المحمي بحساب الضمان", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                        Text(formatSar(grandTotal), style = MaterialTheme.typography.titleMedium, color = NavyPrimary, fontWeight = FontWeight.Bold)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Filled.Security, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(16.dp))
                        Text(
                            text = "لا يُحوّل المبلغ للمالك إلا بعد وصول المعدة وبدء التشغيل الفعلي",
                            style = MaterialTheme.typography.labelSmall,
                            color = VerifiedGreen
                        )
                    }
                }
            }

            Button(
                onClick = { onConfirmBooking(selectedType, units, includeLowbed, siteLocation) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("confirm_booking_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تأكيد الحجز المحمي بالضمان • ${formatSar(grandTotal)}",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SpecMiniBox(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = BgLight,
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, style = MaterialTheme.typography.labelMedium, color = TextPrimary, maxLines = 1)
        }
    }
}

@Composable
private fun PriceRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ProjectFleetTenderDialog(
    onDismiss: () -> Unit,
    onSubmit: (projectName: String, city: String, equipmentSummary: String, months: Int, budget: Int) -> Unit
) {
    var projectName by remember { mutableStateOf("تطوير البنية التحتية — حي الملقا") }
    var city by remember { mutableStateOf("الرياض") }
    var equipmentSummary by remember { mutableStateOf("2 بوكلين 320D + 1 شيول + 3 قلابات") }
    var months by remember { mutableIntStateOf(3) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardWhite,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.Build, contentDescription = null, tint = GoldAccent)
                Text("طلب عقد مشروع / أسطول كامل", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "احصل على عرض موحّد بأسعار تفضيلية وضمان استبدال خلال 24 ساعة وفاتورة ضريبية واحدة.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                OutlinedTextField(
                    value = projectName,
                    onValueChange = { projectName = it },
                    label = { Text("اسم المشروع / الموقع") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("الرياض", "جدة", "الدمام").forEach { c ->
                        val selected = city == c
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { city = c },
                            shape = RoundedCornerShape(10.dp),
                            color = if (selected) NavyPrimary else BgLight
                        ) {
                            Box(Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                Text(c, color = if (selected) GoldAccent else TextPrimary, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
                OutlinedTextField(
                    value = equipmentSummary,
                    onValueChange = { equipmentSummary = it },
                    label = { Text("المعدات المطلوبة للأسطول") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("مدة العقد (أشهر): $months أشهر", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { if (months > 1) months-- }) { Text("-") }
                        OutlinedButton(onClick = { if (months < 24) months++ }) { Text("+") }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(projectName, city, equipmentSummary, months, 68000)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
            ) {
                Text("إرسال طلب المناقصة", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء", color = TextSecondary) }
        }
    )
}

@Composable
fun BreakdownEmergencyDialog(
    equipmentName: String,
    onDismiss: () -> Unit,
    onConfirmReport: (String) -> Unit
) {
    val reasons = listOf(
        "عطل هيدروليكي في الذراع / البوم",
        "توقف مفاجئ في المحرك / ارتفاع حرارة",
        "عطل كهربائي يمنع التشغيل الآمن",
        "كسر جنزير أو إطار ميداني"
    )
    var selectedReason by remember { mutableStateOf(reasons.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardWhite,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Filled.Warning, contentDescription = null, tint = AlertRed)
                Text("بلاغ عطل وطلب بديل (24 ساعة)", style = MaterialTheme.typography.titleMedium, color = AlertRed)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "المعدة: $equipmentName\nسيقوم مركز عمليات عَتاد بإيقاف احتساب ساعات التوقف وإرسال معدة بديلة مماثلة خلال 24 ساعة كحد أقصى.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                reasons.forEach { r ->
                    val chosen = selectedReason == r
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedReason = r },
                        shape = RoundedCornerShape(10.dp),
                        color = if (chosen) GoldSurface else BgLight,
                        border = BorderStroke(1.dp, if (chosen) AlertRed else CardBorder)
                    ) {
                        Text(
                            text = r,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            fontWeight = if (chosen) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmReport(selectedReason)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = AlertRed, contentColor = CardWhite)
            ) {
                Text("تفعيل ضمان البديل فوراً")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إغلاق") }
        }
    )
}

@Composable
fun ZatcaInvoiceDetailDialog(
    invoice: ZatcaInvoiceEntity,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardWhite,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.ReceiptLong, contentDescription = null, tint = GoldAccent)
                    Text("فاتورة ضريبية إلكترونية", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "إغلاق")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = VerifiedGreenBg
                ) {
                    Text(
                        text = "متوافقة مع هيئة الزكاة والضريبة والجمارك (ZATCA - المرحلة الثانية)",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = VerifiedGreen,
                        fontWeight = FontWeight.Bold
                    )
                }

                ZatcaQrCodeView(payload = invoice.qrPayload, size = 140.dp)

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = BgLight,
                    border = BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PriceRow("رقم الفاتورة", invoice.invoiceNumber)
                        PriceRow("رقم العقد", invoice.contractCode)
                        PriceRow("العميل", invoice.clientName)
                        PriceRow("الرقم الضريبي", invoice.clientVatNumber)
                        PriceRow("تاريخ الإصدار", invoice.issueDate)
                        HorizontalDivider(color = CardBorder)
                        PriceRow("المبلغ قبل الضريبة", formatSar(invoice.subtotalSar))
                        PriceRow("ضريبة القيمة المضافة (15%)", formatSar(invoice.vat15Sar))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("الإجمالي المستحق", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                            Text(formatSar(invoice.totalSar), style = MaterialTheme.typography.titleSmall, color = NavyPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                StatusPill(invoice.status)
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = CardWhite)
            ) {
                Text("حفظ وإغلاق")
            }
        }
    )
}
