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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AtaadRole
import com.example.data.ContractEntity
import com.example.data.EquipmentEntity
import com.example.data.OperatorJobEntity
import com.example.data.RegistrationEntity
import com.example.data.WalletTransactionEntity
import com.example.data.WorkLogEntity
import com.example.data.ZatcaInvoiceEntity
import com.example.ui.components.AtaadCard
import com.example.ui.components.AtaadHexagonLogo
import com.example.ui.components.DemoRoleSwitcherChip
import com.example.ui.components.DocumentUploadRow
import com.example.ui.components.EmergencyGuaranteeBanner
import com.example.ui.components.EquipmentPhoto
import com.example.ui.components.LiveTrackingMapCanvas
import com.example.ui.components.Replacement24hBadge
import com.example.ui.components.StatusPill
import com.example.ui.components.VerifiedBadge
import com.example.ui.components.formatSar
import com.example.ui.components.formatTimerHm
import com.example.ui.components.formatTimerHms
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

private data class PortalTab(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val contractorTabs = listOf(
    PortalTab("الرئيسية", Icons.Filled.Home, Icons.Outlined.Home),
    PortalTab("عقودي", Icons.Filled.Assignment, Icons.Outlined.Assignment),
    PortalTab("التتبع", Icons.Filled.GpsFixed, Icons.Outlined.GpsFixed),
    PortalTab("المالية", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
    PortalTab("حسابي", Icons.Filled.Person, Icons.Outlined.Person)
)

@Composable
fun ContractorPortalScreen(
    uiState: AtaadUiState,
    equipmentList: List<EquipmentEntity>,
    contracts: List<ContractEntity>,
    operatorJobs: List<OperatorJobEntity>,
    workLogs: List<WorkLogEntity>,
    invoices: List<ZatcaInvoiceEntity>,
    walletTxs: List<WalletTransactionEntity>,
    registrations: List<RegistrationEntity>,
    onSelectTab: (Int) -> Unit,
    onOpenRoleSwitcher: () -> Unit,
    onStartContractorRegistration: () -> Unit,
    onCitySelected: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onSearchChanged: (String) -> Unit,
    onModeChanged: (Boolean) -> Unit,
    onBookEquipment: (EquipmentEntity, String, Int, Boolean, String) -> Unit,
    onSubmitProjectTender: (String, String, String, Int, Int) -> Unit,
    onToggleMeter: () -> Unit,
    onLogMeterReading: (String, Boolean) -> Unit,
    onReportEmergency: (ContractEntity, String) -> Unit,
    onClearNotifications: () -> Unit
) {
    var selectedEquipmentForBooking by remember { mutableStateOf<EquipmentEntity?>(null) }
    var showProjectTenderDialog by remember { mutableStateOf(false) }
    var showEmergencyDialogFor by remember { mutableStateOf<ContractEntity?>(null) }
    var selectedInvoiceForModal by remember { mutableStateOf<ZatcaInvoiceEntity?>(null) }

    Scaffold(
        containerColor = BgLight,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar(
                containerColor = CardWhite,
                tonalElevation = 6.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                contractorTabs.forEachIndexed { index, tab ->
                    val selected = uiState.contractorTab == index
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onSelectTab(index) },
                        modifier = Modifier.testTag("contractor_tab_$index"),
                        icon = {
                            Icon(
                                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1
                            )
                        },
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
            when (uiState.contractorTab) {
                0 -> ContractorMarketplaceTab(
                    uiState = uiState,
                    equipmentList = equipmentList,
                    operatorJobs = operatorJobs,
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onCitySelected = onCitySelected,
                    onCategorySelected = onCategorySelected,
                    onSearchChanged = onSearchChanged,
                    onModeChanged = onModeChanged,
                    onEquipmentClick = { selectedEquipmentForBooking = it },
                    onOpenProjectTender = { showProjectTenderDialog = true },
                    onClearNotifications = onClearNotifications
                )

                1 -> ContractorContractsTab(
                    contracts = contracts,
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onOpenTracking = { onSelectTab(2) },
                    onOpenTender = { showProjectTenderDialog = true },
                    onTriggerEmergency = { showEmergencyDialogFor = it }
                )

                2 -> ContractorLiveTrackingTab(
                    uiState = uiState,
                    activeContract = contracts.firstOrNull { it.status == "نشط" } ?: contracts.firstOrNull(),
                    workLogs = workLogs.filter { it.roleTag == "CONTRACTOR" },
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onToggleMeter = onToggleMeter,
                    onLogMeterReading = onLogMeterReading,
                    onTriggerEmergency = { contract -> showEmergencyDialogFor = contract }
                )

                3 -> ContractorFinanceTab(
                    contracts = contracts,
                    invoices = invoices,
                    transactions = walletTxs.filter { it.portalRole == "CONTRACTOR" },
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onInvoiceClick = { selectedInvoiceForModal = it }
                )

                4 -> ContractorAccountTab(
                    registrations = registrations,
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onStartRegistration = onStartContractorRegistration
                )
            }
        }
    }

    selectedEquipmentForBooking?.let { item ->
        EquipmentDetailBookingSheet(
            equipment = item,
            onDismiss = { selectedEquipmentForBooking = null },
            onConfirmBooking = { type, units, lowbed, site ->
                onBookEquipment(item, type, units, lowbed, site)
                selectedEquipmentForBooking = null
            }
        )
    }

    if (showProjectTenderDialog) {
        ProjectFleetTenderDialog(
            onDismiss = { showProjectTenderDialog = false },
            onSubmit = onSubmitProjectTender
        )
    }

    showEmergencyDialogFor?.let { contract ->
        BreakdownEmergencyDialog(
            equipmentName = contract.equipmentName,
            onDismiss = { showEmergencyDialogFor = null },
            onConfirmReport = { reason ->
                onReportEmergency(contract, reason)
                showEmergencyDialogFor = null
            }
        )
    }

    selectedInvoiceForModal?.let { inv ->
        ZatcaInvoiceDetailDialog(
            invoice = inv,
            onDismiss = { selectedInvoiceForModal = null }
        )
    }
}

@Composable
private fun ContractorMarketplaceTab(
    uiState: AtaadUiState,
    equipmentList: List<EquipmentEntity>,
    operatorJobs: List<OperatorJobEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onCitySelected: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onSearchChanged: (String) -> Unit,
    onModeChanged: (Boolean) -> Unit,
    onEquipmentClick: (EquipmentEntity) -> Unit,
    onOpenProjectTender: () -> Unit,
    onClearNotifications: () -> Unit
) {
    var cityDropdownExpanded by remember { mutableStateOf(false) }
    val cities = listOf("الكل", "الرياض", "جدة", "الدمام")
    val categories = listOf(
        "الكل" to Icons.Filled.Construction,
        "بوكلين" to Icons.Filled.Build,
        "شيول" to Icons.Filled.Construction,
        "قلاب" to Icons.Filled.LocalShipping,
        "بلدوزر" to Icons.Filled.Build,
        "رصاصة" to Icons.Filled.Construction,
        "كرين" to Icons.Filled.Apartment,
        "مولدات" to Icons.Filled.Bolt,
        "لوبد" to Icons.Filled.LocalShipping,
        "مشغّلين سادة" to Icons.Filled.Engineering
    )

    val filteredEquipment = remember(equipmentList, uiState.selectedCity, uiState.selectedCategory, uiState.searchQuery) {
        equipmentList.filter { eq ->
            val cityMatch = uiState.selectedCity == "الكل" || eq.city == uiState.selectedCity
            val catMatch = uiState.selectedCategory == "الكل" ||
                uiState.selectedCategory == "مشغّلين سادة" ||
                eq.category == uiState.selectedCategory
            val queryMatch = uiState.searchQuery.isBlank() ||
                eq.name.contains(uiState.searchQuery, ignoreCase = true) ||
                eq.category.contains(uiState.searchQuery, ignoreCase = true) ||
                eq.city.contains(uiState.searchQuery, ignoreCase = true) ||
                eq.brand.contains(uiState.searchQuery, ignoreCase = true)
            cityMatch && catMatch && queryMatch
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Deep Navy Header + Overlapping Crisp White Search Bar
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(listOf(NavyDark, NavyPrimary))
                        )
                        .statusBarsPadding()
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 34.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Top bar: Logo + Demo Role Switcher + Notifications
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AtaadHexagonLogo(size = 38.dp)
                                Column {
                                    Text(
                                        text = "عَتاد",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TextOnNavy
                                    )
                                    Text(
                                        text = "بوابة المقاولين",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GoldLight
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                DemoRoleSwitcherChip(
                                    currentRole = AtaadRole.CONTRACTOR,
                                    onClick = onOpenRoleSwitcher
                                )
                                IconButton(
                                    onClick = onClearNotifications,
                                    modifier = Modifier.testTag("notifications_button")
                                ) {
                                    BadgedBox(
                                        badge = {
                                            if (uiState.unreadNotifications > 0) {
                                                Badge(containerColor = GoldAccent, contentColor = NavyPrimary) {
                                                    Text("${uiState.unreadNotifications}")
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Notifications,
                                            contentDescription = "الإشعارات",
                                            tint = TextOnNavy
                                        )
                                    }
                                }
                            }
                        }

                        // Greeting + City selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "مرحباً، شركة البنيان الحديث",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextOnNavy
                                )
                                Text(
                                    text = "اختر معدّاتك الموثّقة بضمان تشغيل فوري",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextOnNavyMuted
                                )
                            }

                            Box {
                                Surface(
                                    modifier = Modifier
                                        .testTag("city_selector_chip")
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { cityDropdownExpanded = true },
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.White.copy(alpha = 0.12f),
                                    border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.45f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(15.dp))
                                        Text(
                                            text = if (uiState.selectedCity == "الكل") "الرياض / كل المدن" else uiState.selectedCity,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = TextOnNavy
                                        )
                                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
                                    }
                                }
                                DropdownMenu(
                                    expanded = cityDropdownExpanded,
                                    onDismissRequest = { cityDropdownExpanded = false },
                                    containerColor = CardWhite
                                ) {
                                    cities.forEach { city ->
                                        DropdownMenuItem(
                                            text = { Text(if (city == "الكل") "جميع المدن السعودية" else city) },
                                            onClick = {
                                                onCitySelected(city)
                                                cityDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Overlapping Crisp White Search Bar
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-22).dp),
                    shape = RoundedCornerShape(14.dp),
                    color = CardWhite,
                    shadowElevation = 4.dp,
                    border = BorderStroke(1.dp, CardBorder)
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("marketplace_search_input"),
                        placeholder = {
                            Text(
                                text = "ابحث عن بوكلين، شيول، قلاب، أو لوبد...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Search, contentDescription = "بحث", tint = NavyPrimary)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = CardWhite,
                            unfocusedContainerColor = CardWhite
                        )
                    )
                }
            }
        }

        // 2. Quick Horizontal Category Chips
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-14).dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { (label, icon) ->
                    val selected = uiState.selectedCategory == label
                    Surface(
                        modifier = Modifier
                            .testTag("category_chip_$label")
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onCategorySelected(label) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (selected) NavyPrimary else CardWhite,
                        border = BorderStroke(1.dp, if (selected) GoldAccent else CardBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (selected) GoldAccent else NavyPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selected) CardWhite else TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // 3. Competitive Advantage Navy-Gold Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(listOf(NavyPrimary, NavyLight))
                    )
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Security, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(20.dp))
                        Text(
                            text = "أسطولك الافتراضي الموثّق",
                            style = MaterialTheme.typography.titleMedium,
                            color = GoldLight
                        )
                    }
                    Text(
                        text = "ضمان معدة بديلة خلال 24 ساعة في حالة العطل، ودفع محمي بالكامل عبر حساب الضمان (Escrow).",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextOnNavy
                    )
                    Button(
                        onClick = onOpenProjectTender,
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("request_project_contract_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
                    ) {
                        Text("اطلب عقد مشروع / مناقصة أسطول", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }

        // 4. Instant Order vs Project Contracts Mode Switcher
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardWhite)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ModeSegmentButton(
                    title = "الطلب الفوري (يومي/أسبوعي)",
                    selected = !uiState.isProjectContractMode,
                    onClick = { onModeChanged(false) },
                    modifier = Modifier.weight(1f)
                )
                ModeSegmentButton(
                    title = "عقود المشاريع (شهري)",
                    selected = uiState.isProjectContractMode,
                    onClick = { onModeChanged(true) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // If "مشغّلين سادة" selected, highlight verified independent operators too
        if (uiState.selectedCategory == "مشغّلين سادة") {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "مشغّلون معتمدون (سادة) متاحون فوراً",
                        style = MaterialTheme.typography.titleMedium,
                        color = NavyPrimary
                    )
                    operatorJobs.forEach { op ->
                        AtaadCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(op.title, style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                    Text("${op.city} • ${op.district} • تصريح أجير ساري", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                }
                                Text(formatSar(op.wageSar), style = MaterialTheme.typography.titleSmall, color = VerifiedGreen)
                            }
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "المعدات المتاحة (${filteredEquipment.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = NavyPrimary
                )
                Text(
                    text = if (uiState.isProjectContractMode) "الأسعار الشهرية" else "الأسعار اليومية والشهرية",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }

        // 5. Featured Heavy Equipment Cards
        items(filteredEquipment, key = { it.id }) { equipment ->
            EquipmentMarketplaceCard(
                equipment = equipment,
                showMonthlyFocus = uiState.isProjectContractMode,
                onClick = { onEquipmentClick(equipment) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun ModeSegmentButton(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) NavyPrimary else Color.Transparent
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) GoldLight else TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun EquipmentMarketplaceCard(
    equipment: EquipmentEntity,
    showMonthlyFocus: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AtaadCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("equipment_card_${equipment.id}"),
        onClick = onClick,
        contentPadding = PaddingValues(0.dp)
    ) {
        Column {
            // Crisp Heavy Machinery Photo + Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
            ) {
                EquipmentPhoto(
                    imageKey = equipment.imageKey,
                    contentDescription = equipment.name,
                    modifier = Modifier.fillMaxSize()
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (equipment.isVerified) VerifiedBadge("موثّق")
                    if (equipment.replacement24h) Replacement24hBadge("بديل 24 ساعة")
                }
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = NavyPrimary.copy(alpha = 0.88f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(14.dp))
                        Text(
                            text = "${equipment.rating}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CardWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${equipment.name} — موديل ${equipment.year}",
                    style = MaterialTheme.typography.titleMedium,
                    color = NavyPrimary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TextMuted, modifier = Modifier.size(15.dp))
                    Text(
                        text = "${equipment.city} • ${equipment.district}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Small capability tags
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MiniFeatureTag(if (equipment.hasOperator) "مع مشغّل" else "بدون مشغّل")
                    if (equipment.lowbedAvailable) MiniFeatureTag("توصيل لوبد متاح")
                    MiniFeatureTag(equipment.capacity.substringBefore(" •"))
                }

                HorizontalDivider(color = CardBorder)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (showMonthlyFocus) {
                                "${formatSar(equipment.monthlyPriceSar)} / شهرياً"
                            } else {
                                "${formatSar(equipment.monthlyPriceSar)} / شهرياً"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = NavyPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "أو ${formatSar(equipment.dailyPriceSar)} / يومياً",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }

                    Button(
                        onClick = onClick,
                        modifier = Modifier.height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldAccent,
                            contentColor = NavyPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text("التفاصيل والحجز", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniFeatureTag(label: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = BgLight,
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun ContractorContractsTab(
    contracts: List<ContractEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onOpenTracking: () -> Unit,
    onOpenTender: () -> Unit,
    onTriggerEmergency: (ContractEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "طلباتي وعقودي",
            subtitle = "العقود النشطة ومناقصات المشاريع المحمية بالضمان",
            role = AtaadRole.CONTRACTOR,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedButton(
                    onClick = onOpenTender,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.2.dp, GoldAccent)
                ) {
                    Text("+ طلب عقد مشروع جديد (أسطول شهري)", color = NavyPrimary, style = MaterialTheme.typography.labelLarge)
                }
            }

            items(contracts, key = { it.id }) { contract ->
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(contract.contractCode, style = MaterialTheme.typography.labelMedium, color = GoldAccent)
                            StatusPill(contract.status)
                        }
                        Text(contract.equipmentName, style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
                        Text("${contract.siteLocation} • ${contract.durationLabel}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MiniFeatureTag(contract.escrowStatus)
                            Text(formatSar(contract.grandTotalSar), style = MaterialTheme.typography.titleSmall, color = NavyPrimary, fontWeight = FontWeight.Bold)
                        }

                        if (contract.emergencyActive) {
                            EmergencyGuaranteeBanner(
                                isActive = true,
                                etaHours = contract.replacementEtaHours,
                                reason = contract.emergencyReason,
                                onTriggerClick = {}
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = onOpenTracking,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                                ) {
                                    Text("التتبع المباشر", style = MaterialTheme.typography.labelMedium)
                                }
                                OutlinedButton(
                                    onClick = { onTriggerEmergency(contract) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, AlertRed)
                                ) {
                                    Text("بلاغ عطل 24س", style = MaterialTheme.typography.labelMedium, color = AlertRed)
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
private fun ContractorLiveTrackingTab(
    uiState: AtaadUiState,
    activeContract: ContractEntity?,
    workLogs: List<WorkLogEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onToggleMeter: () -> Unit,
    onLogMeterReading: (String, Boolean) -> Unit,
    onTriggerEmergency: (ContractEntity) -> Unit
) {
    var fieldPhotoAttached by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "التتبع المباشر والعدّاد الرقمي",
            subtitle = "مراقبة GPS حية للمعدة ومسار اللوبد وساعات التشغيل",
            role = AtaadRole.CONTRACTOR,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Custom Riyadh Navy/Gold Tracking Map
            item {
                LiveTrackingMapCanvas(
                    cityLabel = activeContract?.city ?: "الرياض",
                    routeStartLabel = "لوبد مخرج 18",
                    routeEndLabel = "مشروع بوابة الدرعية",
                    truckProgress = uiState.mapTruckProgress,
                    coordinatesText = "24.7341° N, 46.5752° E",
                    modifier = Modifier.height(235.dp)
                )
            }

            // Floating Digital Work Hour Counter Card
            item {
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("عدّاد ساعات العمل الرقمي", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                Text(activeContract?.equipmentName ?: "بوكلين كاتربيلر 320D", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                            StatusPill(if (uiState.contractorMeterRunning) "قيد التشغيل" else "متوقف مؤقتاً")
                        }

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = NavyPrimary
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = formatTimerHm(uiState.contractorMeterSeconds),
                                    style = MaterialTheme.typography.displaySmall,
                                    color = GoldLight
                                )
                                Text(
                                    text = "التوقيت الحي: ${formatTimerHms(uiState.contractorMeterSeconds)}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TextOnNavyMuted
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                VerifiedBadge("محمي بحساب الضمان Escrow")
                            }
                        }

                        DocumentUploadRow(
                            title = "توثيق صورة ميدانية لعدّاد الساعات",
                            subtitle = "ارفق صورة لوحة الساعات في الموقع مع إحداثيات GPS",
                            isAttached = fieldPhotoAttached,
                            onPickedUri = { if (it != null) fieldPhotoAttached = true },
                            onQuickDemoAttach = { fieldPhotoAttached = true }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = onToggleMeter,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = if (uiState.contractorMeterRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (uiState.contractorMeterRunning) "إيقاف مؤقت" else "استئناف العد")
                            }

                            Button(
                                onClick = {
                                    onLogMeterReading(
                                        activeContract?.equipmentName ?: "بوكلين كاتربيلر 320D",
                                        fieldPhotoAttached
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
                            ) {
                                Text("اعتماد القراءة GPS", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Operational Emergency Button (24-Hour Replacement Guarantee)
            if (activeContract != null) {
                item {
                    EmergencyGuaranteeBanner(
                        isActive = activeContract.emergencyActive,
                        etaHours = activeContract.replacementEtaHours,
                        reason = activeContract.emergencyReason,
                        onTriggerClick = { onTriggerEmergency(activeContract) }
                    )
                }
            }

            // Daily Verified Work Logs
            item {
                Text("سجل ساعات التشغيل الموثّقة", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
            }
            items(workLogs, key = { it.id }) { log ->
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
                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            StatusPill(log.status)
                            Text("${log.durationMinutes / 60} س ${log.durationMinutes % 60} د", style = MaterialTheme.typography.labelMedium, color = NavyPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContractorFinanceTab(
    contracts: List<ContractEntity>,
    invoices: List<ZatcaInvoiceEntity>,
    transactions: List<WalletTransactionEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onInvoiceClick: (ZatcaInvoiceEntity) -> Unit
) {
    val escrowHeld = contracts.filter { it.escrowStatus == "محجوز بالضمان" }.sumOf { it.grandTotalSar }

    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "المالية وفواتير ZATCA",
            subtitle = "حساب الضمان (Escrow)، الفواتير الإلكترونية والتحويلات البنكية",
            role = AtaadRole.CONTRACTOR,
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
                        .background(Brush.linearGradient(listOf(NavyDark, NavyPrimary, NavyLight)))
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Filled.Security, contentDescription = null, tint = GoldAccent)
                            Text("رصيد حساب الضمان المحمي (Escrow)", style = MaterialTheme.typography.labelLarge, color = GoldLight)
                        }
                        Text(formatSar(escrowHeld), style = MaterialTheme.typography.displaySmall, color = CardWhite)
                        Text(
                            text = "محجوز لصالح العقود الجارية • يُصرف للمالك فقط عند إثبات التشغيل الفعلي",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextOnNavyMuted
                        )
                    }
                }
            }

            item {
                Text("الفواتير الضريبية الإلكترونية (ZATCA QR)", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
            }

            items(invoices, key = { it.id }) { inv ->
                AtaadCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onInvoiceClick(inv) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(GoldSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.QrCode2, contentDescription = "رمز QR", tint = NavyPrimary)
                            }
                            Column {
                                Text(inv.invoiceNumber, style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                Text("عقد ${inv.contractCode} • ${inv.issueDate}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text("شامل ضريبة 15% (${formatSar(inv.vat15Sar)})", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(formatSar(inv.totalSar), style = MaterialTheme.typography.titleSmall, color = NavyPrimary, fontWeight = FontWeight.Bold)
                            StatusPill(inv.status)
                        }
                    }
                }
            }

            item {
                Text("سجل التحويلات البنكية", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
            }

            items(transactions, key = { it.id }) { tx ->
                WalletTransactionCard(tx)
            }
        }
    }
}

@Composable
private fun ContractorAccountTab(
    registrations: List<RegistrationEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onStartRegistration: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "حساب المنشأة",
            subtitle = "بيانات شركة المقاولات والتوثيق النظامي",
            role = AtaadRole.CONTRACTOR,
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
                            Text("شركة البنيان الحديث للمقاولات", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
                            VerifiedBadge("منشأة موثّقة")
                        }
                        Text("السجل التجاري (CR): 1010648921", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Text("الرقم الضريبي (VAT): 310482917400003", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Text("الآيبان البنكي: SA44 8000 0219 6080 1044 7821", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Text("العنوان الوطني: الرياض — طريق الملك فهد — حي العليا", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                }
            }

            item {
                Button(
                    onClick = onStartRegistration,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("launch_contractor_reg_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
                ) {
                    Icon(Icons.Filled.HowToReg, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("استعراض مسار تسجيل وتوثيق الشركات / الأفراد", style = MaterialTheme.typography.labelLarge)
                }
            }

            if (registrations.isNotEmpty()) {
                item {
                    Text("طلبات التوثيق المرسلة في هذا الجهاز", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                }
                items(registrations, key = { it.id }) { reg ->
                    AtaadCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(reg.applicantName, style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                                StatusPill("قيد المراجعة")
                            }
                            Text("${reg.roleType} • ${reg.city} • ${reg.identifier}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text(reg.detailsSummary, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PortalTopHeader(
    title: String,
    subtitle: String,
    role: AtaadRole,
    onOpenRoleSwitcher: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(NavyDark, NavyPrimary)))
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                AtaadHexagonLogo(size = 34.dp)
                Column {
                    Text(title, style = MaterialTheme.typography.titleMedium, color = TextOnNavy, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(subtitle, style = MaterialTheme.typography.labelSmall, color = TextOnNavyMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            DemoRoleSwitcherChip(
                currentRole = role,
                onClick = onOpenRoleSwitcher
            )
        }
    }
}

@Composable
fun WalletTransactionCard(tx: WalletTransactionEntity) {
    AtaadCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(tx.title, style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                Text("${tx.referenceCode} • ${tx.dateLabel}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = (if (tx.isCredit) "+ " else "- ") + formatSar(tx.amountSar),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (tx.isCredit) VerifiedGreen else NavyPrimary,
                    fontWeight = FontWeight.Bold
                )
                StatusPill(tx.status)
            }
        }
    }
}
