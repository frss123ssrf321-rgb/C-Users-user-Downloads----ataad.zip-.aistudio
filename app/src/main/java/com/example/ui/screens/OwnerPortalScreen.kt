package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AtaadRole
import com.example.data.ContractEntity
import com.example.data.EquipmentEntity
import com.example.data.RegistrationEntity
import com.example.data.WalletTransactionEntity
import com.example.ui.components.AtaadCard
import com.example.ui.components.EquipmentPhoto
import com.example.ui.components.StatusPill
import com.example.ui.components.VerifiedBadge
import com.example.ui.components.formatSar
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BgLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardWhite
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldSurface
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOnNavy
import com.example.ui.theme.TextOnNavyMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedGreen
import com.example.ui.theme.VerifiedGreenBg
import com.example.viewmodel.AtaadUiState

@Composable
fun OwnerPortalScreen(
    uiState: AtaadUiState,
    equipmentList: List<EquipmentEntity>,
    contracts: List<ContractEntity>,
    walletTxs: List<WalletTransactionEntity>,
    registrations: List<RegistrationEntity>,
    onSelectTab: (Int) -> Unit,
    onOpenRoleSwitcher: () -> Unit,
    onAddNewEquipment: () -> Unit,
    onStartOwnerRegistration: () -> Unit,
    onUpdateEquipmentStatus: (Int, String) -> Unit,
    onAcceptContract: (ContractEntity) -> Unit,
    onRejectContract: (ContractEntity) -> Unit,
    onRequestLowbed: (ContractEntity) -> Unit,
    onWithdrawToIban: (Int) -> Unit
) {
    val ownerFleet = equipmentList.filter { it.isMyFleet }.ifEmpty { equipmentList.take(4) }
    val ownerTxs = walletTxs.filter { it.portalRole == "OWNER" }

    val tabs = listOf(
        Triple("لوحة التحكم", Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
        Triple("معداتي", Icons.Filled.Build, Icons.Outlined.Build),
        Triple("الطلبات", Icons.Filled.Assignment, Icons.Outlined.Assignment),
        Triple("المحفظة", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
        Triple("حسابي", Icons.Filled.Person, Icons.Outlined.Person)
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
                    val selected = uiState.ownerTab == index
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onSelectTab(index) },
                        modifier = Modifier.testTag("owner_tab_$index"),
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
            when (uiState.ownerTab) {
                0 -> OwnerDashboardTab(
                    fleet = ownerFleet,
                    contracts = contracts,
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onAddNewEquipment = onAddNewEquipment,
                    onGoToOrders = { onSelectTab(2) }
                )

                1 -> OwnerMyEquipmentTab(
                    fleet = ownerFleet,
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onAddNewEquipment = onAddNewEquipment,
                    onUpdateStatus = onUpdateEquipmentStatus
                )

                2 -> OwnerOrdersTab(
                    contracts = contracts,
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onAccept = onAcceptContract,
                    onReject = onRejectContract,
                    onRequestLowbed = onRequestLowbed
                )

                3 -> OwnerWalletTab(
                    transactions = ownerTxs,
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onWithdrawToIban = onWithdrawToIban
                )

                4 -> OwnerAccountTab(
                    registrations = registrations,
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onStartRegistration = onStartOwnerRegistration
                )
            }
        }
    }
}

