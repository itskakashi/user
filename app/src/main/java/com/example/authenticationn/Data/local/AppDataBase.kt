//package com.example.authenticationn.Data.local
//
//// AppDatabase.kt
//
//
//import androidx.room.Database
//import androidx.room.RoomDatabase
//import androidx.room.TypeConverters
//import com.example.authenticationn.Data.FireStoreDatabase.Daos.PaymentDao
//import com.example.authenticationn.Data.local.Dao.AnalyticsDao
//import com.example.authenticationn.Data.local.Dao.BillDao
//import com.example.authenticationn.Data.local.Dao.PendingPaymentDao
//import com.example.authenticationn.Data.local.Dao.UserDao
//import com.example.authenticationn.Data.local.Dao.orderdao
//import com.example.authenticationn.Data.local.converter.Converters
//import com.example.authenticationn.Data.local.entity.AnalyticsEntity
//import com.example.authenticationn.Data.local.entity.BillEntity
//import com.example.authenticationn.Data.local.entity.OrderEntity
//import com.example.authenticationn.Data.local.entity.PaymentEntity
//import com.example.authenticationn.Data.local.entity.PendingPaymentEntity
//import com.example.authenticationn.Data.local.entity.UserEntity
//
//
//@Database(
//    entities = [
//        UserEntity::class,
//        OrderEntity::class,
//        BillEntity::class,
//        AnalyticsEntity::class,
//        PaymentEntity::class,
//        PendingPaymentEntity::class
//    ],
//    version = 1,
//    exportSchema = false
//)
//@TypeConverters(Converters::class)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun userDao(): UserDao
//    abstract fun orderDao(): orderdao
//    abstract fun billDao(): BillDao
//    abstract fun analyticsDao(): AnalyticsDao
//    abstract fun paymentDao(): PaymentDao
//    abstract fun pendingPaymentDao(): PendingPaymentDao
//}