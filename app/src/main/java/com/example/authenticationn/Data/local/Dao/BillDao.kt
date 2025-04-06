package com.example.authenticationn.Data.local.Dao

// BillDao.kt

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.authenticationn.Data.local.entity.BillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bill: BillEntity)

    @Update
    suspend fun update(bill: BillEntity)

    @Query("SELECT * FROM bill_table WHERE billId = :billId")
    suspend fun getBillById(billId: String): BillEntity?

    @Query("SELECT * FROM bill_table WHERE userId = :userId")
    fun getBillsByUserId(userId: String): Flow<List<BillEntity>>

    @Query("SELECT * FROM bill_table")
    fun getAllBills(): Flow<List<BillEntity>>
}