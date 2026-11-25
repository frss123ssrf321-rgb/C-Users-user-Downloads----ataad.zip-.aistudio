package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AtaadRole
import com.example.data.OperatorJobEntity
import com.example.data.WalletTransactionEntity
import com.example.data.WorkLogEntity
import com.example.ui.components.AtaadCard
import com.example.ui.components.DocumentUploadRow
import com.example.ui.components.StatusPill
import com.example.ui.components.VerifiedBadge
import com.example.ui.components.formatSar
import com.example.ui.components.formatTimerHms
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BgLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardWhite
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldSurface
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOnNavyMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenBg
import com.example.viewmodel.AtaadUiState

@Composable
fun OperatorPortalScreen(
    uiState: AtaadUiState,
    operatorJobs: List<OperatorJobEntity>,
    workLogs: List<WorkLogEntity>,
    walletTxs: List<WalletTransactionEntity>,
    onSelectTab: (Int) -> Unit,
    onOpenRoleSwitcher: () -> Unit,
    onStartOperatorRegistration: () -> Unit,
    onAcceptJob: (OperatorJobEntity) -> Unit,
    onToggleShiftCheckInOut: (String, Boolean) -> Unit,
    onWithdrawToIban: (Int) -> Unit
) {
    val operatorTxs = walletTxs.filter { it.portalRole == "OPERATOR" }
    val operatorLogs = workLogs.filter { it.roleTag == "OPERATOR" }

    val tabs = listOf(
        Triple("الفرص", Icons.Filled.Work, Icons.Outlined.Work),
        Triple("جدولي", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth),
        Triple("الساعات", Icons.Filled.Timer, Icons.Outlined.Timer),
        Triple("المحفظة", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
        Triple("ملفي", Icons.Filled.Person, Icons.Outlined.Person)
    )

    Scaffold(
        containerColor = BgLight,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(
                containerColor = CardWhite,
                tonalElevation = 6.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                tabs.forEachIndexed { index, (label, filled, outlined) ->
                    val selected = uiState.operatorTab == index
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onSelectTab(index) },
                        modifier = Modifier.testTag("operator_tab_$index"),
                        icon = { Icon(if (selected) filled else outlined, contentDescription = label) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall, maxLines = 1) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldAccent,
                            selectedTextColor = NavyPrimary,
                            indicatorColor = GoldSurface,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            when (uiState.operatorTab) {
                0 -> OperatorOpportunitiesTab(
                    jobs = operatorJobs.filter { it.status == "متاح" },
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onAcceptJob = onAcceptJob
                )

                1 -> OperatorScheduleTab(
                    jobs = operatorJobs.filter { it.status != "متاح" },
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onGoToWorkHours = { onSelectTab(2) }
                )

                2 -> OperatorWorkHoursTab(
                    uiState = uiState,
                    activeJob = operatorJobs.firstOrNull { it.status == "مقبول" } ?: operatorJobs.firstOrNull(),
                    logs = operatorLogs,
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onToggleShift = onToggleShiftCheckInOut
                )

                3 -> OperatorWalletTab(
                    transactions = operatorTxs,
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onWithdrawToIban = onWithdrawToIban
                )

                4 -> OperatorProfileTab(
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onStartRegistration = onStartOperatorRegistration
                )
            }
        }
    }
}

@Composable
private fun OperatorOpportunitiesTab(
    jobs: List<OperatorJobEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onAcceptJob: (OperatorJobEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "فرص التشغيل المتاحة",
            subtitle = "معدات تحتاج مشغّلاً (سادة) بالقرب من موقعك الحالي",
            role = AtaadRole.OPERATOR,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = VerifiedGreenBg,
                    border = BorderStroke(1.dp, VerifiedGreen.copy(alpha = 0.45f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.GpsFixed, contentDescription = null, tint = VerifiedGreen)
                        Text(
                            text = "تنبيه المواقع القريبة نشط • نطاق البحث 15 كم في الرياض",
                            style = MaterialTheme.typography.labelMedium,
                            color = VerifiedGreen
                        )
                    }
                }
            }

            if (jobs.isEmpty()) {
                item {
                    AtaadCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "قبلت جميع الفرص المتاحة حالياً! راجع تبويب «جدولي» لبدء الوردية.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                items(jobs, key = { it.id }) { job ->
                    AtaadCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(shape = RoundedCornerShape(8.dp), color = GoldSurface) {
                                    Text(
                                        text = "يبعد ${job.distanceKm} كم • ${job.city}",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NavyPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                StatusPill("عقد ${job.wageType}")
                            }

                            Text(job.title, style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
                            Text("${job.contractorName} • ${job.district}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text("الوردية: ${job.shiftHours} ساعات يومياً • ${job.dateRange}", style = MaterialTheme.typography.labelSmall, color = TextMuted)

                            HorizontalDivider(color = CardBorder)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${formatSar(job.wageSar)} / ${job.wageType}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = VerifiedGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text("مضمون عبر حساب الضمان", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                }

                                Button(
                                    onClick = { onAcceptJob(job) },
                                    modifier = Modifier.height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
                                ) {
                                    Text("قبول الفرصة", style = MaterialTheme.typography.labelLarge)
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
private fun OperatorScheduleTab(
    jobs: List<OperatorJobEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onGoToWorkHours: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "جدولي الميداني",
            subtitle = "عقود التشغيل المقبولة ومواعيد الورديات",
            role = AtaadRole.OPERATOR,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(jobs, key = { it.id }) { job ->
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(job.title, style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
                            StatusPill(job.status)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Filled.LocationOn, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                            Text("${job.city} — ${job.district}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                        Text("${job.contractorName} • ${job.dateRange}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Button(
                            onClick = onGoToWorkHours,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = GoldLight)
                        ) {
                            Text("فتح توثيق الدخول والخروج بالـ GPS", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OperatorWorkHoursTab(
    uiState: AtaadUiState,
    activeJob: OperatorJobEntity?,
    logs: List<WorkLogEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onToggleShift: (String, Boolean) -> Unit
) {
    var sitePhotoAttached by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "ساعات العمل والتوثيق بالـ GPS",
            subtitle = "توثيق الدخول والخروج اليومي لحماية مستحقاتك",
            role = AtaadRole.OPERATOR,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(activeJob?.title ?: "مشغّل بلدوزر D8R", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = NavyPrimary
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = formatTimerHms(uiState.operatorShiftSeconds),
                                    style = MaterialTheme.typography.displaySmall,
                                    color = GoldLight
                                )
                                Text(
                                    text = if (uiState.operatorShiftCheckedIn) "تم التحقق من الموقع: 24.8124° N, 46.6389° E"
                                    else "اضغط لتسجيل الدخول الميداني بالـ GPS",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextOnNavyMuted
                                )
                            }
                        }

                        DocumentUploadRow(
                            title = "صورة إثبات التواجد في كابينة المعدة",
                            subtitle = "مطلوبة لاعتماد الوردية الفورية",
                            isAttached = sitePhotoAttached,
                            onPickedUri = { if (it != null) sitePhotoAttached = true },
                            onQuickDemoAttach = { sitePhotoAttached = true }
                        )

                        Button(
                            onClick = {
                                onToggleShift(
                                    activeJob?.title ?: "بلدوزر D8R — وردية صباحية",
                                    sitePhotoAttached
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("operator_gps_clock_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (uiState.operatorShiftCheckedIn) AlertRed else VerifiedGreen,
                                contentColor = CardWhite
                            )
                        ) {
                            Icon(Icons.Filled.GpsFixed, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (uiState.operatorShiftCheckedIn) "توثيق الخروج وإنهاء الوردية (GPS)"
                                else "تسجيل الدخول الميداني الآن (GPS)",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }

            item {
                Text("سجل الورديات المعتمدة", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
            }
            items(logs, key = { it.id }) { log ->
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(log.siteOrEquipment, style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                            Text("${log.dateLabel} • ${log.checkInTime} - ${log.checkOutTime}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text("GPS: ${log.coordinates}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        }
                        StatusPill(log.status)
                    }
                }
            }
        }
    }
}

@Composable
private fun OperatorWalletTab(
    transactions: List<WalletTransactionEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onWithdrawToIban: (Int) -> Unit
) {
    val escrow = transactions.filter { it.category == "ضمان" }.sumOf { it.amountSar }
    val ready = (transactions.filter { it.category == "جاهز للتحويل" }.sumOf { it.amountSar } -
        transactions.filter { it.category == "تحويل بنكي" }.sumOf { it.amountSar }).coerceAtLeast(1950)

    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "محفظة المشغّل",
            subtitle = "أجور الورديات المحمية بالضمان والتحويل البنكي",
            role = AtaadRole.OPERATOR,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(NavyDark, NavyPrimary)))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("الرصيد المتاح للتحويل", style = MaterialTheme.typography.labelMedium, color = GoldLight)
                                Text(formatSar(ready), style = MaterialTheme.typography.headlineMedium, color = CardWhite)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("قيد الضمان", style = MaterialTheme.typography.labelMedium, color = TextOnNavyMuted)
                                Text(formatSar(escrow), style = MaterialTheme.typography.titleMedium, color = GoldAccent)
                            }
                        }
                        Button(
                            onClick = { onWithdrawToIban(ready) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
                        ) {
                            Text("تحويل إلى الآيبان البنكي", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
            items(transactions, key = { it.id }) { tx ->
                WalletTransactionCard(tx)
            }
        }
    }
}

@Composable
private fun OperatorProfileTab(
    onOpenRoleSwitcher: () -> Unit,
    onStartRegistration: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "ملفي المهني (سادة)",
            subtitle = "الرخصة الثقيلة، التقييم، وحالة التصريح النظامي (أجير)",
            role = AtaadRole.OPERATOR,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("سعد عبدالله القحطاني", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
                                Text("مشغّل معدات ثقيلة محترف • 9 سنوات خبرة", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                            Surface(shape = RoundedCornerShape(10.dp), color = GoldSurface) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Filled.Star, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                                    Text("4.9", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                }
                            }
                        }

                        HorizontalDivider(color = CardBorder)

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Filled.Badge, contentDescription = null, tint = VerifiedGreen)
                            Text("رخصة قيادة أشغال عامة سارية (حتى 2029)", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = VerifiedGreen)
                            Text("الوضع النظامي: مؤهّل للتعاقد عبر منصة «أجير» — ساري وموثّق", style = MaterialTheme.typography.bodySmall, color = VerifiedGreen, fontWeight = FontWeight.SemiBold)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            VerifiedBadge("بوكلين")
                            VerifiedBadge("شيول")
                            VerifiedBadge("بلدوزر")
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = onStartRegistration,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
                ) {
                    Icon(Icons.Filled.HowToReg, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("استعراض مسار تسجيل المشغّلين السادة (3 خطوات)", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
