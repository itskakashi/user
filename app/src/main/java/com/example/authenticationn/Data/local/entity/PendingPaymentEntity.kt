package com.example.authenticationn.Data.local.entity

// PendingPaymentEntity.kt

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.authenticationn.Data.FireStoreDatabase.Models.PendingPayment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

@Entity(tableName = "pending_payment_table")
data class PendingPaymentEntity(
    @PrimaryKey val pendingPaymentId: String,
    var userId: String? = null,
    var amount: Double? = null,
    var dueDate: Long? = null,
    var isOverdue: Boolean? = null
) {
    fun toPendingPayment(): PendingPayment {
        val userRef: DocumentReference? = userId?.let { FirebaseFirestore.getInstance().document("Users/$it") }

        return PendingPayment(
            pendingPaymentId = pendingPaymentId,
            userId = userRef,
            amount = amount,
            dueDate = if (dueDate != null) Timestamp(java.util.Date(dueDate!!)) else null,
            isOverdue = isOverdue
        )
    }
}