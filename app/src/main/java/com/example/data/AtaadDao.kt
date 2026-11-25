package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AtaadDao {

    // Equipment
    @Query("SELECT * FROM equipment ORDER BY id DESC")
    fun observeAllEquipment(): Flow<List<EquipmentEntity>>

    @Query("SELECT COUNT(*) FROM equipment")
    suspend fun getEquipmentCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipment(item: EquipmentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEquipment(items: List<EquipmentEntity>)

    @Query("UPDATE equipment SET status = :status WHERE id = :id")
    suspend fun updateEquipmentStatus(id: Int, status: String)

    // Contracts & Orders
    @Query("SELECT * FROM contracts ORDER BY createdAt DESC")
    fun observeAllContracts(): Flow<List<ContractEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContract(contract: ContractEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllContracts(contracts: List<ContractEntity>)

    @Update
    suspend fun updateContract(contract: ContractEntity)

    // Lowbed Trips
    @Query("SELECT * FROM lowbed_trips ORDER BY createdAt DESC")
    fun observeAllLowbedTrips(): Flow<List<LowbedTripEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLowbedTrip(trip: LowbedTripEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLowbedTrips(trips: List<LowbedTripEntity>)

    @Update
    suspend fun updateLowbedTrip(trip: LowbedTripEntity)

    // Operator Jobs
    @Query("SELECT * FROM operator_jobs ORDER BY id DESC")
    fun observeOperatorJobs(): Flow<List<OperatorJobEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllOperatorJobs(jobs: List<OperatorJobEntity>)

    @Query("UPDATE operator_jobs SET status = :status WHERE id = :id")
    suspend fun updateOperatorJobStatus(id: Int, status: String)

    // Work Logs
    @Query("SELECT * FROM work_logs ORDER BY id DESC")
    fun observeWorkLogs(): Flow<List<WorkLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkLog(log: WorkLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWorkLogs(logs: List<WorkLogEntity>)

    // ZATCA Invoices
    @Query("SELECT * FROM zatca_invoices ORDER BY id DESC")
    fun observeInvoices(): Flow<List<ZatcaInvoiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoice(invoice: ZatcaInvoiceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllInvoices(invoices: List<ZatcaInvoiceEntity>)

    // Wallet Transactions
    @Query("SELECT * FROM wallet_transactions ORDER BY id DESC")
    fun observeWalletTransactions(): Flow<List<WalletTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWalletTransaction(tx: WalletTransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWalletTransactions(txs: List<WalletTransactionEntity>)

    // Registrations
    @Query("SELECT * FROM registrations ORDER BY submittedAt DESC")
    fun observeRegistrations(): Flow<List<RegistrationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(record: RegistrationEntity): Long
}
