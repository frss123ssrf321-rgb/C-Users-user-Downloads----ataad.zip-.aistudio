package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AtaadRole(val titleAr: String, val subtitleAr: String, val shortLabelAr: String) {
    CONTRACTOR(
        titleAr = "شركة مقاولات / عميل",
        subtitleAr = "حجز معدات فوري، عقود مشاريع، تتبع مباشر وحماية بالضمان",
        shortLabelAr = "المقاول"
    ),
    OWNER(
        titleAr = "مالك معدة ثقيلة",
        subtitleAr = "تشغيل أسطولك، متابعة الإيرادات وصافي الربح بعد عمولة 7%",
        shortLabelAr = "المالك"
    ),
    OPERATOR(
        titleAr = "مشغّل معدات (سادة)",
        subtitleAr = "فرص تشغيل يومية وشهرية قريبة، توثيق GPS وتصريح أجير",
        shortLabelAr = "المشغّل"
    ),
    LOWBED(
        titleAr = "ناقل لوبد (نقل معدات)",
        subtitleAr = "رحلات نقل مرتبطة بالعقود، تتبع حي وتسليم بالتوقيع الإلكتروني",
        shortLabelAr = "اللوبد"
    )
}

@Entity(tableName = "equipment")
data class EquipmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val brand: String,
    val model: String,
    val year: Int,
    val capacity: String,
    val city: String,
    val district: String,
    val dailyPriceSar: Int,
    val monthlyPriceSar: Int,
    val rating: Double,
    val reviewCount: Int,
    val hasOperator: Boolean,
    val lowbedAvailable: Boolean,
    val isVerified: Boolean = true,
    val replacement24h: Boolean = true,
    val status: String, // "متاحة" | "شغّالة" | "صيانة"
    val ownerName: String,
    val imageKey: String, // "excavator" | "wheel_loader" | "dump_truck" | "crane_lowbed"
    val isMyFleet: Boolean = false
)

@Entity(tableName = "contracts")
data class ContractEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contractCode: String,
    val equipmentId: Int,
    val equipmentName: String,
    val contractorName: String,
    val ownerName: String,
    val city: String,
    val siteLocation: String,
    val contractType: String, // "يومي" | "أسبوعي" | "عقد مشروع شهري"
    val durationLabel: String,
    val includeLowbed: Boolean,
    val lowbedCostSar: Int,
    val baseTotalSar: Int,
    val vatAmountSar: Int,
    val grandTotalSar: Int,
    val platformCommissionSar: Int,
    val ownerNetPayoutSar: Int,
    val status: String, // "قيد المراجعة" | "نشط" | "بانتظار اللوبد" | "مكتمل" | "مرفوض"
    val escrowStatus: String, // "محجوز بالضمان" | "تم التحرير"
    val emergencyActive: Boolean = false,
    val emergencyReason: String = "",
    val replacementEtaHours: Int = 24,
    val operatorAssigned: String = "فهد الدوسري (موثّق)",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "lowbed_trips")
data class LowbedTripEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripCode: String,
    val contractCode: String,
    val equipmentName: String,
    val weightTons: Int,
    val pickupLocation: String,
    val dropoffLocation: String,
    val city: String,
    val distanceKm: Int,
    val priceSar: Int,
    val stage: String, // "طلب جديد" | "في الطريق للتحميل" | "تم التحميل" | "في الطريق للموقع" | "تم التسليم"
    val pickupPhotoAttached: Boolean = false,
    val deliveryPhotoAttached: Boolean = false,
    val isSigned: Boolean = false,
    val signedByName: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "operator_jobs")
data class OperatorJobEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val equipmentType: String,
    val contractorName: String,
    val city: String,
    val district: String,
    val distanceKm: Double,
    val wageType: String, // "يومي" | "شهري"
    val wageSar: Int,
    val shiftHours: Int,
    val status: String, // "متاح" | "مقبول" | "مكتمل"
    val dateRange: String
)

@Entity(tableName = "work_logs")
data class WorkLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roleTag: String, // "CONTRACTOR" | "OPERATOR"
    val siteOrEquipment: String,
    val dateLabel: String,
    val checkInTime: String,
    val checkOutTime: String,
    val durationMinutes: Int,
    val coordinates: String,
    val photoAttached: Boolean,
    val status: String // "معتمد GPS" | "قيد التشغيل"
)

@Entity(tableName = "zatca_invoices")
data class ZatcaInvoiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val invoiceNumber: String,
    val contractCode: String,
    val clientName: String,
    val clientVatNumber: String,
    val subtotalSar: Int,
    val vat15Sar: Int,
    val totalSar: Int,
    val issueDate: String,
    val status: String, // "محمي بالضمان" | "مسددة"
    val qrPayload: String
)

@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val portalRole: String, // "CONTRACTOR" | "OWNER" | "OPERATOR" | "LOWBED"
    val title: String,
    val referenceCode: String,
    val amountSar: Int,
    val isCredit: Boolean,
    val category: String, // "ضمان" | "جاهز للتحويل" | "تحويل بنكي"
    val dateLabel: String,
    val status: String
)

@Entity(tableName = "registrations")
data class RegistrationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roleType: String,
    val applicantName: String,
    val identifier: String,
    val city: String,
    val detailsSummary: String,
    val status: String = "قيد المراجعة والتوثيق خلال 24 ساعة",
    val submittedAt: Long = System.currentTimeMillis()
)