@Composable
private fun OwnerDashboardTab(
    fleet: List<EquipmentEntity>,
    contracts: List<ContractEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onAddNewEquipment: () -> Unit,
    onGoToOrders: () -> Unit
) {
    val workingCount = fleet.count { it.status == "شغّالة" }
    val availableCount = fleet.count { it.status == "متاحة" }
    val utilization = if (fleet.isEmpty()) 0 else ((workingCount * 100) / fleet.size).coerceIn(0, 100)
    val monthlyRevenue = contracts.filter { it.status != "مرفوض" }.sumOf { it.ownerNetPayoutSar }

    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "لوحة تحكم المالك",
            subtitle = "مؤسسة رواسي نجد للمعدات الثقيلة",
            role = AtaadRole.OWNER,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Revenue Hero Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(NavyDark, NavyPrimary, NavyLight)))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("صافي الإيرادات الشهرية المتوقعة", style = MaterialTheme.typography.labelLarge, color = GoldLight)
                            Icon(Icons.Filled.TrendingUp, contentDescription = null, tint = VerifiedGreen)
                        }
                        Text(formatSar(monthlyRevenue), style = MaterialTheme.typography.displaySmall, color = TextOnNavy)
                        Text(
                            text = "صافي ربح المالك بعد خصم عمولة منصة عَتاد الثابتة (7%)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextOnNavyMuted
                        )
                    }
                }
            }

            // Fleet KPI Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OwnerKpiCard(title = "معدات شغّالة", value = "$workingCount", accent = VerifiedGreen, modifier = Modifier.weight(1f))
                    OwnerKpiCard(title = "معدات متاحة", value = "$availableCount", accent = GoldAccent, modifier = Modifier.weight(1f))
                    OwnerKpiCard(title = "نسبة التشغيل", value = "$utilization%", accent = NavyPrimary, modifier = Modifier.weight(1f))
                }
            }

            // Utilization progress bar
            item {
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("كفاءة تشغيل الأسطول هذا الشهر", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                            Text("$utilization%", style = MaterialTheme.typography.titleSmall, color = VerifiedGreen, fontWeight = FontWeight.Bold)
                        }
                        LinearProgressIndicator(
                            progress = { (utilization / 100f).coerceIn(0.1f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = GoldAccent,
                            trackColor = BgLight
                        )
                        Text(
                            text = "إجمالي أسطولك المسجل: ${fleet.size} معدات موثّقة بالاستمارة والتأمين",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Quick Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onAddNewEquipment,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("owner_add_equipment_dash_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("إضافة معدة جديدة", style = MaterialTheme.typography.labelLarge)
                    }
                    OutlinedButton(
                        onClick = onGoToOrders,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.2.dp, NavyPrimary)
                    ) {
                        Text("الطلبات الواردة (${contracts.size})", style = MaterialTheme.typography.labelLarge, color = NavyPrimary)
                    }
                }
            }
        }
    }
}

