package com.example.authenticationn.Data.local.entity

// OrderEntity.kt

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.authenticationn.Data.FireStoreDatabase.Models.Order
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

@Entity(tableName = "order_table")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    var orderNumber: String? = null,
    var userID: String? = null,
    var deliveryAddress: String? = null,
    var waterType: String? = null,
    var quantity: Int? = null,
    var expectedDeliveryDate: Long? = null,
    var isDelivered: Boolean? = null,
    var deliveryTime: Long? = null,
    var deliveryStatus: String? = null,
    var deliveryFee: Double? = null,
    var totalAmount: Double? = null,
    var notes: String? = null,
    var items: Int? = null,
    var canesReturning: Int? = null,
) {
    fun toOrder(): Order {

        val userRef: DocumentReference? = userID?.let { FirebaseFirestore.getInstance().document("Users/$it") }

        return Order(
            orderId = orderId,
            orderNumber = orderNumber,
            userID = userRef,
            deliveryAddress = deliveryAddress,
            waterType = waterType,
            quantity = quantity,
            expectedDeliveryDate = if (expectedDeliveryDate != null) Timestamp(java.util.Date(expectedDeliveryDate!!)) else null,
            isDelivered = isDelivered,
            deliveryTime = if (deliveryTime != null) Timestamp(java.util.Date(deliveryTime!!)) else null,
            deliveryStatus = deliveryStatus,
            deliveryFee = deliveryFee,
            totalAmount = totalAmount,
            notes = notes,
            items = items,
            canesReturning = canesReturning
        )
    }
}