package com.example.authenticationn.Presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authenticationn.Data.FireStoreDatabase.Models.Analytics
import com.example.authenticationn.Data.FireStoreDatabase.Models.Bill
import com.example.authenticationn.Data.FireStoreDatabase.Models.Order
import com.example.authenticationn.Data.FireStoreDatabase.Models.Payment
import com.example.authenticationn.Data.FireStoreDatabase.Models.User
import com.example.authenticationn.Domain.FireBaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FireBaseViewModel(private val  repository:FireBaseRepository) : ViewModel() {

    private val _userid= MutableStateFlow("")
    val userid=_userid.asStateFlow()


    private val _user= MutableStateFlow(User())
    val user =_user.asStateFlow()

    // --- LiveData for Orders ---
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    private val _todayOrders = MutableLiveData<List<Order>>()
    val todayOrders: LiveData<List<Order>> = _todayOrders

    private val _createOrderStatus = MutableLiveData<Result<String>>()
    val createOrderStatus: LiveData<Result<String>> = _createOrderStatus

    private val _getOrderStatus = MutableLiveData<Result<Order>>()
    val getOrderStatus: LiveData<Result<Order>> = _getOrderStatus

    private val _updateOrderStatus = MutableLiveData<Result<Unit>>()
    val updateOrderStatus: LiveData<Result<Unit>> = _updateOrderStatus

    private val _deleteOrderStatus = MutableLiveData<Result<Unit>>()
    val deleteOrderStatus: LiveData<Result<Unit>> = _deleteOrderStatus

//    private val _getOrdersByMonth = MutableStateFlow<List<Order>>(emptyList())
//    val getOrdersByMonth = _getOrdersByMonth.asStateFlow()

    private val _getOrdersByMonth = MutableLiveData<List<Order>>()
    val getOrdersByMonth :LiveData<List<Order>> =_getOrdersByMonth

    fun updateUserId(userid:String){
        _userid.update {
            userid
        }
    }


    fun updateCurrentUser(userid: String){
        viewModelScope.launch {
            repository.getUser(userid).onSuccess {
                    user ->
                _user.update {
                    user
                }
            }.onFailure {exception ->
                println("Error getting user : ${exception.message}")
            }

        }
    }


    // User-related functions
    fun signUp(user: User, password: String, onSuccess: (String) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.signUp(user, password).onSuccess { userId ->
                onSuccess(userId)
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun signIn(email: String, password: String, onSuccess: (String) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.signIn(email, password).onSuccess { userId ->
                onSuccess(userId)
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun getUser(userId: String, onSuccess: (User) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.getUser(userId).onSuccess { user ->
                onSuccess(user)
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun updateUser(user: User, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.updateUser(user).onSuccess {
                onSuccess()
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun addCustomer(user: User, password: String, onSuccess: (String) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.addCustomer(user, password).onSuccess { userId ->
                onSuccess(userId)
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun deleteUser(userId: String, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.deleteUser(userId).onSuccess {
                onSuccess()
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun getAllUsers(onSuccess: (List<User>) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.getAllUsers().onSuccess { users ->
                onSuccess(users)
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    // Order-related functions
    fun createOrder(userId:String,order: Order, onSuccess: (String) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.createOrder(userId,order).onSuccess {orderId ->
                _createOrderStatus.value = Result.success(orderId)
                onSuccess(orderId)
            }.onFailure { exception ->
                _createOrderStatus.value = Result.failure(exception)
                onFailure(exception)
            }
        }
    }

    fun getOrder(userId:String,order: Order, onSuccess: (Order) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.getOrder(userId,order).onSuccess { order ->
                _getOrderStatus.value = Result.success(order)
                onSuccess(order)
            }.onFailure { exception ->
                _getOrderStatus.value = Result.failure(exception)
                onFailure(exception)
            }
        }
    }

    fun updateOrderStatus(userId:String ,orderId: String, newStatus: String, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.updateOrderStatus( userId,orderId, newStatus).onSuccess {
                _updateOrderStatus.value = Result.success(it)
                onSuccess()
            }.onFailure { exception ->
                _updateOrderStatus.value = Result.failure(exception)
                onFailure(exception)
            }
        }
    }

    fun updateOrder(userId: String,order: Order, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.updateOrder(userId,order).onSuccess {
                _updateOrderStatus.value = Result.success(it)
                onSuccess()
            }.onFailure { exception ->
                _updateOrderStatus.value = Result.failure(exception)
                onFailure(exception)
            }
        }
    }

    fun deleteOrder(userId: String,orderId: String, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.deleteOrder(userId,orderId).onSuccess {
                _deleteOrderStatus.value = Result.success(it)
                onSuccess()
            }.onFailure { exception ->
                _deleteOrderStatus.value = Result.failure(exception)
                onFailure(exception)
            }
        }
    }

    fun getAllOrders(userId:String,onSuccess: (List<Order>) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.getAllOrders(userId).onSuccess { orders ->
                _orders.value=orders
                onSuccess(orders)
            }.onFailure { exception ->
                // Handle error
                onFailure(exception)
            }
        }
    }

    fun getAllTodayOrders(userId: String,onSuccess: (List<Order>) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.getAllTodayOrders(userId).onSuccess { todayOrders ->
                _todayOrders.value=todayOrders
                onSuccess(todayOrders)
            }.onFailure { exception ->
                // Handle error
                onFailure(exception)
            }
        }
    }
    fun getOrdersByMonth(userId: String, yearMonth: String, onSuccess: (List<Order>) -> Unit, onFailure: (Throwable) -> Unit){
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val firstDayOfMonth =  dateFormat.parse("$yearMonth-01")
            val calendar = java.util.Calendar.getInstance()
            calendar.time = firstDayOfMonth
            calendar.add(java.util.Calendar.MONTH, 1)
            calendar.add(java.util.Calendar.DAY_OF_MONTH,-1)
            val lastDayOfMonth = calendar.time
            repository.getOrdersByMonth(userId,firstDayOfMonth,lastDayOfMonth).onSuccess { orders ->
                _getOrdersByMonth.value=orders
                onSuccess(orders)
            }.onFailure { exception ->
                // Handle error
                onFailure(exception)
            }
        }
    }




    // Bill-related functions
    fun createBill(bill: Bill, onSuccess: (String) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.createBill(bill).onSuccess { billId ->
                onSuccess(billId)
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun markBillAsPaid(billId: String, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.markBillAsPaid(billId).onSuccess {
                onSuccess()
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun updateBill(bill: Bill, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.updateBill(bill).onSuccess {
                onSuccess()
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun getBill(billId: String, onSuccess: (Bill) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.getBill(billId).onSuccess { bill ->
                onSuccess(bill)
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun getAllBills(onSuccess: (List<Bill>) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.getAllBills().onSuccess { bills ->
                onSuccess(bills)
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun recordPayment(payment: Payment, onSuccess: (String) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.recordPayment(payment).onSuccess { paymentId ->
                onSuccess(paymentId)
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun getAllPayments(onSuccess: (List<Payment>) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.getAllPayments().onSuccess { payments ->
                onSuccess(payments)
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    // Analytics-related functions
    fun getAnalytics(analyticsId: String, onSuccess: (Analytics) -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.getAnalytics(analyticsId).onSuccess { analytics ->
                onSuccess(analytics)
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun updateAnalytics(analytics: Analytics, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.updateAnalytics(analytics).onSuccess {
                onSuccess()
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    // Canes-related functions
    fun updateCanesReturned(userId: String, canesReturned: Int, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.updateCanesReturned(userId, canesReturned).onSuccess {
                onSuccess()
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }

    fun updateCanesTaken(userId: String, canesTaken: Int, onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.updateCanesTaken(userId, canesTaken).onSuccess {
                onSuccess()
            }.onFailure { exception ->
                onFailure(exception)
            }
        }
    }
}