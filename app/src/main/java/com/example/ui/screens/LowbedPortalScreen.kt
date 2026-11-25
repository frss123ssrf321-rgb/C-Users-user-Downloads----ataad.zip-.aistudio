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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.LowbedTripEntity
import com.example.data.WalletTransactionEntity
import com.example.ui.components.AtaadCard
import com.example.ui.components.DocumentUploadRow
import com.example.ui.components.LiveTrackingMapCanvas
import com.example.ui.components.SignaturePadCanvas
import com.example.ui.components.StatusPill
import com.example.ui.components.VerifiedBadge
import com.example.ui.components.formatSar
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
import com.example.viewmodel.AtaadUiState

@Composable
fun LowbedPortalScreen(
    uiState: AtaadUiState,
    trips: List<LowbedTripEntity>,
    walletTxs: List<WalletTransactionEntity>,
    onSelectTab: (Int) -> Unit,
    onOpenRoleSwitcher: () -> Unit,
    onStartLowbedRegistration: () -> Unit,
    onAdvanceTripStage: (LowbedTripEntity) -> Unit,
    onCompleteHandover: (LowbedTripEntity, Boolean, Boolean, Boolean, String) -> Unit,
    onWithdrawToIban: (Int) -> Unit
) {
    val lowbedTxs = walletTxs.filter { it.portalRole == "LOWBED" }
    val newRequests = trips.filter { it.stage == "طلب جديد" }
    val activeTrip = trips.firstOrNull { it.stage != "طلب جديد" && it.stage != "تم التسليم" }
    val historyTrips = trips.filter { it.stage == "تم التسليم" }

    val tabs = listOf(
        Triple("رحلات النقل", Icons.Filled.LocalShipping, Icons.Outlined.LocalShipping),
        Triple("الحالية", Icons.Filled.Navigation, Icons.Outlined.Navigation),
        Triple("السجل", Icons.Filled.History, Icons.Outlined.History),
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
                    val selected = uiState.lowbedTab == index
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onSelectTab(index) },
                        modifier = Modifier.testTag("lowbed_tab_$index"),
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
            when (uiState.lowbedTab) {
                0 -> LowbedRequestsTab(
                    trips = newRequests,
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onAcceptTrip = { trip ->
                        onAdvanceTripStage(trip)
                        onSelectTab(1)
                    }
                )

                1 -> LowbedCurrentTripTab(
                    uiState = uiState,
                    trip = activeTrip ?: trips.firstOrNull(),
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onAdvanceStage = onAdvanceTripStage,
                    onCompleteHandover = onCompleteHandover
                )

                2 -> LowbedHistoryTab(
                    trips = historyTrips,
                    onOpenRoleSwitcher = onOpenRoleSwitcher
                )

                3 -> LowbedWalletTab(
                    transactions = lowbedTxs,
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onWithdrawToIban = onWithdrawToIban
                )

                4 -> LowbedAccountTab(
                    onOpenRoleSwitcher = onOpenRoleSwitcher,
                    onStartRegistration = onStartLowbedRegistration
                )
            }
        }
    }
}

