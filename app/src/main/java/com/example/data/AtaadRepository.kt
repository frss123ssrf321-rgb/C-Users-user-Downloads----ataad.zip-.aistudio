package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlin.math.roundToInt

class AtaadRepository(private val dao: AtaadDao) {

    val equipmentFlow: Flow<List<EquipmentEntity>> = dao.observeAllEquipment()
    val contractsFlow: Flow<List<ContractEntity>> = dao.observeAllContracts()
    val lowbedTripsFlow: Flow<List<LowbedTripEntity>> = dao.observeAllLowbedTrips()
    val operatorJobsFlow: Flow<List<OperatorJobEntity>> = dao.observeOperatorJobs()
    val workLogsFlow: Flow<List<WorkLogEntity>> = dao.observeWorkLogs()
    val invoicesFlow: Flow<List<ZatcaInvoiceEntity>> = dao.observeInvoices()
    val walletTransactionsFlow: Flow<List<WalletTransactionEntity>> = dao.observeWalletTransactions()
    val registrationsFlow: Flow<List<RegistrationEntity>> = dao.observeRegistrations()

    suspend fun ensureSeeded() {
        if (dao.getEquipmentCount() == 0) {
            dao.insertAllEquipment(AtaadSaudiSeedData.initialEquipment())
            dao.insertAllContracts(AtaadSaudiSeedData.initialContracts())
            dao.insertAllLowbedTrips(AtaadSaudiSeedData.initialLowbedTrips())
            dao.insertAllOperatorJobs(AtaadSaudiSeedData.initialOperatorJobs())
            dao.insertAllWorkLogs(AtaadSaudiSeedData.initialWorkLogs())
            dao.insertAllInvoices(AtaadSaudiSeedData.initialInvoices())
            dao.insertAllWalletTransactions(AtaadSaudiSeedData.initialWalletTransactions())
        }
    }

    suspend fun bookEquipment(
        equipment: EquipmentEntity,
        contractType: String, // "يومي" | "أسبوعي" | "عقد مشروع شهري"
        units: Int,
        includeLowbed: Boolean,
        siteLocation: String
    ): String {
        val code = "ATD-2026-${(8500..9999).random()}"
        val basePrice = when (contractType) {
            "يومي" -> equipment.dailyPriceSar * units
            "أسبوعي" -> (equipment.dailyPriceSar * 6) * units
            else -> equipment.monthlyPriceSar * units
        }
        val lowbedCost = if (includeLowbed) 1250 else 0
        val subtotal = basePrice + lowbedCost
        val vat = (subtotal * 0.15).roundToInt()
        val grandTotal = subtotal + vat
        val commission = (basePrice * 0.07).roundToInt()
        val ownerNet = basePrice - commission

        val durationLabel = when (contractType) {
            "يومي" -> "$units أيام عمل"
            "أسبوعي" -> "$units أسابيع"
            else -> "$units أشهر (${units * 260} ساعة)"
        }

        val contract = ContractEntity(
            contractCode = code,
            equipmentId = equipment.id,
            equipmentName = equipment.name,
            contractorName = "شركة البنيان الحديث للمقاولات",
            ownerName = equipment.ownerName,
            city = equipment.city,
            siteLocation = siteLocation.ifBlank { "${equipment.city} — موقع المشروع الرئيسي" },
            contractType = contractType,
            durationLabel = durationLabel,
            includeLowbed = includeLowbed,
            lowbedCostSar = lowbedCost,
            baseTotalSar = basePrice,
            vatAmountSar = vat,
            grandTotalSar = grandTotal,
            platformCommissionSar = commission,
            ownerNetPayoutSar = ownerNet,
            status = "قيد المراجعة",
            escrowStatus = "محجوز بالضمان",
            operatorAssigned = if (equipment.hasOperator) "مشغّل معتمد مع المعدة" else "بانتظار تعيين مشغّل سادة"
        )
        dao.insertContract(contract)

        dao.insertInvoice(
            ZatcaInvoiceEntity(
                invoiceNumber = "INV-ZATCA-2026-${(1000..9999).random()}",
                contractCode = code,
                clientName = "شركة البنيان الحديث للمقاولات",
                clientVatNumber = "310482917400003",
                subtotalSar = subtotal,
                vat15Sar = vat,
                totalSar = grandTotal,
                issueDate = "2026-11-24",
                status = "محمي بالضمان",
                qrPayload = "ATAAD|310482917400003|2026-11-24|$grandTotal|$vat"
            )
        )

        dao.insertWalletTransaction(
            WalletTransactionEntity(
                portalRole = "CONTRACTOR",
                title = "حجز ضمان — ${equipment.name}",
                referenceCode = code,
                amountSar = grandTotal,
                isCredit = false,
                category = "ضمان",
                dateLabel = "اليوم • 24 نوفمبر",
                status = "محجوز بالضمان"
            )
        )

        dao.insertWalletTransaction(
            WalletTransactionEntity(
                portalRole = "OWNER",
                title = "طلب جديد — ${equipment.name} (صافي بعد 7%)",
                referenceCode = code,
                amountSar = ownerNet,
                isCredit = true,
                category = "ضمان",
                dateLabel = "اليوم • 24 نوفمبر",
                status = "محجوز بالضمان"
            )
        )

        if (includeLowbed) {
            dao.insertLowbedTrip(
                LowbedTripEntity(
                    tripCode = "LBD-${(9100..9999).random()}",
                    contractCode = code,
                    equipmentName = equipment.name,
                    weightTons = 24,
                    pickupLocation = "حوش ${equipment.ownerName} — ${equipment.city}",
                    dropoffLocation = contract.siteLocation,
                    city = equipment.city,
                    distanceKm = (18..48).random(),
                    priceSar = lowbedCost,
                    stage = "طلب جديد"
                )
            )
        }
        return code
    }

