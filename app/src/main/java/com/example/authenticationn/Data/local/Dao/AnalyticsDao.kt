//package com.example.authenticationn.Data.local.Dao
//
//// AnalyticsDao.kt
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Update
//import com.example.authenticationn.Data.local.entity.AnalyticsEntity
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface AnalyticsDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(analytics: AnalyticsEntity)
//
//    @Update
//    suspend fun update(analytics: AnalyticsEntity)
//
//    @Query("SELECT * FROM analytics_table WHERE analyticsId = :analyticsId")
//    suspend fun getAnalyticsById(analyticsId: String): AnalyticsEntity?
//
//    @Query("SELECT * FROM analytics_table WHERE userId = :userId")
//    fun getAnalyticsByUserId(userId: String): Flow<List<AnalyticsEntity>>
//
//    @Query("SELECT * FROM analytics_table")
//    fun getAllAnalytics(): Flow<List<AnalyticsEntity>>
//}