

import com.example.authenticationn.Data.FireStoreDatabase.Models.Bill
import com.example.authenticationn.Data.FireStoreDatabase.Models.Payment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class BillingAndPaymentManager {
    private val db: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // --- Creating a New Bill ---
    suspend fun createBill(bill: Bill): Result<String> {
        return try {
            val documentReference = db.collection("Billing").document()
            val billId = documentReference.id
            bill.billId = billId
            documentReference.set(bill).await()
            Result.success(billId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Marking a Bill as Paid ---
    suspend fun markBillAsPaid(billId: String): Result<Unit> {
        return try {
            db.collection("Billing").document(billId)
                .update("isPaid", true, "paymentStatus", "Paid", "updatedAt", FieldValue.serverTimestamp()).await()
            Result.success(Unit) // Changed to Result<Unit> and Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- update bill ---
    suspend fun updateBill(bill: Bill): Result<Unit> {
        return try {
            db.collection("Billing").document(bill.billId!!).update(
                mapOf(
                    "userId" to bill.userId,
                    "amount" to bill.amount,
                    "date" to bill.date,
                    "month" to bill.month,
                    "year" to bill.year,
                    "isPaid" to bill.isPaid,
                    "orderId" to bill.orderId,
                    "overdueDate" to bill.overdueDate,
                    "isOverdue" to bill.isOverdue,
                    "totalJars" to bill.totalJars,
                    "paymentStatus" to bill.paymentStatus,
                    "itemizedDetails" to bill.itemizedDetails,
                    "updatedAt" to FieldValue.serverTimestamp()

                )
            ).await()
            Result.success(Unit) // Changed to Result<Unit> and Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Retrieving Bill Information ---
    suspend fun getBill(billId: String): Result<Bill> {
        return try {
            val document = db.collection("Billing").document(billId).get().await()
            if (document.exists()) {
                val bill = document.toObject(Bill::class.java)
                Result.success(bill!!)
            } else {
                Result.failure(Exception("Bill not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Get All Bills ---
    suspend fun getAllBills(): Result<List<Bill>> {
        return try {
            val querySnapshot = db.collection("Billing").get().await()
            val bills = mutableListOf<Bill>()
            for (document in querySnapshot.documents) {
                val bill = document.toObject(Bill::class.java)
                bill?.let { bills.add(it) }
            }
            Result.success(bills)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Recording a Payment ---
    suspend fun recordPayment(payment: Payment): Result<String> {
        return try {
            val documentReference = db.collection("Payments").document()
            val paymentId = documentReference.id
            payment.paymentId = paymentId
            documentReference.set(payment).await()
            Result.success(paymentId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Get All payments ---
    suspend fun getAllPayments(): Result<List<Payment>> {
        return try {
            val querySnapshot = db.collection("Payments").get().await()
            val payments = mutableListOf<Payment>()
            for (document in querySnapshot.documents) {
                val payment = document.toObject(Payment::class.java)
                payment?.let { payments.add(it) }
            }
            Result.success(payments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}