    suspend fun createProjectFleetTender(
        projectName: String,
        city: String,
        equipmentSummary: String,
        months: Int,
        estimatedMonthlyBudget: Int
    ): String {
        val code = "TND-2026-${(4100..4999).random()}"
        val baseTotal = estimatedMonthlyBudget * months
        val vat = (baseTotal * 0.15).roundToInt()
        val grandTotal = baseTotal + vat
        val commission = (baseTotal * 0.07).roundToInt()

        dao.insertContract(
            ContractEntity(
                contractCode = code,
                equipmentId = 0,
                equipmentName = "عقد أسطول مشروع: $equipmentSummary",
                contractorName = "شركة البنيان الحديث للمقاولات",
                ownerName = "أسطول عَتاد الموحّد",
                city = city,
                siteLocation = "$city — $projectName",
                contractType = "عقد مشروع شهري",
                durationLabel = "$months أشهر • أسطول متكامل",
                includeLowbed = true,
                lowbedCostSar = 2800,
                baseTotalSar = baseTotal,
                vatAmountSar = vat,
                grandTotalSar = grandTotal,
                platformCommissionSar = commission,
                ownerNetPayoutSar = baseTotal - commission,
                status = "قيد المراجعة",
                escrowStatus = "محجوز بالضمان",
                operatorAssigned = "طاقم مشغّلين معتمد"
            )
        )
        return code
    }

    suspend fun updateEquipmentStatus(id: Int, newStatus: String) {
        dao.updateEquipmentStatus(id, newStatus)
    }

    suspend fun addNewEquipment(item: EquipmentEntity) {
        dao.insertEquipment(item)
    }

    suspend fun acceptContractAsOwner(contract: ContractEntity) {
        dao.updateContract(contract.copy(status = "نشط"))
        if (contract.equipmentId > 0) {
            dao.updateEquipmentStatus(contract.equipmentId, "شغّالة")
        }
    }

    suspend fun rejectContractAsOwner(contract: ContractEntity) {
        dao.updateContract(contract.copy(status = "مرفوض"))
    }

    suspend fun requestLowbedForContract(contract: ContractEntity) {
        dao.updateContract(contract.copy(includeLowbed = true, status = "بانتظار اللوبد"))
        dao.insertLowbedTrip(
            LowbedTripEntity(
                tripCode = "LBD-${(9200..9999).random()}",
                contractCode = contract.contractCode,
                equipmentName = contract.equipmentName,
                weightTons = 25,
                pickupLocation = "موقع المالك — ${contract.city}",
                dropoffLocation = contract.siteLocation,
                city = contract.city,
                distanceKm = 31,
                priceSar = 1350,
                stage = "طلب جديد"
            )
        )
    }

