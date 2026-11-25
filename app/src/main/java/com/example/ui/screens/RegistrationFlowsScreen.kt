package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.AtaadRole
import com.example.data.EquipmentEntity
import com.example.data.RegistrationEntity
import com.example.ui.components.AtaadCard
import com.example.ui.components.AtaadHexagonLogo
import com.example.ui.components.DocumentUploadRow
import com.example.ui.theme.BgLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardWhite
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldSurface
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.ReviewAmber
import com.example.ui.theme.ReviewAmberBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOnNavy
import com.example.ui.theme.TextOnNavyMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenBg

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegistrationFlowsScreen(
    role: AtaadRole,
    initialStep: Int = 0,
    onBack: () -> Unit,
    onCompletedBrowsePortal: (AtaadRole) -> Unit,
    onPersistSubmission: (RegistrationEntity, EquipmentEntity?) -> Unit
) {
    val totalSteps = if (role == AtaadRole.OWNER) 4 else 3
    var currentStep by remember(role, initialStep) { mutableIntStateOf(initialStep.coerceIn(0, totalSteps - 1)) }
    var isSubmitted by remember { mutableStateOf(false) }

    // Shared OTP State
    var mobileNumber by remember { mutableStateOf("0554829104") }
    var otpSent by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var otpVerified by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf("الرياض") }
    var ibanNumber by remember { mutableStateOf("SA4480000219608010447821") }

    // 1. Contractor specific state
    var contractorIsCompany by remember { mutableStateOf(true) }
    var companyName by remember { mutableStateOf("شركة ركائز المملكة للمقاولات") }
    var crNumber by remember { mutableStateOf("1010754321") }
    var vatNumber by remember { mutableStateOf("310948271600003") }
    var nationalAddress by remember { mutableStateOf("الرياض — حي الملقا — طريق أنس بن مالك") }
    var signatoryName by remember { mutableStateOf("م. طارق بن عبدالعزيز السبيعي") }
    var nationalId by remember { mutableStateOf("1084529103") }
    var officialEmail by remember { mutableStateOf("procurement@rakaiz.sa") }
    var crDocAttached by remember { mutableStateOf(false) }
    var vatDocAttached by remember { mutableStateOf(false) }

    // 2. Owner + Equipment specific state
    var ownerName by remember { mutableStateOf("مؤسسة صرح نجد لتأجير المعدات") }
    var ownerEntityType by remember { mutableStateOf("مؤسسة تأجير") }
    var nafathVerified by remember { mutableStateOf(false) }
    var eqType by remember { mutableStateOf("بوكلين") }
    var eqBrand by remember { mutableStateOf("كاتربيلر") }
    var eqModel by remember { mutableStateOf("330D2") }
    var eqYear by remember { mutableStateOf("2023") }
    var eqCapacity by remember { mutableStateOf("30 طن") }
    var eqFrontPhoto by remember { mutableStateOf(false) }
    var eqBackPhoto by remember { mutableStateOf(false) }
    var eqRightPhoto by remember { mutableStateOf(false) }
    var eqLeftPhoto by remember { mutableStateOf(false) }
    var eqSerialPhoto by remember { mutableStateOf(false) }
    var eqOwnershipDoc by remember { mutableStateOf(false) }
    var eqInsuranceDoc by remember { mutableStateOf(false) }
    var dailyRate by remember { mutableStateOf("1150") }
    var monthlyRate by remember { mutableStateOf("21500") }
    var withOperator by remember { mutableStateOf(true) }
    var acceptedCommissionTerms by remember { mutableStateOf(true) }

    // 3. Operator (Sada) specific state
    var operatorName by remember { mutableStateOf("خالد بن مسفر العتيبي") }
    var operatorId by remember { mutableStateOf("1092837465") }
    val masteredMachines = remember { mutableStateListOf("بوكلين", "شيول") }
    var yearsExp by remember { mutableStateOf("7") }
    var heavyLicenseAttached by remember { mutableStateOf(false) }
    var safetyCertAttached by remember { mutableStateOf(false) }
    var legalStatus by remember { mutableStateOf("مقيم قابل للتعاقد عبر منصة «أجير»") }
    var ajeerDocAttached by remember { mutableStateOf(false) }

    // 4. Lowbed specific state
    var lowbedName by remember { mutableStateOf("مؤسسة المسار الثقيل لنقل المعدات") }
    var lowbedIdOrCr by remember { mutableStateOf("1010884120") }
    var lowbedType by remember { mutableStateOf("لوبد هيدروليك 4 محاور") }
    var maxTonnage by remember { mutableStateOf("60") }
    val coveredCities = remember { mutableStateListOf("الرياض", "الدمام", "جدة") }
    var heavyTransportLicense by remember { mutableStateOf(false) }
    var truckRegAttached by remember { mutableStateOf(false) }
    var tgaCardAttached by remember { mutableStateOf(false) }

    BackHandler {
        if (isSubmitted) {
            onCompletedBrowsePortal(role)
        } else if (currentStep > 0) {
            currentStep--
        } else {
            onBack()
        }
    }

    val stepTitles = when (role) {
        AtaadRole.CONTRACTOR -> listOf("بيانات المنشأة", "المفوض بالتوقيع", "التوثيق والمالية")
        AtaadRole.OWNER -> listOf("بيانات المالك", "بيانات المعدة", "التوثيق والصور", "التسعير والعمولة")
        AtaadRole.OPERATOR -> listOf("البيانات الشخصية", "المؤهلات والخبرات", "الوضع النظامي والبنكي")
        AtaadRole.LOWBED -> listOf("بيانات السائق/المؤسسة", "مواصفات اللوبد", "وثائق لوجستي والآيبان")
    }

    if (isSubmitted) {
        RegistrationSubmittedScreen(
            role = role,
            onBrowseNow = { onCompletedBrowsePortal(role) }
        )
        return
    }

    Scaffold(
        containerColor = BgLight,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Surface(
                color = CardWhite,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (currentStep > 0) {
                        OutlinedButton(
                            onClick = { currentStep-- },
                            modifier = Modifier
                                .weight(0.85f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("السابق", color = NavyPrimary, style = MaterialTheme.typography.labelLarge)
                        }
                    }

                    Button(
                        onClick = {
                            if (currentStep < totalSteps - 1) {
                                currentStep++
                            } else {
                                // Persist registration record (and new Equipment if Owner!)
                                val record = when (role) {
                                    AtaadRole.CONTRACTOR -> RegistrationEntity(
                                        roleType = if (contractorIsCompany) "شركة مقاولات" else "عميل فرد",
                                        applicantName = if (contractorIsCompany) companyName else signatoryName,
                                        identifier = if (contractorIsCompany) "CR: $crNumber" else "ID: $nationalId",
                                        city = selectedCity,
                                        detailsSummary = "الرقم الضريبي: $vatNumber • الآيبان موثّق"
                                    )
                                    AtaadRole.OWNER -> RegistrationEntity(
                                        roleType = "مالك معدة ($ownerEntityType)",
                                        applicantName = ownerName,
                                        identifier = "نفاذ • $mobileNumber",
                                        city = selectedCity,
                                        detailsSummary = "$eqType $eqBrand $eqModel ($eqYear) • موافق على عمولة 7%"
                                    )
                                    AtaadRole.OPERATOR -> RegistrationEntity(
                                        roleType = "مشغّل سادة",
                                        applicantName = operatorName,
                                        identifier = operatorId,
                                        city = selectedCity,
                                        detailsSummary = "${masteredMachines.joinToString("، ")} • $yearsExp سنوات خبرة • $legalStatus"
                                    )
                                    AtaadRole.LOWBED -> RegistrationEntity(
                                        roleType = "ناقل لوبد (TGA)",
                                        applicantName = lowbedName,
                                        identifier = lowbedIdOrCr,
                                        city = coveredCities.firstOrNull() ?: "الرياض",
                                        detailsSummary = "$lowbedType • حمولة $maxTonnage طن • بطاقة تشغيل لوجستي"
                                    )
                                }

                                val addedEq = if (role == AtaadRole.OWNER) {
                                    val imgKey = when (eqType) {
                                        "شيول", "رصاصة" -> "wheel_loader"
                                        "قلاب", "مولدات" -> "dump_truck"
                                        "كرين", "لوبد" -> "crane_lowbed"
                                        else -> "excavator"
                                    }
                                    EquipmentEntity(
                                        name = "$eqType $eqBrand $eqModel",
                                        category = eqType,
                                        brand = eqBrand,
                                        model = eqModel,
                                        year = eqYear.toIntOrNull() ?: 2023,
                                        capacity = eqCapacity,
                                        city = selectedCity,
                                        district = "$selectedCity — مضاف حديثاً",
                                        dailyPriceSar = dailyRate.toIntOrNull() ?: 1150,
                                        monthlyPriceSar = monthlyRate.toIntOrNull() ?: 21500,
                                        rating = 5.0,
                                        reviewCount = 1,
                                        hasOperator = withOperator,
                                        lowbedAvailable = true,
                                        status = "متاحة",
                                        ownerName = ownerName,
                                        imageKey = imgKey,
                                        isMyFleet = true
                                    )
                                } else null

                                onPersistSubmission(record, addedEq)
                                isSubmitted = true
                            }
                        },
                        enabled = role != AtaadRole.OWNER || currentStep != 3 || acceptedCommissionTerms,
                        modifier = Modifier
                            .weight(1.4f)
                            .height(50.dp)
                            .testTag("registration_next_or_submit_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentStep == totalSteps - 1) GoldAccent else NavyPrimary,
                            contentColor = if (currentStep == totalSteps - 1) NavyPrimary else CardWhite
                        )
                    ) {
                        Text(
                            text = if (currentStep == totalSteps - 1) "إرسال للتوثيق والتفعيل" else "الخطوة التالية",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Top Navy Stepper Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(NavyDark, NavyPrimary)))
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = { if (currentStep > 0) currentStep-- else onBack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع", tint = CardWhite)
                            }
                            Column {
                                Text("تسجيل ${role.titleAr}", style = MaterialTheme.typography.titleMedium, color = TextOnNavy)
                                Text("الخطوة ${currentStep + 1} من $totalSteps: ${stepTitles[currentStep]}", style = MaterialTheme.typography.labelSmall, color = GoldLight)
                            }
                        }
                        AtaadHexagonLogo(size = 34.dp)
                    }

                    // Progress Stepper Bar
                    LinearProgressIndicator(
                        progress = { (currentStep + 1).toFloat() / totalSteps.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = GoldAccent,
                        trackColor = CardWhite.copy(alpha = 0.2f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        stepTitles.forEachIndexed { idx, title ->
                            val reached = idx <= currentStep
                            Text(
                                text = "${idx + 1}. $title",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (reached) GoldLight else TextOnNavyMuted
                            )
                        }
                    }
                }
            }

            // Form Step Body
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (role) {
                    AtaadRole.CONTRACTOR -> {
                        when (currentStep) {
                            0 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("اختر نوع حساب العميل", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        ToggleChoiceChip("حساب شركة / مؤسسة", contractorIsCompany, { contractorIsCompany = true }, Modifier.weight(1f))
                                        ToggleChoiceChip("حساب فرد", !contractorIsCompany, { contractorIsCompany = false }, Modifier.weight(1f))
                                    }
                                    if (contractorIsCompany) {
                                        AtaadTextField("اسم الشركة / المؤسسة", companyName) { companyName = it }
                                        AtaadTextField("رقم السجل التجاري (CR)", crNumber, KeyboardType.Number) { crNumber = it }
                                        AtaadTextField("الرقم الضريبي (VAT - 15 رقماً)", vatNumber, KeyboardType.Number) { vatNumber = it }
                                    } else {
                                        AtaadTextField("الاسم الكامل", signatoryName) { signatoryName = it }
                                        AtaadTextField("رقم الهوية الوطنية", nationalId, KeyboardType.Number) { nationalId = it }
                                    }
                                    CitySelectorRow(selectedCity) { selectedCity = it }
                                    AtaadTextField("العنوان الوطني", nationalAddress) { nationalAddress = it }
                                }
                            }
                            1 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("بيانات المفوض بالتوقيع والتحقق", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    AtaadTextField("الاسم الكامل للمفوض بالتوقيع", signatoryName) { signatoryName = it }
                                    AtaadTextField("رقم الهوية الوطنية", nationalId, KeyboardType.Number) { nationalId = it }
                                    OtpVerificationBlock(
                                        mobile = mobileNumber,
                                        onMobileChange = { mobileNumber = it },
                                        otpSent = otpSent,
                                        otpCode = otpCode,
                                        otpVerified = otpVerified,
                                        onSendOtp = { otpSent = true; otpCode = "4829" },
                                        onOtpChange = { otpCode = it },
                                        onVerifyOtp = { otpVerified = true }
                                    )
                                    AtaadTextField("البريد الإلكتروني الرسمي", officialEmail, KeyboardType.Email) { officialEmail = it }
                                }
                            }
                            2 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("رفع الوثائق والبيانات البنكية للفوترة (ZATCA)", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    DocumentUploadRow(
                                        title = "صورة السجل التجاري (CR)",
                                        subtitle = "مطلوب لتوثيق حساب المنشأة",
                                        isAttached = crDocAttached,
                                        onPickedUri = { if (it != null) crDocAttached = true },
                                        onQuickDemoAttach = { crDocAttached = true }
                                    )
                                    DocumentUploadRow(
                                        title = "شهادة تسجيل ضريبة القيمة المضافة (VAT)",
                                        subtitle = "لإصدار فواتير ضريبية إلكترونية متوافقة مع ZATCA",
                                        isAttached = vatDocAttached,
                                        onPickedUri = { if (it != null) vatDocAttached = true },
                                        onQuickDemoAttach = { vatDocAttached = true }
                                    )
                                    AtaadTextField("رقم الآيبان البنكي (IBAN) باسم المنشأة", ibanNumber) { ibanNumber = it }
                                }
                            }
                        }
                    }

                    AtaadRole.OWNER -> {
                        when (currentStep) {
                            0 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("بيانات مالك المعدة والتحقق عبر نفاذ", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        ToggleChoiceChip("فرد مالك معدة", ownerEntityType == "فرد مالك معدة", { ownerEntityType = "فرد مالك معدة" }, Modifier.weight(1f))
                                        ToggleChoiceChip("مؤسسة تأجير", ownerEntityType == "مؤسسة تأجير", { ownerEntityType = "مؤسسة تأجير" }, Modifier.weight(1f))
                                    }
                                    AtaadTextField("اسم المالك أو مؤسسة التأجير", ownerName) { ownerName = it }
                                    OtpVerificationBlock(
                                        mobile = mobileNumber,
                                        onMobileChange = { mobileNumber = it },
                                        otpSent = otpSent,
                                        otpCode = otpCode,
                                        otpVerified = otpVerified,
                                        onSendOtp = { otpSent = true; otpCode = "7391" },
                                        onOtpChange = { otpCode = it },
                                        onVerifyOtp = { otpVerified = true }
                                    )
                                    // Nafath unified national login verification
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = if (nafathVerified) VerifiedGreenBg else CardWhite,
                                        border = BorderStroke(1.2.dp, if (nafathVerified) VerifiedGreen else NavyPrimary)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { nafathVerified = true }
                                                .padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                                Icon(Icons.Filled.VerifiedUser, contentDescription = null, tint = if (nafathVerified) VerifiedGreen else NavyPrimary)
                                                Column {
                                                    Text("النفاذ الوطني الموحد (نفاذ)", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                                    Text(
                                                        if (nafathVerified) "تم التحقق من الهوية بنجاح عبر نفاذ (رقم 42)"
                                                        else "اضغط لتأكيد هويتك الموثّقة عبر تطبيق نفاذ",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = if (nafathVerified) VerifiedGreen else TextSecondary
                                                    )
                                                }
                                            }
                                            if (nafathVerified) {
                                                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = VerifiedGreen)
                                            }
                                        }
                                    }
                                    AtaadTextField("رقم الآيبان البنكي (IBAN) لاستلام المستحقات", ibanNumber) { ibanNumber = it }
                                }
                            }
                            1 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("بيانات المعدة الثقيلة", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    Text("نوع المعدة", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        listOf("بوكلين", "شيول", "قلاب", "بلدوزر", "كرين", "رصاصة", "مولدات").forEach { t ->
                                            SmallChip(t, eqType == t) { eqType = t }
                                        }
                                    }
                                    Text("الماركة", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        listOf("كاتربيلر", "كوماتسو", "هيتاشي", "فولفو", "مرسيدس بنز", "ساني").forEach { b ->
                                            SmallChip(b, eqBrand == b) { eqBrand = b }
                                        }
                                    }
                                    AtaadTextField("الموديل (مثال: 320D أو WA380)", eqModel) { eqModel = it }
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        AtaadTextField("سنة الصنع", eqYear, KeyboardType.Number, Modifier.weight(1f)) { eqYear = it }
                                        AtaadTextField("القدرة / الحمولة", eqCapacity, modifier = Modifier.weight(1f)) { eqCapacity = it }
                                    }
                                    CitySelectorRow(selectedCity) { selectedCity = it }
                                }
                            }
                            2 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text("صور المعدة الحقيقية ووثائق الملكية والتأمين", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    DocumentUploadRow("صورة المعدة — الواجهة الأمامية", "صورة واضحة في ضوء النهار", eqFrontPhoto, onPickedUri = { if (it != null) eqFrontPhoto = true }, onQuickDemoAttach = { eqFrontPhoto = true })
                                    DocumentUploadRow("صورة المعدة — الواجهة الخلفية", "إظهار الحالة الخارجية", eqBackPhoto, onPickedUri = { if (it != null) eqBackPhoto = true }, onQuickDemoAttach = { eqBackPhoto = true })
                                    DocumentUploadRow("صورة المعدة — الجانب الأيمن", "إظهار الذراع والهيكل", eqRightPhoto, onPickedUri = { if (it != null) eqRightPhoto = true }, onQuickDemoAttach = { eqRightPhoto = true })
                                    DocumentUploadRow("صورة المعدة — الجانب الأيسر", "إظهار الكابينة والجنزير", eqLeftPhoto, onPickedUri = { if (it != null) eqLeftPhoto = true }, onQuickDemoAttach = { eqLeftPhoto = true })
                                    DocumentUploadRow("صورة الرقم التسلسلي / اللوحة", "مطابقة الشاصي والرقم التسلسلي", eqSerialPhoto, onPickedUri = { if (it != null) eqSerialPhoto = true }, onQuickDemoAttach = { eqSerialPhoto = true })
                                    DocumentUploadRow("الاستمارة أو إثبات الملكية", "وثيقة رسمية سارية", eqOwnershipDoc, onPickedUri = { if (it != null) eqOwnershipDoc = true }, onQuickDemoAttach = { eqOwnershipDoc = true })
                                    DocumentUploadRow("وثيقة التأمين السارية", "تأمين شامل أو ضد الغير", eqInsuranceDoc, onPickedUri = { if (it != null) eqInsuranceDoc = true }, onQuickDemoAttach = { eqInsuranceDoc = true })
                                }
                            }
                            3 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("التسعير وخيارات التشغيل", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        AtaadTextField("السعر اليومي (ر.س)", dailyRate, KeyboardType.Number, Modifier.weight(1f)) { dailyRate = it }
                                        AtaadTextField("السعر الشهري (ر.س)", monthlyRate, KeyboardType.Number, Modifier.weight(1f)) { monthlyRate = it }
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        ToggleChoiceChip("المعدة مع مشغّلها", withOperator, { withOperator = true }, Modifier.weight(1f))
                                        ToggleChoiceChip("المعدة بدون مشغّل", !withOperator, { withOperator = false }, Modifier.weight(1f))
                                    }

                                    // Transparent 7% Commission Notice
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = GoldSurface,
                                        border = BorderStroke(1.5.dp, GoldAccent)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(Icons.Filled.Info, contentDescription = null, tint = NavyPrimary)
                                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text("إشعار شفافية العمولة — 7% فقط", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                                Text(
                                                    text = "عمولة منصة «عَتاد» هي 7% ثابتة تُخصم تلقائياً فقط عند إتمام التعاقد الفعلي، وتشمل حماية الدفع عبر حساب الضمان (Escrow)، التوثيق الرقمي، وإصدار الفواتير الضريبية.",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = TextPrimary
                                                )
                                            }
                                        }
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable { acceptedCommissionTerms = !acceptedCommissionTerms }
                                    ) {
                                        Checkbox(
                                            checked = acceptedCommissionTerms,
                                            onCheckedChange = { acceptedCommissionTerms = it },
                                            colors = CheckboxDefaults.colors(checkedColor = NavyPrimary)
                                        )
                                        Text(
                                            text = "أوافق على شروط الخدمة وسياسة عمولة عَتاد (7%) والالتزام بجاهزية المعدة.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    AtaadRole.OPERATOR -> {
                        when (currentStep) {
                            0 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("البيانات الشخصية للمشغّل (سادة)", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    AtaadTextField("الاسم الكامل", operatorName) { operatorName = it }
                                    AtaadTextField("رقم الهوية الوطنية أو الإقامة", operatorId, KeyboardType.Number) { operatorId = it }
                                    OtpVerificationBlock(
                                        mobile = mobileNumber,
                                        onMobileChange = { mobileNumber = it },
                                        otpSent = otpSent,
                                        otpCode = otpCode,
                                        otpVerified = otpVerified,
                                        onSendOtp = { otpSent = true; otpCode = "6150" },
                                        onOtpChange = { otpCode = it },
                                        onVerifyOtp = { otpVerified = true }
                                    )
                                    CitySelectorRow(selectedCity) { selectedCity = it }
                                }
                            }
                            1 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("المؤهلات والخبرات الميدانية", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    Text("اختر المعدات التي تجيد تشغيلها (اختيار متعدد):", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        listOf("بوكلين", "شيول", "بلدوزر", "كرين", "قلاب", "رصاصة").forEach { m ->
                                            val active = masteredMachines.contains(m)
                                            SmallChip(m, active) {
                                                if (active) masteredMachines.remove(m) else masteredMachines.add(m)
                                            }
                                        }
                                    }
                                    AtaadTextField("عدد سنوات الخبرة", yearsExp, KeyboardType.Number) { yearsExp = it }
                                    DocumentUploadRow("صورة رخصة قيادة المعدات الثقيلة (سارية)", "رخصة أشغال عامة معتمدة", heavyLicenseAttached, onPickedUri = { if (it != null) heavyLicenseAttached = true }, onQuickDemoAttach = { heavyLicenseAttached = true })
                                    DocumentUploadRow("شهادة السلامة المهنية (اختياري)", "OSHA / سلامة مواقع المشاريع", safetyCertAttached, onPickedUri = { if (it != null) safetyCertAttached = true }, onQuickDemoAttach = { safetyCertAttached = true })
                                }
                            }
                            2 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("الوضع النظامي (أجير) والبيانات البنكية", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    ToggleChoiceChip(
                                        "سعودي (تعاقد مباشر)",
                                        legalStatus == "سعودي",
                                        { legalStatus = "سعودي" },
                                        Modifier.fillMaxWidth()
                                    )
                                    ToggleChoiceChip(
                                        "مقيم قابل للتعاقد عبر منصة «أجير»",
                                        legalStatus != "سعودي",
                                        { legalStatus = "مقيم قابل للتعاقد عبر منصة «أجير»" },
                                        Modifier.fillMaxWidth()
                                    )
                                    if (legalStatus != "سعودي") {
                                        DocumentUploadRow("إثبات الكفالة أو تصريح «أجير»", "وثيقة نظامية سارية للتشغيل المؤقت", ajeerDocAttached, onPickedUri = { if (it != null) ajeerDocAttached = true }, onQuickDemoAttach = { ajeerDocAttached = true })
                                    }
                                    AtaadTextField("رقم الآيبان البنكي (IBAN)", ibanNumber) { ibanNumber = it }
                                }
                            }
                        }
                    }

                    AtaadRole.LOWBED -> {
                        when (currentStep) {
                            0 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("بيانات السائق أو مؤسسة النقل الثقيل", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    AtaadTextField("اسم السائق أو مؤسسة النقل", lowbedName) { lowbedName = it }
                                    AtaadTextField("رقم الهوية أو السجل التجاري", lowbedIdOrCr, KeyboardType.Number) { lowbedIdOrCr = it }
                                    OtpVerificationBlock(
                                        mobile = mobileNumber,
                                        onMobileChange = { mobileNumber = it },
                                        otpSent = otpSent,
                                        otpCode = otpCode,
                                        otpVerified = otpVerified,
                                        onSendOtp = { otpSent = true; otpCode = "9042" },
                                        onOtpChange = { otpCode = it },
                                        onVerifyOtp = { otpVerified = true }
                                    )
                                }
                            }
                            1 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("مواصفات اللوبد والسطحة والمدن المغطاة", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        listOf("لوبد هيدروليك 3 محاور", "لوبد هيدروليك 4 محاور", "لوبد ثقيل 5 محاور (100 طن)", "سطحة نقل معدات").forEach { t ->
                                            SmallChip(t, lowbedType == t) { lowbedType = t }
                                        }
                                    }
                                    AtaadTextField("أقصى حمولة بالطن", maxTonnage, KeyboardType.Number) { maxTonnage = it }
                                    Text("المدن المغطاة (اختيار متعدد):", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        listOf("الرياض", "جدة", "الدمام", "القصيم", "مكة المكرمة", "نيوم / تبوك").forEach { c ->
                                            val active = coveredCities.contains(c)
                                            SmallChip(c, active) {
                                                if (active) coveredCities.remove(c) else coveredCities.add(c)
                                            }
                                        }
                                    }
                                }
                            }
                            2 -> item {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("رخص النقل الثقيل وبطاقة التشغيل (TGA / لوجستي)", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    DocumentUploadRow("رخصة قيادة النقل الثقيل", "سارية المفعول", heavyTransportLicense, onPickedUri = { if (it != null) heavyTransportLicense = true }, onQuickDemoAttach = { heavyTransportLicense = true })
                                    DocumentUploadRow("استمارة الشاحنة والسطحة", "مطابقة لعدد المحاور والحمولة", truckRegAttached, onPickedUri = { if (it != null) truckRegAttached = true }, onQuickDemoAttach = { truckRegAttached = true })
                                    DocumentUploadRow("بطاقة التشغيل السارية (الهيئة العامة للنقل TGA / لوجستي)", "إلزامية للربط التلقائي بعقود النقل", tgaCardAttached, onPickedUri = { if (it != null) tgaCardAttached = true }, onQuickDemoAttach = { tgaCardAttached = true })
                                    AtaadTextField("رقم الآيبان البنكي (IBAN)", ibanNumber) { ibanNumber = it }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RegistrationSubmittedScreen(
    role: AtaadRole,
    onBrowseNow: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        AtaadCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(74.dp)
                        .clip(CircleShape)
                        .background(ReviewAmberBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.HourglassTop,
                        contentDescription = "قيد المراجعة",
                        tint = ReviewAmber,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Text(
                    text = "طلبك قيد المراجعة والتوثيق خلال 24 ساعة",
                    style = MaterialTheme.typography.headlineSmall,
                    color = NavyPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (role == AtaadRole.OWNER) {
                        "تم حفظ بياناتك وإضافة معدتك بنجاح في النموذج التفاعلي! سيقوم فريق التوثيق بمراجعة الوثائق خلال 24 ساعة، ويمكنك استعراض لوحة التحكم وسوق المعدات فوراً."
                    } else {
                        "تم استلام بيانات ووثائق «${role.titleAr}» بنجاح وحفظها في النظام. يمكنك تصفح المعدات وتجربة البوابة فوراً بينما يكتمل التوثيق."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = VerifiedGreenBg
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Filled.Security, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(16.dp))
                        Text("التوثيق الآلي مرتبط مع نفاذ وZATCA ولوجستي", style = MaterialTheme.typography.labelSmall, color = VerifiedGreen, fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = onBrowseNow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("browse_after_registration_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
                ) {
                    Text("تصفح المعدات والدخول للبوابة فوراً", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun AtaadTextField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.bodySmall) },
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
private fun ToggleChoiceChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) NavyPrimary else CardWhite,
        border = BorderStroke(1.2.dp, if (selected) GoldAccent else CardBorder)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 10.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) GoldLight else TextPrimary
            )
        }
    }
}

