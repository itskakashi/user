//package com.example.authenticationn.Data.local.entity
//
//// BillEntity.kt
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import com.example.authenticationn.Data.FireStoreDatabase.Models.Bill
//import com.google.firebase.Timestamp
//import com.google.firebase.firestore.DocumentReference
//import com.google.firebase.firestore.FirebaseFirestore
//
//@Entity(tableName = "bill_table")
//data class BillEntity(
//    @PrimaryKey val billId: String,
//    var userId: String? = null,
//    var amount: Double? = null,
//    var date: Long? = null,
//    var month: String? = null,
//    var year: String? = null,
//    var isPaid: Boolean? = null,
//    var orderId: String? = null,
//    var overdueDate: Long? = null,
//    var isOverdue: Boolean? = null,
//    var totalJars: Int? = null,
//    var paymentStatus: String? = null,
//) {
//    fun toBill(): Bill {
//        val userRef: DocumentReference? = userId?.let { FirebaseFirestore.getInstance().document("Users/$it") }
//        val orderRef: DocumentReference? = orderId?.let { FirebaseFirestore.getInstance().document("Orders/$it") }
//
//        return Bill(
//            billId = billId,
//            userId = userRef,
//            amount = amount,
//            date = if (date != null) Timestamp(java.util.Date(date!!)) else null,
//            month = month,
//            year = year,
//            isPaid = isPaid,
//            orderId = orderRef,
//            overdueDate = if (overdueDate != null) Timestamp(java.util.Date(overdueDate!!)) else null,
//            isOverdue = isOverdue,
//            totalJars = totalJars,
//            paymentStatus = paymentStatus
//        )
//    }
//}