    suspend fun reportBreakdownAndRequestReplacement(contract: ContractEntity, reason: String) {
        dao.updateContract(
            contract.copy(
                emergencyActive = true,
                emergencyReason = reason.ifBlank { "عطل هيدروليكي مفاجئ في الموقع" },
                replacementEtaHours = 18
            )
        )
    }

    suspend fun acceptOperatorJob(job: OperatorJobEntity) {
        dao.updateOperatorJobStatus(job.id, "مقبول")
    }

    suspend fun recordWorkLog(
        roleTag: String,
        siteOrEquipment: String,
        durationMinutes: Int,
        coordinates: String,
        photoAttached: Boolean
    ) {
        dao.insertWorkLog(
            WorkLogEntity(
                roleTag = roleTag,
                siteOrEquipment = siteOrEquipment,
                dateLabel = "اليوم • 24 نوفمبر",
                checkInTime = "07:00 ص",
                checkOutTime = "تم التوثيق الآن",
                durationMinutes = durationMinutes,
                coordinates = coordinates,
                photoAttached = photoAttached,
                status = "معتمد GPS"
            )
        )
        if (roleTag == "OPERATOR") {
            dao.insertWalletTransaction(
                WalletTransactionEntity(
                    portalRole = "OPERATOR",
                    title = "توثيق وردية عمل — $siteOrEquipment",
                    referenceCode = "SHIFT-${(100..999).random()}",
                    amountSar = 320,
                    isCredit = true,
                    category = "جاهز للتحويل",
                    dateLabel = "اليوم • 24 نوفمبر",
                    status = "جاهز للتحويل"
                )
            )
        }
    }

    suspend fun advanceLowbedTrip(trip: LowbedTripEntity) {
        val nextStage = when (trip.stage) {
            "طلب جديد" -> "في الطريق للتحميل"
            "في الطريق للتحميل" -> "تم التحميل"
            "تم التحميل" -> "في الطريق للموقع"
            "في الطريق للموقع" -> "تم التسليم"
            else -> "تم التسليم"
        }
        dao.updateLowbedTrip(trip.copy(stage = nextStage))
    }

    suspend fun updateLowbedTripPhotosAndSign(
        trip: LowbedTripEntity,
        pickupPhoto: Boolean,
        deliveryPhoto: Boolean,
        signed: Boolean,
        signerName: String
    ) {
        val updated = trip.copy(
            pickupPhotoAttached = pickupPhoto,
            deliveryPhotoAttached = deliveryPhoto,
            isSigned = signed,
            signedByName = signerName,
            stage = if (signed) "تم التسليم" else trip.stage
        )
        dao.updateLowbedTrip(updated)
        if (signed) {
            dao.insertWalletTransaction(
                WalletTransactionEntity(
                    portalRole = "LOWBED",
                    title = "تحرير أجرة رحلة ${trip.tripCode} — ${trip.equipmentName}",
                    referenceCode = trip.tripCode,
                    amountSar = trip.priceSar,
                    isCredit = true,
                    category = "جاهز للتحويل",
                    dateLabel = "اليوم • 24 نوفمبر",
                    status = "جاهز للتحويل"
                )
            )
        }
    }

    suspend fun withdrawWalletToIban(portalRole: String, amountSar: Int, ibanSuffix: String) {
        dao.insertWalletTransaction(
            WalletTransactionEntity(
                portalRole = portalRole,
                title = "تحويل بنكي فوري إلى آيبان (...$ibanSuffix)",
                referenceCode = "IBAN-${(1000..9999).random()}",
                amountSar = amountSar,
                isCredit = false,
                category = "تحويل بنكي",
                dateLabel = "اليوم • 24 نوفمبر",
                status = "تم التحويل"
            )
        )
    }

    suspend fun submitRegistration(record: RegistrationEntity) {
        dao.insertRegistration(record)
    }
}