@Composable
private fun SmallChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) NavyPrimary else CardWhite,
        border = BorderStroke(1.dp, if (selected) GoldAccent else CardBorder)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) GoldLight else TextPrimary
        )
    }
}

@Composable
private fun CitySelectorRow(selectedCity: String, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("المدينة ونطاق العمل", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("الرياض", "جدة", "الدمام").forEach { city ->
                ToggleChoiceChip(city, selectedCity == city, { onSelect(city) }, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun OtpVerificationBlock(
    mobile: String,
    onMobileChange: (String) -> Unit,
    otpSent: Boolean,
    otpCode: String,
    otpVerified: Boolean,
    onSendOtp: () -> Unit,
    onOtpChange: (String) -> Unit,
    onVerifyOtp: () -> Unit
) {
    AtaadCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = mobile,
                    onValueChange = onMobileChange,
                    label = { Text("رقم الجوال السعودي") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Button(
                    onClick = onSendOtp,
                    modifier = Modifier.height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Text(if (otpSent) "أُرسل" else "إرسال OTP", style = MaterialTheme.typography.labelMedium)
                }
            }

            if (otpSent && !otpVerified) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = onOtpChange,
                        label = { Text("رمز التحقق (OTP)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Button(
                        onClick = onVerifyOtp,
                        modifier = Modifier.height(54.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen)
                    ) {
                        Text("تحقق", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            if (otpVerified) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(16.dp))
                    Text("تم التحقق من رقم الجوال عبر رمز OTP بنجاح", style = MaterialTheme.typography.labelSmall, color = VerifiedGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
