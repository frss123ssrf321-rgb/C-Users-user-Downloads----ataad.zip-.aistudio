package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        EquipmentEntity::class,
        ContractEntity::class,
        LowbedTripEntity::class,
        OperatorJobEntity::class,
        WorkLogEntity::class,
        ZatcaInvoiceEntity::class,
        WalletTransactionEntity::class,
        RegistrationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AtaadDatabase : RoomDatabase() {
    abstract fun ataadDao(): AtaadDao

    companion object {
        @Volatile
        private var INSTANCE: AtaadDatabase? = null

        fun getInstance(context: Context): AtaadDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AtaadDatabase::class.java,
                    "ataad_saudi_fleet_v1.db"
                ).fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

object AtaadSaudiSeedData {

    fun initialEquipment(): List<EquipmentEntity> = listOf(
        EquipmentEntity(
            name = "بوكلين كاتربيلر 320D",
            category = "بوكلين",
            brand = "كاتربيلر",
            model = "320D",
            year = 2022,
            capacity = "22 طن • حفار هيدروليك",
            city = "الرياض",
            district = "شرق الرياض — حي القادسية",
            dailyPriceSar = 950,
            monthlyPriceSar = 17500,
            rating = 4.9,
            reviewCount = 48,
            hasOperator = true,
            lowbedAvailable = true,
            status = "متاحة",
            ownerName = "مؤسسة رواسي نجد للمعدات",
            imageKey = "excavator",
            isMyFleet = true
        ),
        EquipmentEntity(
            name = "شيول كوماتسو WA380-8",
            category = "شيول",
            brand = "كوماتسو",
            model = "WA380-8",
            year = 2023,
            capacity = "3.5 م³ • حمولة 18 طن",
            city = "الرياض",
            district = "شمال الرياض — طريق الملك سلمان",
            dailyPriceSar = 850,
            monthlyPriceSar = 15800,
            rating = 4.8,
            reviewCount = 34,
            hasOperator = true,
            lowbedAvailable = true,
            status = "شغّالة",
            ownerName = "مؤسسة رواسي نجد للمعدات",
            imageKey = "wheel_loader",
            isMyFleet = true
        ),
        EquipmentEntity(
            name = "قلاب مرسيدس أكتروس 6×4",
            category = "قلاب",
            brand = "مرسيدس بنز",
            model = "Actros 3340",
            year = 2021,
            capacity = "24 م³ • 32 طن",
            city = "الرياض",
            district = "غرب الرياض — مشروع القدية",
            dailyPriceSar = 700,
            monthlyPriceSar = 13200,
            rating = 4.7,
            reviewCount = 61,
            hasOperator = true,
            lowbedAvailable = false,
            status = "متاحة",
            ownerName = "مجموعة العليان للنقليات",
            imageKey = "dump_truck",
            isMyFleet = false
        ),
        EquipmentEntity(
            name = "بلدوزر كاتربيلر D8R",
            category = "بلدوزر",
            brand = "كاتربيلر",
            model = "D8R",
            year = 2021,
            capacity = "38 طن • شق وتسوية طرق",
            city = "الدمام",
            district = "الدمام — المدينة الصناعية الثانية",
            dailyPriceSar = 1450,
            monthlyPriceSar = 28500,
            rating = 4.9,
            reviewCount = 29,
            hasOperator = true,
            lowbedAvailable = true,
            status = "شغّالة",
            ownerName = "مؤسسة رواسي نجد للمعدات",
            imageKey = "excavator",
            isMyFleet = true
        ),
        EquipmentEntity(
            name = "كرين تادانو متحرك 50 طن",
            category = "كرين",
            brand = "تادانو",
            model = "GR-500EX",
            year = 2022,
            capacity = "50 طن • ذراع 42 متر",
            city = "جدة",
            district = "شمال جدة — حي أبحر",
            dailyPriceSar = 1650,
            monthlyPriceSar = 32000,
            rating = 4.9,
            reviewCount = 41,
            hasOperator = true,
            lowbedAvailable = true,
            status = "متاحة",
            ownerName = "شركة الساحل الغربي للمعدات",
            imageKey = "crane_lowbed",
            isMyFleet = false
        ),
        EquipmentEntity(
            name = "رصاصة بوماج BW211D-5",
            category = "رصاصة",
            brand = "بوماج",
            model = "BW211D-5",
            year = 2023,
            capacity = "12 طن • دك أسفلت وتربة",
            city = "الرياض",
            district = "جنوب الرياض — العزيزية",
            dailyPriceSar = 600,
            monthlyPriceSar = 11400,
            rating = 4.8,
            reviewCount = 19,
            hasOperator = false,
            lowbedAvailable = true,
            status = "صيانة",
            ownerName = "مؤسسة رواسي نجد للمعدات",
            imageKey = "wheel_loader",
            isMyFleet = true
        ),
        EquipmentEntity(
            name = "مولد كهرباء كاتربيلر 250 ك.ف.أ",
            category = "مولدات",
            brand = "كاتربيلر",
            model = "DE250E0",
            year = 2023,
            capacity = "250 kVA • كاتم صوت للمشاريع",
            city = "جدة",
            district = "جدة — طريق الحرمين",
            dailyPriceSar = 550,
            monthlyPriceSar = 9800,
            rating = 4.9,
            reviewCount = 27,
            hasOperator = false,
            lowbedAvailable = true,
            status = "متاحة",
            ownerName = "طاقة الجزيرة للتأجير",
            imageKey = "dump_truck",
            isMyFleet = false
        ),
        EquipmentEntity(
            name = "لوبد نقل ثقيل 4 محاور (60 طن)",
            category = "لوبد",
            brand = "فولفو FH16",
            model = "Lowbed 60T",
            year = 2022,
            capacity = "60 طن • تصريح لوجستي TGA",
            city = "الرياض",
            district = "الرياض — مخرج 18 الدائري الشرقي",
            dailyPriceSar = 1200,
            monthlyPriceSar = 22000,
            rating = 5.0,
            reviewCount = 74,
            hasOperator = true,
            lowbedAvailable = true,
            status = "متاحة",
            ownerName = "أسطول الراجحي للنقل الثقيل",
            imageKey = "crane_lowbed",
            isMyFleet = false
        )
    )

    fun initialContracts(): List<ContractEntity> = listOf(
        ContractEntity(
            contractCode = "ATD-2026-8410",
            equipmentId = 1,
            equipmentName = "بوكلين كاتربيلر 320D",
            contractorName = "شركة البنيان الحديث للمقاولات",
            ownerName = "مؤسسة رواسي نجد للمعدات",
            city = "الرياض",
            siteLocation = "شمال الرياض — مشروع بوابة الدرعية (قطاع B4)",
            contractType = "عقد مشروع شهري",
            durationLabel = "شهر واحد (260 ساعة)",
            includeLowbed = true,
            lowbedCostSar = 1400,
            baseTotalSar = 17500,
            vatAmountSar = 2835,
            grandTotalSar = 21735,
            platformCommissionSar = 1225,
            ownerNetPayoutSar = 16275,
            status = "نشط",
            escrowStatus = "محجوز بالضمان",
            operatorAssigned = "سعد القحطاني (رخصة سارية)"
        ),
        ContractEntity(
            contractCode = "ATD-2026-8422",
            equipmentId = 2,
            equipmentName = "شيول كوماتسو WA380-8",
            contractorName = "شركة أفق العمران للبنية التحتية",
            ownerName = "مؤسسة رواسي نجد للمعدات",
            city = "الرياض",
            siteLocation = "شرق الرياض — طريق الدمام السريع",
            contractType = "أسبوعي",
            durationLabel = "أسبوع واحد (7 أيام)",
            includeLowbed = true,
            lowbedCostSar = 1100,
            baseTotalSar = 5950,
            vatAmountSar = 1058,
            grandTotalSar = 8108,
            platformCommissionSar = 417,
            ownerNetPayoutSar = 5533,
            status = "قيد المراجعة",
            escrowStatus = "محجوز بالضمان",
            operatorAssigned = "بدون مشغّل (مطلوب سادة)"
        ),
        ContractEntity(
            contractCode = "ATD-2026-8395",
            equipmentId = 4,
            equipmentName = "بلدوزر كاتربيلر D8R",
            contractorName = "شركة المسار الشرقي للطرق",
            ownerName = "مؤسسة رواسي نجد للمعدات",
            city = "الدمام",
            siteLocation = "الجبيل الصناعية — توسعة الطريق الساحلي",
            contractType = "عقد مشروع شهري",
            durationLabel = "شهران (520 ساعة)",
            includeLowbed = false,
            lowbedCostSar = 0,
            baseTotalSar = 57000,
            vatAmountSar = 8550,
            grandTotalSar = 65550,
            platformCommissionSar = 3990,
            ownerNetPayoutSar = 53010,
            status = "نشط",
            escrowStatus = "محجوز بالضمان",
            operatorAssigned = "محمد الشمري (موثّق)"
        )
    )

    fun initialLowbedTrips(): List<LowbedTripEntity> = listOf(
        LowbedTripEntity(
            tripCode = "LBD-9041",
            contractCode = "ATD-2026-8410",
            equipmentName = "بوكلين كاتربيلر 320D",
            weightTons = 22,
            pickupLocation = "حوش رواسي نجد — مخرج 18 الرياض",
            dropoffLocation = "مشروع بوابة الدرعية — قطاع B4",
            city = "الرياض",
            distanceKm = 34,
            priceSar = 1400,
            stage = "في الطريق للموقع",
            pickupPhotoAttached = true,
            deliveryPhotoAttached = false,
            isSigned = false
        ),
        LowbedTripEntity(
            tripCode = "LBD-9052",
            contractCode = "ATD-2026-8422",
            equipmentName = "شيول كوماتسو WA380-8",
            weightTons = 18,
            pickupLocation = "حي القادسية — شرق الرياض",
            dropoffLocation = "موقع توسعة طريق الملك سلمان",
            city = "الرياض",
            distanceKm = 27,
            priceSar = 1100,
            stage = "طلب جديد"
        ),
        LowbedTripEntity(
            tripCode = "LBD-8977",
            contractCode = "ATD-2026-8312",
            equipmentName = "كرين تادانو 50 طن",
            weightTons = 38,
            pickupLocation = "ميناء جدة الإسلامي — بوابة 4",
            dropoffLocation = "مشروع الواجهة البحرية — أبحر",
            city = "جدة",
            distanceKm = 46,
            priceSar = 1950,
            stage = "تم التسليم",
            pickupPhotoAttached = true,
            deliveryPhotoAttached = true,
            isSigned = true,
            signedByName = "م. خالد الغامدي"
        )
    )

    fun initialOperatorJobs(): List<OperatorJobEntity> = listOf(
        OperatorJobEntity(
            title = "مشغّل بوكلين كاتربيلر 320D",
            equipmentType = "بوكلين",
            contractorName = "شركة البنيان الحديث للمقاولات",
            city = "الرياض",
            district = "حي النرجس — شمال الرياض",
            distanceKm = 4.2,
            wageType = "شهري",
            wageSar = 6500,
            shiftHours = 9,
            status = "متاح",
            dateRange = "1 ديسمبر — 31 ديسمبر"
        ),
        OperatorJobEntity(
            title = "مشغّل شيول كوماتسو WA380",
            equipmentType = "شيول",
            contractorName = "شركة أفق العمران للبنية التحتية",
            city = "الرياض",
            district = "طريق الدمام السريع — شرق الرياض",
            distanceKm = 7.8,
            wageType = "يومي",
            wageSar = 320,
            shiftHours = 8,
            status = "متاح",
            dateRange = "لمدة 7 أيام عمل"
        ),
        OperatorJobEntity(
            title = "مشغّل بلدوزر D8R — وردية صباحية",
            equipmentType = "بلدوزر",
            contractorName = "شركة المسار الشرقي للطرق",
            city = "الدمام",
            district = "الجبيل الصناعية",
            distanceKm = 11.5,
            wageType = "شهري",
            wageSar = 7800,
            shiftHours = 10,
            status = "مقبول",
            dateRange = "نشط حالياً — ينتهي 28 ديسمبر"
        )
    )

    fun initialWorkLogs(): List<WorkLogEntity> = listOf(
        WorkLogEntity(
            roleTag = "CONTRACTOR",
            siteOrEquipment = "بوكلين كاتربيلر 320D — بوابة الدرعية",
            dateLabel = "اليوم • 24 نوفمبر",
            checkInTime = "06:30 ص",
            checkOutTime = "قيد التشغيل",
            durationMinutes = 405,
            coordinates = "24.7341° N, 46.5752° E",
            photoAttached = true,
            status = "قيد التشغيل"
        ),
        WorkLogEntity(
            roleTag = "CONTRACTOR",
            siteOrEquipment = "بوكلين كاتربيلر 320D — بوابة الدرعية",
            dateLabel = "أمس • 23 نوفمبر",
            checkInTime = "06:30 ص",
            checkOutTime = "03:30 م",
            durationMinutes = 540,
            coordinates = "24.7341° N, 46.5752° E",
            photoAttached = true,
            status = "معتمد GPS"
        ),
        WorkLogEntity(
            roleTag = "OPERATOR",
            siteOrEquipment = "بلدوزر D8R — توسعة الطريق الساحلي",
            dateLabel = "أمس • 23 نوفمبر",
            checkInTime = "06:00 ص",
            checkOutTime = "04:00 م",
            durationMinutes = 600,
            coordinates = "26.4207° N, 50.0888° E",
            photoAttached = true,
            status = "معتمد GPS"
        )
    )

    fun initialInvoices(): List<ZatcaInvoiceEntity> = listOf(
        ZatcaInvoiceEntity(
            invoiceNumber = "INV-ZATCA-2026-0914",
            contractCode = "ATD-2026-8410",
            clientName = "شركة البنيان الحديث للمقاولات",
            clientVatNumber = "310482917400003",
            subtotalSar = 18900,
            vat15Sar = 2835,
            totalSar = 21735,
            issueDate = "2026-11-22",
            status = "محمي بالضمان",
            qrPayload = "ATAAD|310482917400003|2026-11-22T09:14:00Z|21735.00|2835.00"
        ),
        ZatcaInvoiceEntity(
            invoiceNumber = "INV-ZATCA-2026-0881",
            contractCode = "ATD-2026-8395",
            clientName = "شركة البنيان الحديث للمقاولات",
            clientVatNumber = "310482917400003",
            subtotalSar = 57000,
            vat15Sar = 8550,
            totalSar = 65550,
            issueDate = "2026-11-10",
            status = "مسددة",
            qrPayload = "ATAAD|310482917400003|2026-11-10T11:30:00Z|65550.00|8550.00"
        )
    )

    fun initialWalletTransactions(): List<WalletTransactionEntity> = listOf(
        // Contractor
        WalletTransactionEntity(
            portalRole = "CONTRACTOR",
            title = "إيداع ضمان عقد بوكلين 320D",
            referenceCode = "ESC-8410",
            amountSar = 21735,
            isCredit = false,
            category = "ضمان",
            dateLabel = "22 نوفمبر 2026",
            status = "محجوز بالضمان"
        ),
        WalletTransactionEntity(
            portalRole = "CONTRACTOR",
            title = "شحن محفظة الشركة — مصرف الراجحي",
            referenceCode = "BNK-5519",
            amountSar = 120000,
            isCredit = true,
            category = "تحويل بنكي",
            dateLabel = "15 نوفمبر 2026",
            status = "مكتمل"
        ),
        // Owner
        WalletTransactionEntity(
            portalRole = "OWNER",
            title = "صافي عقد بلدوزر D8R (بعد عمولة 7%)",
            referenceCode = "PAY-8395",
            amountSar = 53010,
            isCredit = true,
            category = "جاهز للتحويل",
            dateLabel = "20 نوفمبر 2026",
            status = "جاهز للتحويل"
        ),
        WalletTransactionEntity(
            portalRole = "OWNER",
            title = "مستحقات بوكلين 320D (قيد الضمان)",
            referenceCode = "ESC-8410",
            amountSar = 16275,
            isCredit = true,
            category = "ضمان",
            dateLabel = "22 نوفمبر 2026",
            status = "محجوز بالضمان"
        ),
        WalletTransactionEntity(
            portalRole = "OWNER",
            title = "تحويل بنكي إلى آيبان البنك الأهلي",
            referenceCode = "IBAN-7731",
            amountSar = 38500,
            isCredit = false,
            category = "تحويل بنكي",
            dateLabel = "10 نوفمبر 2026",
            status = "تم التحويل"
        ),
        // Operator
        WalletTransactionEntity(
            portalRole = "OPERATOR",
            title = "مستحقات تشغيل بلدوزر D8R — الأسبوع 3",
            referenceCode = "OPR-3102",
            amountSar = 1950,
            isCredit = true,
            category = "جاهز للتحويل",
            dateLabel = "21 نوفمبر 2026",
            status = "جاهز للتحويل"
        ),
        WalletTransactionEntity(
            portalRole = "OPERATOR",
            title = "أجر ورديات جارية (محمي بالضمان)",
            referenceCode = "ESC-OP-44",
            amountSar = 1280,
            isCredit = true,
            category = "ضمان",
            dateLabel = "24 نوفمبر 2026",
            status = "محجوز بالضمان"
        ),
        // Lowbed
        WalletTransactionEntity(
            portalRole = "LOWBED",
            title = "أجرة رحلة نقل كرين تادانو — جدة",
            referenceCode = "LBD-8977",
            amountSar = 1950,
            isCredit = true,
            category = "جاهز للتحويل",
            dateLabel = "19 نوفمبر 2026",
            status = "جاهز للتحويل"
        ),
        WalletTransactionEntity(
            portalRole = "LOWBED",
            title = "رحلة جارية — بوكلين 320D الرياض",
            referenceCode = "LBD-9041",
            amountSar = 1400,
            isCredit = true,
            category = "ضمان",
            dateLabel = "24 نوفمبر 2026",
            status = "محجوز بالضمان"
        )
    )
}
