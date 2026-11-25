package com.example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AtaadRole
import com.example.ui.components.DemoRoleSwitcherSheet
import com.example.ui.screens.AtaadSplashScreen
import com.example.ui.screens.ContractorPortalScreen
import com.example.ui.screens.LowbedPortalScreen
import com.example.ui.screens.OperatorPortalScreen
import com.example.ui.screens.OwnerPortalScreen
import com.example.ui.screens.RegistrationFlowsScreen
import com.example.ui.screens.RoleSelectionScreen
import com.example.ui.theme.AtaadTheme
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.TextOnNavy
import com.example.viewmodel.AppScreen
import com.example.viewmodel.AtaadViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.WHITE, Color.WHITE)
        )
        setContent {
            AtaadTheme {
                AtaadAppRoot()
            }
        }
    }
}

@Composable
fun AtaadAppRoot(
    viewModel: AtaadViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val equipment by viewModel.equipment.collectAsStateWithLifecycle()
    val contracts by viewModel.contracts.collectAsStateWithLifecycle()
    val lowbedTrips by viewModel.lowbedTrips.collectAsStateWithLifecycle()
    val operatorJobs by viewModel.operatorJobs.collectAsStateWithLifecycle()
    val workLogs by viewModel.workLogs.collectAsStateWithLifecycle()
    val invoices by viewModel.invoices.collectAsStateWithLifecycle()
    val walletTxs by viewModel.walletTransactions.collectAsStateWithLifecycle()
    val registrations by viewModel.registrations.collectAsStateWithLifecycle()

    var showRoleSwitcherSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        val message = uiState.snackbarMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.consumeMessage()
        }
    }

    // Handle system Back cleanly when inside a Portal
    val currentScreen = uiState.currentScreen
    if (currentScreen is AppScreen.Portal) {
        BackHandler {
            viewModel.openRoleSelection()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = currentScreen,
            label = "ataad_screen_transition"
        ) { screen ->
            when (screen) {
                AppScreen.Splash -> {
                    AtaadSplashScreen(onFinished = { viewModel.finishSplash() })
                }

                AppScreen.RoleSelection -> {
                    RoleSelectionScreen(
                        onEnterPortal = { role -> viewModel.openPortal(role) },
                        onStartRegistration = { role -> viewModel.openRegistration(role, 0) }
                    )
                }

                is AppScreen.Registration -> {
                    RegistrationFlowsScreen(
                        role = screen.role,
                        initialStep = screen.initialStep,
                        onBack = { viewModel.openPortal(screen.role) },
                        onCompletedBrowsePortal = { role -> viewModel.openPortal(role, 0) },
                        onPersistSubmission = { record, newEquipment ->
                            viewModel.submitRegistrationAndEquipment(record, newEquipment)
                        }
                    )
                }

                is AppScreen.Portal -> {
                    when (screen.role) {
                        AtaadRole.CONTRACTOR -> ContractorPortalScreen(
                            uiState = uiState,
                            equipmentList = equipment,
                            contracts = contracts,
                            operatorJobs = operatorJobs,
                            workLogs = workLogs,
                            invoices = invoices,
                            walletTxs = walletTxs,
                            registrations = registrations,
                            onSelectTab = { viewModel.setTabForRole(AtaadRole.CONTRACTOR, it) },
                            onOpenRoleSwitcher = { showRoleSwitcherSheet = true },
                            onStartContractorRegistration = { viewModel.openRegistration(AtaadRole.CONTRACTOR) },
                            onCitySelected = viewModel::setCityFilter,
                            onCategorySelected = viewModel::setCategoryFilter,
                            onSearchChanged = viewModel::setSearchQuery,
                            onModeChanged = viewModel::setProjectContractMode,
                            onBookEquipment = viewModel::bookEquipment,
                            onSubmitProjectTender = viewModel::submitProjectFleetTender,
                            onToggleMeter = viewModel::toggleContractorMeter,
                            onLogMeterReading = viewModel::logContractorMeterReading,
                            onReportEmergency = viewModel::reportBreakdownEmergency,
                            onClearNotifications = viewModel::clearNotifications
                        )

                        AtaadRole.OWNER -> OwnerPortalScreen(
                            uiState = uiState,
                            equipmentList = equipment,
                            contracts = contracts,
                            walletTxs = walletTxs,
                            registrations = registrations,
                            onSelectTab = { viewModel.setTabForRole(AtaadRole.OWNER, it) },
                            onOpenRoleSwitcher = { showRoleSwitcherSheet = true },
                            onAddNewEquipment = { viewModel.openRegistration(AtaadRole.OWNER, initialStep = 1) },
                            onStartOwnerRegistration = { viewModel.openRegistration(AtaadRole.OWNER, initialStep = 0) },
                            onUpdateEquipmentStatus = viewModel::updateEquipmentStatus,
                            onAcceptContract = viewModel::acceptContract,
                            onRejectContract = viewModel::rejectContract,
                            onRequestLowbed = viewModel::requestLowbedForContract,
                            onWithdrawToIban = { amount -> viewModel.withdrawToIban("OWNER", amount, "7731") }
                        )

                        AtaadRole.OPERATOR -> OperatorPortalScreen(
                            uiState = uiState,
                            operatorJobs = operatorJobs,
                            workLogs = workLogs,
                            walletTxs = walletTxs,
                            onSelectTab = { viewModel.setTabForRole(AtaadRole.OPERATOR, it) },
                            onOpenRoleSwitcher = { showRoleSwitcherSheet = true },
                            onStartOperatorRegistration = { viewModel.openRegistration(AtaadRole.OPERATOR) },
                            onAcceptJob = viewModel::acceptOperatorJob,
                            onToggleShiftCheckInOut = viewModel::toggleOperatorShift,
                            onWithdrawToIban = { amount -> viewModel.withdrawToIban("OPERATOR", amount, "3102") }
                        )

                        AtaadRole.LOWBED -> LowbedPortalScreen(
                            uiState = uiState,
                            trips = lowbedTrips,
                            walletTxs = walletTxs,
                            onSelectTab = { viewModel.setTabForRole(AtaadRole.LOWBED, it) },
                            onOpenRoleSwitcher = { showRoleSwitcherSheet = true },
                            onStartLowbedRegistration = { viewModel.openRegistration(AtaadRole.LOWBED) },
                            onAdvanceTripStage = viewModel::advanceLowbedTrip,
                            onCompleteHandover = viewModel::completeLowbedHandover,
                            onWithdrawToIban = { amount -> viewModel.withdrawToIban("LOWBED", amount, "8977") }
                        )
                    }
                }
            }
        }

        // Global Snackbar positioned cleanly above Bottom Navigation Bar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 84.dp, start = 16.dp, end = 16.dp)
        ) { data ->
            Snackbar(
                snackbarData = data,
                shape = RoundedCornerShape(12.dp),
                containerColor = NavyPrimary,
                contentColor = TextOnNavy,
                actionColor = GoldAccent
            )
        }
    }

    if (showRoleSwitcherSheet) {
        val activeRole = (currentScreen as? AppScreen.Portal)?.role ?: uiState.previousRole
        DemoRoleSwitcherSheet(
            currentRole = activeRole,
            onDismiss = { showRoleSwitcherSheet = false },
            onSelectPortal = { role -> viewModel.openPortal(role) },
            onStartRegistration = { role -> viewModel.openRegistration(role, 0) },
            onReturnToRoleSelection = { viewModel.openRoleSelection() }
        )
    }
}
