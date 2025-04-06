package com.example.authenticationn.Data.local.entity

// AnalyticsEntity.kt

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.authenticationn.Data.FireStoreDatabase.Models.Analytics
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

@Entity(tableName = "analytics_table")
data class AnalyticsEntity(
    @PrimaryKey val analyticsId: String,
    var userId: String? = null,
    var totalOrder: Int? = null,
    var totalDelivered: Int? = null,
    var totalAmount: Double? = null,
    var averageRating: Double? = null,
    var totalJars: Int? = null,
    var totalAmountSpend: Double? = null,
    var weeklyAverage: Double? = null,
    var newOrderToday: Int? = null,
    var scheduledDeliveries: Int? = null,
    var activeOrders: Int? = null,
    var totalRevenue: Double? = null,
    var availableStock: Int? = null,
    var activeCustomers: Int? = null,
) {
    fun toAnalytics(): Analytics {

        val userRef: DocumentReference? = userId?.let { FirebaseFirestore.getInstance().document("Users/$it") }
        return Analytics(
            analyticsId = analyticsId,
            userId = userRef,
            totalOrder = totalOrder,
            totalDelivered = totalDelivered,
            totalAmount = totalAmount,
            averageRating = averageRating,
            totalJars = totalJars,
            totalAmountSpend = totalAmountSpend,
            weeklyAverage = weeklyAverage,
            newOrderToday = newOrderToday,
            scheduledDeliveries = scheduledDeliveries,
            activeOrders = activeOrders,
            totalRevenue = totalRevenue,
            availableStock = availableStock,
            activeCustomers = activeCustomers
        )
    }
}