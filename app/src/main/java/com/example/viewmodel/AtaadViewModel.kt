package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AtaadDatabase
import com.example.data.AtaadRepository
import com.example.data.AtaadRole
import com.example.data.ContractEntity
import com.example.data.EquipmentEntity
import com.example.data.LowbedTripEntity
import com.example.data.OperatorJobEntity
import com.example.data.RegistrationEntity
import com.example.data.WalletTransactionEntity
import com.example.data.WorkLogEntity
import com.example.data.ZatcaInvoiceEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed interface AppScreen {
    data object Splash : AppScreen
    data object RoleSelection : AppScreen
    data class Portal(val role: AtaadRole) : AppScreen
    data class Registration(val role: AtaadRole, val initialStep: Int = 0) : AppScreen
}

data class AtaadUiState(
    val currentScreen: AppScreen = AppScreen.Splash,
    val previousRole: AtaadRole = AtaadRole.CONTRACTOR,
    val contractorTab: Int = 0,
    val ownerTab: Int = 0,
    val operatorTab: Int = 0,
    val lowbedTab: Int = 0,
    val selectedCity: String = "الكل",
    val selectedCategory: String = "الكل",
    val searchQuery: String = "",
    val isProjectContractMode: Boolean = false,
    val contractorMeterRunning: Boolean = true,
    val contractorMeterSeconds: Long = 6L * 3600L + 45L * 60L + 12L,
    val operatorShiftCheckedIn: Boolean = false,
    val operatorShiftSeconds: Long = 0L,
    val mapTruckProgress: Float = 0.62f,
    val unreadNotifications: Int = 3,
    val snackbarMessage: String? = null
)

class AtaadViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AtaadRepository =
        AtaadRepository(AtaadDatabase.getInstance(application).ataadDao())

    private val _uiState = MutableStateFlow(AtaadUiState())
    val uiState: StateFlow<AtaadUiState> = _uiState.asStateFlow()

    val equipment: StateFlow<List<EquipmentEntity>> = repository.equipmentFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val contracts: StateFlow<List<ContractEntity>> = repository.contractsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowbedTrips: StateFlow<List<LowbedTripEntity>> = repository.lowbedTripsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val operatorJobs: StateFlow<List<OperatorJobEntity>> = repository.operatorJobsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workLogs: StateFlow<List<WorkLogEntity>> = repository.workLogsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val invoices: StateFlow<List<ZatcaInvoiceEntity>> = repository.invoicesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val walletTransactions: StateFlow<List<WalletTransactionEntity>> = repository.walletTransactionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val registrations: StateFlow<List<RegistrationEntity>> = repository.registrationsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
        }
        // Live digital work hour counters & map lowbed animation ticker
        viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _uiState.update { state ->
                    val nextTruck = (state.mapTruckProgress + 0.012f).let { if (it > 0.94f) 0.18f else it }
                    state.copy(
                        contractorMeterSeconds = if (state.contractorMeterRunning) state.contractorMeterSeconds + 1 else state.contractorMeterSeconds,
                        operatorShiftSeconds = if (state.operatorShiftCheckedIn) state.operatorShiftSeconds + 1 else state.operatorShiftSeconds,
                        mapTruckProgress = nextTruck
                    )
                }
            }
        }
    }

    fun finishSplash() {
        _uiState.update {
            if (it.currentScreen is AppScreen.Splash) it.copy(currentScreen = AppScreen.RoleSelection) else it
        }
    }

    fun openRoleSelection() {
        _uiState.update { it.copy(currentScreen = AppScreen.RoleSelection) }
    }

    fun openPortal(role: AtaadRole, tabIndex: Int? = null) {
        _uiState.update { state ->
            state.copy(
                currentScreen = AppScreen.Portal(role),
                previousRole = role,
                contractorTab = if (role == AtaadRole.CONTRACTOR && tabIndex != null) tabIndex else state.contractorTab,
                ownerTab = if (role == AtaadRole.OWNER && tabIndex != null) tabIndex else state.ownerTab,
                operatorTab = if (role == AtaadRole.OPERATOR && tabIndex != null) tabIndex else state.operatorTab,
                lowbedTab = if (role == AtaadRole.LOWBED && tabIndex != null) tabIndex else state.lowbedTab
            )
        }
    }

    fun openRegistration(role: AtaadRole, initialStep: Int = 0) {
        _uiState.update {
            it.copy(
                currentScreen = AppScreen.Registration(role, initialStep),
                previousRole = role
            )
        }
    }

    fun setTabForRole(role: AtaadRole, index: Int) {
        _uiState.update {
            when (role) {
                AtaadRole.CONTRACTOR -> it.copy(contractorTab = index)
                AtaadRole.OWNER -> it.copy(ownerTab = index)
                AtaadRole.OPERATOR -> it.copy(operatorTab = index)
                AtaadRole.LOWBED -> it.copy(lowbedTab = index)
            }
        }
    }

    fun setCityFilter(city: String) {
        _uiState.update { it.copy(selectedCity = city) }
    }

    fun setCategoryFilter(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setProjectContractMode(enabled: Boolean) {
        _uiState.update { it.copy(isProjectContractMode = enabled) }
    }

    fun toggleContractorMeter() {
        _uiState.update { it.copy(contractorMeterRunning = !it.contractorMeterRunning) }
    }

    fun logContractorMeterReading(siteName: String, photoAttached: Boolean) {
        val mins = (_uiState.value.contractorMeterSeconds / 60L).toInt().coerceAtLeast(1)
        viewModelScope.launch {
            repository.recordWorkLog(
                roleTag = "CONTRACTOR",
                siteOrEquipment = siteName,
                durationMinutes = mins,
                coordinates = "24.7341° N, 46.5752° E",
                photoAttached = photoAttached
            )
            showMessage("تم توثيق قراءة العدّاد الرقمي وإحداثيات الموقع بنجاح")
        }
    }

    fun toggleOperatorShift(siteName: String, photoAttached: Boolean) {
        val currentlyIn = _uiState.value.operatorShiftCheckedIn
        if (!currentlyIn) {
            _uiState.update { it.copy(operatorShiftCheckedIn = true, operatorShiftSeconds = 0L) }
            showMessage("تم تسجيل الدخول بالـ GPS وبدء احتساب ساعات الوردية")
        } else {
            val mins = ((_uiState.value.operatorShiftSeconds / 60L).toInt()).coerceAtLeast(480)
            _uiState.update { it.copy(operatorShiftCheckedIn = false) }
            viewModelScope.launch {
                repository.recordWorkLog(
                    roleTag = "OPERATOR",
                    siteOrEquipment = siteName,
                    durationMinutes = mins,
                    coordinates = "24.8124° N, 46.6389° E",
                    photoAttached = photoAttached
                )
                showMessage("تم توثيق الخروج بالـ GPS وإضافة مستحقات الوردية للمحفظة")
            }
        }
    }

    fun bookEquipment(
        equipment: EquipmentEntity,
        contractType: String,
        units: Int,
        includeLowbed: Boolean,
        siteLocation: String
    ) {
        viewModelScope.launch {
            val code = repository.bookEquipment(equipment, contractType, units, includeLowbed, siteLocation)
            val lowbedSuffix = if (includeLowbed) " وإرسال طلب لوبد تلقائياً" else ""
            showMessage("تم تأكيد الحجز ($code) وحماية المبلغ بحساب الضمان$lowbedSuffix")
            _uiState.update { it.copy(contractorTab = 1) }
        }
    }

    fun submitProjectFleetTender(
        projectName: String,
        city: String,
        equipmentSummary: String,
        months: Int,
        estimatedMonthlyBudget: Int
    ) {
        viewModelScope.launch {
            val code = repository.createProjectFleetTender(
                projectName, city, equipmentSummary, months, estimatedMonthlyBudget
            )
            showMessage("تم رفع طلب عقد المشروع ($code) — يصلك عرض الأسطول خلال ساعات")
            _uiState.update { it.copy(contractorTab = 1) }
        }
    }

    fun reportBreakdownEmergency(contract: ContractEntity, reason: String) {
        viewModelScope.launch {
            repository.reportBreakdownAndRequestReplacement(contract, reason)
            showMessage("تم تفعيل ضمان عَتاد! جاري تجهيز معدة بديلة خلال 24 ساعة")
        }
    }

    fun updateEquipmentStatus(id: Int, newStatus: String) {
        viewModelScope.launch {
            repository.updateEquipmentStatus(id, newStatus)
            showMessage("تم تحديث حالة المعدة إلى: $newStatus")
        }
    }

    fun acceptContract(contract: ContractEntity) {
        viewModelScope.launch {
            repository.acceptContractAsOwner(contract)
            showMessage("تم قبول العقد ${contract.contractCode} وتحديث حالة المعدة إلى شغّالة")
        }
    }

    fun rejectContract(contract: ContractEntity) {
        viewModelScope.launch {
            repository.rejectContractAsOwner(contract)
            showMessage("تم رفض الطلب ${contract.contractCode}")
        }
    }

    fun requestLowbedForContract(contract: ContractEntity) {
        viewModelScope.launch {
            repository.requestLowbedForContract(contract)
            showMessage("تم إرسال طلب نقل باللوبد — ظهر الآن في بوابة ناقلي اللوبد")
        }
    }

    fun acceptOperatorJob(job: OperatorJobEntity) {
        viewModelScope.launch {
            repository.acceptOperatorJob(job)
            showMessage("تم قبول فرصة التشغيل وإضافتها إلى جدولك")
            _uiState.update { it.copy(operatorTab = 1) }
        }
    }

    fun advanceLowbedTrip(trip: LowbedTripEntity) {
        viewModelScope.launch {
            repository.advanceLowbedTrip(trip)
            showMessage("تم تحديث حالة رحلة اللوبد (${trip.tripCode})")
        }
    }

    fun completeLowbedHandover(
        trip: LowbedTripEntity,
        pickupPhoto: Boolean,
        deliveryPhoto: Boolean,
        signed: Boolean,
        signerName: String
    ) {
        viewModelScope.launch {
            repository.updateLowbedTripPhotosAndSign(trip, pickupPhoto, deliveryPhoto, signed, signerName)
            showMessage(
                if (signed) "تم توثيق التسليم بالتوقيع الإلكتروني وتحرير أجرة الرحلة لمحفظتك"
                else "تم حفظ صور توثيق الرحلة بنجاح"
            )
        }
    }

    fun withdrawToIban(portalRole: String, amountSar: Int, ibanSuffix: String) {
        if (amountSar <= 0) {
            showMessage("لا يوجد رصيد متاح للتحويل حالياً")
            return
        }
        viewModelScope.launch {
            repository.withdrawWalletToIban(portalRole, amountSar, ibanSuffix)
            showMessage("تم إرسال طلب تحويل ${amountSar} ر.س إلى الآيبان (...$ibanSuffix)")
        }
    }

    fun submitRegistrationAndEquipment(
        record: RegistrationEntity,
        newEquipment: EquipmentEntity? = null
    ) {
        viewModelScope.launch {
            repository.submitRegistration(record)
            if (newEquipment != null) {
                repository.addNewEquipment(newEquipment)
            }
        }
    }

    fun clearNotifications() {
        _uiState.update { it.copy(unreadNotifications = 0) }
        showMessage("تم عرض جميع الإشعارات الميدانية والمالية")
    }

    fun showMessage(msg: String) {
        _uiState.update { it.copy(snackbarMessage = msg) }
    }

    fun consumeMessage() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
