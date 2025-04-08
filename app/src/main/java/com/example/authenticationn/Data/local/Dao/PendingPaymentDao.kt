//package com.example.authenticationn.Data.local.Dao
//
//// PendingPaymentDao.kt
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Update
//import com.example.authenticationn.Data.local.entity.PendingPaymentEntity
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface PendingPaymentDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(pendingPayment: PendingPaymentEntity)
//
//    @Update
//    suspend fun update(pendingPayment: PendingPaymentEntity)
//
//    @Query("SELECT * FROM pending_payment_table WHERE pendingPaymentId = :pendingPaymentId")
//    suspend fun getPendingPaymentById(pendingPaymentId: String): PendingPaymentEntity?
//
//    @Query("SELECT * FROM pending_payment_table WHERE userId = :userId")
//    fun getPendingPaymentsByUserId(userId: String): Flow<List<PendingPaymentEntity>>
//
//    @Query("SELECT * FROM pending_payment_table")
//    fun getAllPendingPayments(): Flow<List<PendingPaymentEntity>>
//}