@Composable
private fun OwnerKpiCard(title: String, value: String, accent: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    AtaadCard(modifier = modifier, contentPadding = PaddingValues(12.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(value, style = MaterialTheme.typography.headlineMedium, color = accent, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(title, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}

@Composable
private fun OwnerMyEquipmentTab(
    fleet: List<EquipmentEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onAddNewEquipment: () -> Unit,
    onUpdateStatus: (Int, String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "معداتي المسجلة",
            subtitle = "إدارة حالة التشغيل وإضافة معدات جديدة للأسطول",
            role = AtaadRole.OWNER,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Button(
                    onClick = onAddNewEquipment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("owner_add_equipment_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("+ إضافة معدة جديدة إلى منصة عَتاد", style = MaterialTheme.typography.labelLarge)
                }
            }

            items(fleet, key = { it.id }) { eq ->
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            EquipmentPhoto(
                                imageKey = eq.imageKey,
                                contentDescription = eq.name,
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(eq.name, style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    StatusPill(eq.status)
                                }
                                Text("${eq.city} • موديل ${eq.year} • ${eq.capacity}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text(
                                    text = "${formatSar(eq.dailyPriceSar)} / يوم  •  ${formatSar(eq.monthlyPriceSar)} / شهر",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = NavyPrimary
                                )
                            }
                        }

                        // Quick status toggles
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("شغّالة", "متاحة", "صيانة").forEach { st ->
                                val active = eq.status == st
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onUpdateStatus(eq.id, st) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (active) NavyPrimary else BgLight,
                                    border = BorderStroke(1.dp, if (active) GoldAccent else CardBorder)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = st,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (active) GoldLight else TextSecondary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
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
private fun OwnerOrdersTab(
    contracts: List<ContractEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onAccept: (ContractEntity) -> Unit,
    onReject: (ContractEntity) -> Unit,
    onRequestLowbed: (ContractEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "الطلبات والعقود",
            subtitle = "شفافية كاملة لصافي الربح بعد عمولة منصة عَتاد (7%)",
            role = AtaadRole.OWNER,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(contracts, key = { it.id }) { order ->
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${order.contractCode} • ${order.contractType}", style = MaterialTheme.typography.labelMedium, color = GoldAccent)
                            StatusPill(order.status)
                        }
                        Text(order.equipmentName, style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
                        Text("المقاول: ${order.contractorName}", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                        Text("الموقع: ${order.siteLocation} • المدة: ${order.durationLabel}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)

                        // Transparent 7% Commission Breakdown Box
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GoldSurface,
                            border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.6f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("القيمة الإجمالية للتأجير", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                    Text(formatSar(order.baseTotalSar), style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                                }
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("عمولة منصة عَتاد (7% شفافة)", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                    Text("- ${formatSar(order.platformCommissionSar)}", style = MaterialTheme.typography.bodySmall, color = AlertRed)
                                }
                                HorizontalDivider(color = GoldAccent.copy(alpha = 0.35f))
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("صافي ربح المالك المضمون", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    Text(formatSar(order.ownerNetPayoutSar), style = MaterialTheme.typography.titleSmall, color = VerifiedGreen, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (order.status == "قيد المراجعة") {
                                Button(
                                    onClick = { onAccept(order) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen)
                                ) {
                                    Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("قبول", style = MaterialTheme.typography.labelLarge)
                                }
                                OutlinedButton(
                                    onClick = { onReject(order) },
                                    modifier = Modifier
                                        .weight(0.8f)
                                        .height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, AlertRed)
                                ) {
                                    Icon(Icons.Filled.Close, contentDescription = null, tint = AlertRed, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("رفض", color = AlertRed, style = MaterialTheme.typography.labelMedium)
                                }
                            }
                            Button(
                                onClick = { onRequestLowbed(order) },
                                modifier = Modifier
                                    .weight(1.2f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = GoldLight)
                            ) {
                                Icon(Icons.Filled.LocalShipping, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text("طلب لوبد للنقل", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OwnerWalletTab(
    transactions: List<WalletTransactionEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onWithdrawToIban: (Int) -> Unit
) {
    val escrowHeld = transactions.filter { it.category == "ضمان" }.sumOf { it.amountSar }
    val readyCredits = transactions.filter { it.category == "جاهز للتحويل" }.sumOf { it.amountSar }
    val withdrawn = transactions.filter { it.category == "تحويل بنكي" }.sumOf { it.amountSar }
    val readyBalance = (readyCredits - withdrawn).coerceAtLeast(14510)

    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "محفظة المالك",
            subtitle = "المبالغ المحجوزة بالضمان والتحويل الفوري للآيبان",
            role = AtaadRole.OWNER,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("جاهز للتحويل إلى الآيبان", style = MaterialTheme.typography.labelMedium, color = GoldLight)
                                Text(formatSar(readyBalance), style = MaterialTheme.typography.headlineMedium, color = CardWhite)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("محجوز في الضمان", style = MaterialTheme.typography.labelMedium, color = TextOnNavyMuted)
                                Text(formatSar(escrowHeld), style = MaterialTheme.typography.titleMedium, color = GoldAccent)
                            }
                        }

                        Button(
                            onClick = { onWithdrawToIban(readyBalance) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("owner_withdraw_iban_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
                        ) {
                            Text("تحويل فوري إلى الآيبان البنكي (SA...7731)", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            item {
                Text("سجل الدفعات والتحويلات", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
            }

            items(transactions, key = { it.id }) { tx ->
                WalletTransactionCard(tx)
            }
        }
    }
}

@Composable
private fun OwnerAccountTab(
    registrations: List<RegistrationEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onStartRegistration: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "حساب مالك المعدات",
            subtitle = "التوثيق عبر النفاذ الوطني الموحد (نفاذ) والآيبان",
            role = AtaadRole.OWNER,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("مؤسسة رواسي نجد للمعدات", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
                            VerifiedBadge("موثّق بنفاذ")
                        }
                        Text("الصفة: مؤسسة تأجير معدات ثقيلة", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Text("الآيبان المعتمد: SA19 1000 0023 4189 0011 7731", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Text("نسبة عمولة المنصة: 7% ثابتة وشفافة عند التعاقد فقط", style = MaterialTheme.typography.labelMedium, color = VerifiedGreen)
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
                    Text("استعراض مسار تسجيل المالك وإضافة معدة (4 خطوات)", style = MaterialTheme.typography.labelLarge)
                }
            }

            if (registrations.isNotEmpty()) {
                item {
                    Text("سجل طلبات التسجيل الحديثة", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                }
                items(registrations, key = { it.id }) { reg ->
                    AtaadCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("${reg.applicantName} (${reg.roleType})", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                            Text(reg.detailsSummary, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}
