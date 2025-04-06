package com.example.authenticationn.Data.local.entity

// PaymentEntity.kt

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.authenticationn.Data.FireStoreDatabase.Models.Payment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

@Entity(tableName = "payment_table")
data class PaymentEntity(
    @PrimaryKey val paymentId: String,
    var userId: String? = null,
    var paymentAmount: Double? = null,
    var paymentDate: Long? = null,
    var paymentMethod: String? = null,
    var notes: String? = null,
    var billId: String? = null
) {
    fun toPayment(): Payment {
        val userRef: DocumentReference? = userId?.let { FirebaseFirestore.getInstance().document("Users/$it") }
        val billRef: DocumentReference? = billId?.let { FirebaseFirestore.getInstance().document("Bills/$it") }

        return Payment(
            paymentId = paymentId,
            userId = userRef,
            paymentAmount = paymentAmount,
            paymentDate = if (paymentDate != null) Timestamp(java.util.Date(paymentDate!!)) else null,
            paymentMethod = paymentMethod,
            notes = notes,
            billId = billRef
        )
    }
}