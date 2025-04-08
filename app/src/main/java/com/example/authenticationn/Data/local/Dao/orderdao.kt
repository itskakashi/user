//package com.example.authenticationn.Data.local.Dao
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Update
//import com.example.authenticationn.Data.local.entity.OrderEntity
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface orderdao{
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(order: OrderEntity)
//
//    @Update
//    suspend fun update(order: OrderEntity)
//
//    @Query("SELECT * FROM order_table WHERE orderId = :orderId")
//    suspend fun getOrderById(orderId: String): OrderEntity?
//
//    @Query("SELECT * FROM order_table WHERE userID = :userId")
//    fun getOrdersByUserId(userId: String): Flow<List<OrderEntity>>
//
//    @Query("SELECT * FROM order_table")
//    fun getAllOrders(): Flow<List<OrderEntity>>
//}