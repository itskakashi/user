//// PaymentDao.kt
//package com.example.authenticationn.Data.FireStoreDatabase.Daos
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Update
//import com.example.authenticationn.Data.local.entity.PaymentEntity
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface PaymentDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(payment: PaymentEntity)
//
//    @Update
//    suspend fun update(payment: PaymentEntity)
//
//    @Query("SELECT * FROM payment_table WHERE paymentId = :paymentId")
//    suspend fun getPaymentById(paymentId: String): PaymentEntity?
//
//    @Query("SELECT * FROM payment_table WHERE userId = :userId")
//    fun getPaymentsByUserId(userId: String): Flow<List<PaymentEntity>>
//
//    @Query("SELECT * FROM payment_table")
//    fun getAllPayments(): Flow<List<PaymentEntity>>
//}