@Composable
private fun LowbedRequestsTab(
    trips: List<LowbedTripEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onAcceptTrip: (LowbedTripEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "رحلات نقل المعدات",
            subtitle = "طلبات نقل مرتبطة تلقائياً بعقود منصة عَتاد",
            role = AtaadRole.LOWBED,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (trips.isEmpty()) {
                item {
                    AtaadCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "لا توجد طلبات جديدة حالياً. عند حجز مقاول لمعدة بخيار اللوبد أو طلب المالك للوبد سيظهر الطلب هنا فوراً!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                items(trips, key = { it.id }) { trip ->
                    AtaadCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${trip.tripCode} • عقد ${trip.contractCode}", style = MaterialTheme.typography.labelMedium, color = GoldAccent)
                                StatusPill(trip.stage)
                            }
                            Text("${trip.equipmentName} (${trip.weightTons} طن)", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = BgLight,
                                border = BorderStroke(1.dp, CardBorder)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("نقطة التحميل: ${trip.pickupLocation}", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                                    Text("نقطة التنزيل: ${trip.dropoffLocation}", style = MaterialTheme.typography.bodySmall, color = VerifiedGreen)
                                    Text("المسافة التقديرية: ${trip.distanceKm} كم • ${trip.city}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(formatSar(trip.priceSar), style = MaterialTheme.typography.titleMedium, color = NavyPrimary, fontWeight = FontWeight.Bold)
                                Button(
                                    onClick = { onAcceptTrip(trip) },
                                    modifier = Modifier.height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyPrimary)
                                ) {
                                    Text("قبول وبدء الرحلة", style = MaterialTheme.typography.labelLarge)
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
private fun LowbedCurrentTripTab(
    uiState: AtaadUiState,
    trip: LowbedTripEntity?,
    onOpenRoleSwitcher: () -> Unit,
    onAdvanceStage: (LowbedTripEntity) -> Unit,
    onCompleteHandover: (LowbedTripEntity, Boolean, Boolean, Boolean, String) -> Unit
) {
    var pickupPhoto by remember(trip?.id) { mutableStateOf(trip?.pickupPhotoAttached ?: false) }
    var deliveryPhoto by remember(trip?.id) { mutableStateOf(trip?.deliveryPhotoAttached ?: false) }
    var hasSignature by remember(trip?.id) { mutableStateOf(trip?.isSigned ?: false) }
    var receiverName by remember(trip?.id) { mutableStateOf(trip?.signedByName?.ifBlank { "م. عبدالله الحربي — مشرف الموقع" } ?: "م. عبدالله الحربي — مشرف الموقع") }

    val stages = listOf("في الطريق للتحميل", "تم التحميل", "في الطريق للموقع", "تم التسليم")

    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "الرحلة الحالية والتسليم",
            subtitle = "تتبع حي وتوثيق الاستلام والتسليم بالصور والتوقيع",
            role = AtaadRole.LOWBED,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )

        if (trip == null) {
            Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text("لا توجد رحلة نشطة حالياً", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
            }
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                LiveTrackingMapCanvas(
                    cityLabel = trip.city,
                    routeStartLabel = trip.pickupLocation.take(18),
                    routeEndLabel = trip.dropoffLocation.take(18),
                    truckProgress = uiState.mapTruckProgress,
                    coordinatesText = "24.7136° N, 46.6753° E",
                    modifier = Modifier.height(215.dp)
                )
            }

            // Stage Progress Bar
            item {
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${trip.equipmentName} • ${trip.weightTons} طن", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                            StatusPill(trip.stage)
                        }

                        val currentIndex = stages.indexOf(trip.stage).coerceAtLeast(0)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            stages.forEachIndexed { idx, label ->
                                val reached = idx <= currentIndex
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (reached) NavyPrimary else BgLight,
                                    border = BorderStroke(1.dp, if (reached) GoldAccent else CardBorder)
                                ) {
                                    Text(
                                        text = label,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 7.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (reached) GoldLight else TextMuted,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        if (trip.stage != "تم التسليم") {
                            Button(
                                onClick = { onAdvanceStage(trip) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = GoldLight)
                            ) {
                                Icon(Icons.Filled.Route, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("تحديث للمرحلة التالية في الرحلة", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }

            // Pickup & Delivery Photo + Interactive E-Signature Pad
            item {
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("توثيق الاستلام والتسليم الرقمي", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)

                        DocumentUploadRow(
                            title = "صورة تحميل المعدة على اللوبد",
                            subtitle = "تثبيت السلاسل وحالة المعدة عند التحميل",
                            isAttached = pickupPhoto,
                            onPickedUri = { if (it != null) pickupPhoto = true },
                            onQuickDemoAttach = { pickupPhoto = true }
                        )

                        DocumentUploadRow(
                            title = "صورة تنزيل المعدة في موقع المشروع",
                            subtitle = "تأكيد الوصول السليم لموقع المقاول",
                            isAttached = deliveryPhoto,
                            onPickedUri = { if (it != null) deliveryPhoto = true },
                            onQuickDemoAttach = { deliveryPhoto = true }
                        )

                        OutlinedTextField(
                            value = receiverName,
                            onValueChange = { receiverName = it },
                            label = { Text("اسم المستلم في الموقع") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        SignaturePadCanvas(
                            onSignatureChanged = { signed -> hasSignature = signed }
                        )

                        Button(
                            onClick = {
                                onCompleteHandover(trip, pickupPhoto, deliveryPhoto, true, receiverName)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("confirm_lowbed_handover_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (hasSignature) VerifiedGreen else GoldAccent,
                                contentColor = if (hasSignature) CardWhite else NavyPrimary
                            )
                        ) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("تأكيد التسليم وتحرير الأجرة (${formatSar(trip.priceSar)})", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LowbedHistoryTab(
    trips: List<LowbedTripEntity>,
    onOpenRoleSwitcher: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "سجل رحلات النقل",
            subtitle = "الرحلات المكتملة والموثّقة بالتوقيع الإلكتروني",
            role = AtaadRole.LOWBED,
            onOpenRoleSwitcher = onOpenRoleSwitcher
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(trips, key = { it.id }) { trip ->
                AtaadCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${trip.tripCode} • ${trip.equipmentName}", style = MaterialTheme.typography.titleSmall, color = NavyPrimary)
                            StatusPill("تم التسليم")
                        }
                        Text("${trip.pickupLocation} ← ${trip.dropoffLocation}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("توقيع المستلم: ${trip.signedByName.ifBlank { "موثّق إلكترونياً" }}", style = MaterialTheme.typography.labelSmall, color = VerifiedGreen)
                            Text(formatSar(trip.priceSar), style = MaterialTheme.typography.titleSmall, color = NavyPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LowbedWalletTab(
    transactions: List<WalletTransactionEntity>,
    onOpenRoleSwitcher: () -> Unit,
    onWithdrawToIban: (Int) -> Unit
) {
    val escrow = transactions.filter { it.category == "ضمان" }.sumOf { it.amountSar }
    val ready = (transactions.filter { it.category == "جاهز للتحويل" }.sumOf { it.amountSar } -
        transactions.filter { it.category == "تحويل بنكي" }.sumOf { it.amountSar }).coerceAtLeast(1950)

    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "محفظة ناقل اللوبد",
            subtitle = "أجور رحلات النقل والتحويل الفوري للآيبان",
            role = AtaadRole.LOWBED,
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
                                Text("جاهز للتحويل", style = MaterialTheme.typography.labelMedium, color = GoldLight)
                                Text(formatSar(ready), style = MaterialTheme.typography.headlineMedium, color = CardWhite)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("رحلات قيد التنفيذ", style = MaterialTheme.typography.labelMedium, color = TextOnNavyMuted)
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
                            Text("تحويل للآيبان البنكي الآن", style = MaterialTheme.typography.labelLarge)
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
private fun LowbedAccountTab(
    onOpenRoleSwitcher: () -> Unit,
    onStartRegistration: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PortalTopHeader(
            title = "حساب ناقل اللوبد",
            subtitle = "بطاقة التشغيل السارية من الهيئة العامة للنقل (TGA / لوجستي)",
            role = AtaadRole.LOWBED,
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
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("أسطول الراجحي للنقل الثقيل", style = MaterialTheme.typography.titleMedium, color = NavyPrimary)
                            VerifiedBadge("مرخّص TGA")
                        }
                        Text("نوع اللوبد: سطحة لوبد هيدروليك 4 محاور — أقصى حمولة 60 طن", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Text("المدن المغطاة: الرياض • القصيم • الدمام • جدة", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Filled.Verified, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(16.dp))
                            Text("بطاقة تشغيل الهيئة العامة للنقل (لوجستي): سارية المفعول", style = MaterialTheme.typography.labelMedium, color = VerifiedGreen)
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
                    Text("استعراض مسار تسجيل ناقل اللوبد (3 خطوات)", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
