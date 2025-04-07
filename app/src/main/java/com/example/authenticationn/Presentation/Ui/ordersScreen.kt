package com.example.authenticationn.Presentation.Ui

import BillingAndPaymentManager
import android.R.attr.onClick
import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.copy
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.authenticationn.Data.FireStoreDatabase.Models.Order
import com.example.authenticationn.Data.FireStoreDatabase.managers.AnalyticsManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.CanesManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.OrderManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.UserManager
import com.example.authenticationn.Domain.FireBaseRepository
import com.example.authenticationn.Presentation.FireBaseViewModel
import com.example.authenticationn.R
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun orderScreen(navController: NavController, viewModel: FireBaseViewModel, userId: String) {
    val pendingColor = remember { mutableStateOf(MyColor.focussedIconColor) }
    val completedColor = remember { mutableStateOf(MyColor.unfocusedIconColor) }

    var selectedDate = remember { mutableStateOf(getCurrentDate()) }
    val context = LocalContext.current
    //year and month of the selected date
    val selectedYearMonth = YearMonth.parse(selectedDate.value.substring(0, 7))

    LaunchedEffect(key1 = selectedDate.value, key2 = userId) {
        // Get orders by month
        viewModel.getOrdersByMonth(
            userId, selectedDate.value,
            onSuccess = {} ,
            onFailure = {}
        )
    }

    var orders by remember { mutableStateOf<List<Order>>(emptyList())}
    orders = viewModel.getOrdersByMonth.observeAsState(emptyList()).value

    var totalOrders by remember { mutableStateOf(0) }
    var pendingOrders by remember { mutableStateOf(0) }
    var completedOrders by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = orders) {
        // Calculate counts when orders change
        totalOrders = orders.size
        pendingOrders = orders.count { it.deliveryStatus == "Pending" }
        completedOrders = orders.count { it.deliveryStatus == "Completed" }
    }


    // Date Picker Dialog
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            selectedDate.value = String.format("%04d-%02d", mYear, mMonth + 1)
        }, year, month, day
    )

    Scaffold(
        topBar = { topbar("Your Order", {}) },
        bottomBar = { bottomBar(navController) }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MyColor.backGroundColor)
                .padding(it)
        ) {

            Column(
                Modifier
                    .padding(10.dp)
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                    .padding(10.dp)
            ) {
                Box(Modifier.clickable {
                    datePickerDialog.show()
                }) {
                    Row() {
                        Text(
                            "${monthList[selectedDate.value.substring(5, 7).toInt() - 1]} " +
                                    selectedDate.value.substring(0, 4),
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                        )
                        Spacer(Modifier.width(5.dp))
                        Icon(imageVector = Icons.Filled.ArrowDropDown, null)
                    }
                    Spacer(Modifier.height(10.dp))
                }
                Spacer(Modifier.height(15.dp))

                Column() {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Box() {
                            Column {
                                Text(
                                    totalOrders.toString(),
                                    style = TextStyle(
                                        color = Color(0xFF111827),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    )
                                )
                                Spacer(Modifier.height(5.dp))
                                Text(
                                    "Total Orders",
                                    style = TextStyle(
                                        color = Color(0xFF4B5563),
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }

                        Box() {
                            Column {
                                Text(
                                    pendingOrders.toString(),
                                    style = TextStyle(
                                        color = Color(0xFFF97316),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    )
                                )
                                Spacer(Modifier.height(5.dp))
                                Text(
                                    "Pending",
                                    style = TextStyle(
                                        color = Color(0xFF4B5563),
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }
                        Box() {
                            Column {
                                Text(
                                    completedOrders.toString(),
                                    style = TextStyle(
                                        color = Color(0xFF22C55E),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    )
                                )
                                Spacer(Modifier.height(5.dp))
                                Text(
                                    "Completed",
                                    style = TextStyle(
                                        color = Color(0xFF4B5563),
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                        }


                    }

                }
                Spacer(Modifier.height(10.dp))
            }
            Spacer(Modifier.height(5.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .size(width = 195.dp, height = 50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    Modifier.clickable {
                        pendingColor.value = MyColor.focussedIconColor
                        completedColor.value = MyColor.unfocusedIconColor
                    }

                ) {
                    Text(
                        "Pending",
                        style = TextStyle(
                            color = pendingColor.value,
                            fontWeight = (if (pendingColor.value == MyColor.focussedIconColor) FontWeight.Medium else FontWeight.Normal),
                            fontSize = 16.sp
                        )

                    )
                }

                Box(
                    Modifier.clickable {
                        pendingColor.value = MyColor.unfocusedIconColor
                        completedColor.value = MyColor.focussedIconColor
                    }
                ) {
                    Text(
                        "Completed",
                        style = TextStyle(
                            color = completedColor.value,
                            fontWeight = (if (completedColor.value == MyColor.focussedIconColor) FontWeight.Medium else FontWeight.Normal),
                            fontSize = 16.sp
                        )

                    )
                }


            }

            if (pendingColor.value == MyColor.focussedIconColor) {

                LazyColumn {

                    items(orders) {
                        if (it.deliveryStatus == "Pending")
                            pendingScreenItem(it,viewModel,userId,{   viewModel.getOrdersByMonth(
                                userId, selectedDate.value,
                                onSuccess = {} ,
                                onFailure = {}
                            )})
                    }
                }


            } else if (completedColor.value == MyColor.focussedIconColor  ) {

                LazyColumn {

                    items(orders) {
                        if (it.deliveryStatus == "Completed"||it.deliveryStatus=="Cancelled")
                            pendingScreenItem(it,viewModel,userId ,{  viewModel.getOrdersByMonth(
                                userId, selectedDate.value,
                                onSuccess = {} ,
                                onFailure = {}
                            )})


                    }

                }


            }


        }

    }



}


@Composable
fun pendingScreenItem(order: Order,viewModel:FireBaseViewModel,userId: String ,onClick:()-> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var orderDeliveryStatus by remember {
        mutableStateOf(order.deliveryStatus)
    }





    Spacer(Modifier.height(5.dp))
    Column(
        Modifier
            .padding(17.dp)
            .background(shape = RoundedCornerShape(20.dp), color = Color.White)
            .padding(8.dp)

    ) {
        Spacer(Modifier.height(15.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                order.orderDate ?: "",
                style = TextStyle(
                    color = Color(0xFF111827),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

            )
            Box(
                Modifier
                    .size(if (orderDeliveryStatus == "Completed") 111.dp else 90.dp, 36.dp)
                    .background(
                        shape = RoundedCornerShape(20.dp),
                        color = if (orderDeliveryStatus == "Completed") Color(
                            0xFF10B981
                        ) else Color(0xFFFFEF3C7)
                    )
                , contentAlignment = Alignment.Center
            ) {
                Text(
                    orderDeliveryStatus ?: "",
                    style = TextStyle(
                        color = (if (orderDeliveryStatus == "Completed") Color(0xFFD1FAE5)else Color(0xFFF59E0B) ),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                )

            }
        }
        Spacer(Modifier.height(7.dp))

        Text(
            order.quantity.toString() ?: "",
            style = TextStyle(
                color = Color(0xFF4B5563),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        )

        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(R.drawable.clockicon),
                null,
                tint = Color(0xFF4B5563),
                modifier = Modifier.size(16.dp)
            )

            Text(
                "${if (orderDeliveryStatus == "Pending") {"Expected"} else if(orderDeliveryStatus == "Completed") "completed " else "Cancelled"}: " +
                        " {$${if(orderDeliveryStatus=="Cancelled")order.expectedDeliveryDate else ""}}",
                style = TextStyle(
                    color = Color(0xFF4B5563),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )

            )
        }
        Spacer(Modifier.height(12.dp))
        Divider(thickness = 1.dp, color = Color.LightGray, modifier = Modifier.padding(5.dp))
        Spacer(Modifier.height(20.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "₹ ${order.totalAmount}",
                style = TextStyle(
                    color = Color(0xFF111827),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

            )

            Box(
                Modifier
                    .size(111.dp, 36.dp)
                    .background(
                        shape = RoundedCornerShape(20.dp),
                        color = if (orderDeliveryStatus == "Pending") Color(0xFFF97316) else Color.White
                    )
                    .border(
                        1.dp,
                        color = if (orderDeliveryStatus == "Pending") Color.White else Color(
                            0xFF2563EB
                        ),
                        RoundedCornerShape(20.dp)
                    )
                    .clickable {
                     showDialog=true;

                    }
                , contentAlignment = Alignment.Center
            ) {
                Text(
                    if (orderDeliveryStatus == "Pending") "Cancel" else "Reorder",
                    style = TextStyle(
                        color = if (orderDeliveryStatus == "Pending") Color.White else Color(0xFF2563EB),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                )

            }

        }
        Spacer(Modifier.height(12.dp))
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Cancellation") },
            text = { Text("Are you sure you want to cancel this order?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        if (orderDeliveryStatus == "Pending") {
                            Log.d("Userid", "first id is  : $userId")
                            viewModel.updateOrderStatus(
                                userId = userId,
                                order.orderId!!,
                                "Cancelled",
                                {
                                    onClick();
                                    Log.d("updated", "successful: ")
                                },
                                {
                                    Log.d("updated", "unsuccessful: ${it.message}")
                                })
                        }
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }


}

@Composable
fun bottomBar(navController: NavController) {
    val homeIconColor = remember { mutableStateOf(MyColor.unfocusedIconColor) }
    val orderIconColor = remember { mutableStateOf(MyColor.unfocusedIconColor) }
    val analyiticIconColor = remember { mutableStateOf(MyColor.unfocusedIconColor) }
    val profileIconColor = remember { mutableStateOf(MyColor.unfocusedIconColor) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround

    ) {

        Box(
            Modifier.clickable {
                homeIconColor.value = MyColor.focussedIconColor
                orderIconColor.value = MyColor.unfocusedIconColor
                analyiticIconColor.value = MyColor.unfocusedIconColor
                profileIconColor.value = MyColor.unfocusedIconColor
                navController.navigate(route.homeScreen)
            },
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.homeicon),
                    contentDescription = null,
                    Modifier.size(24.dp),
                    tint = homeIconColor.value
                )

                Text(
                    "Home",
                    style = TextStyle(
                        color = homeIconColor.value,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                )
            }

        }


        Box(
            Modifier.clickable {
                homeIconColor.value = MyColor.unfocusedIconColor
                orderIconColor.value = MyColor.focussedIconColor
                analyiticIconColor.value = MyColor.unfocusedIconColor
                profileIconColor.value = MyColor.unfocusedIconColor
                navController.navigate(route.orderScreen)
            },
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ordersicon),
                    contentDescription = null,
                    Modifier.size(24.dp),
                    tint = orderIconColor.value
                )

                Text(
                    "Orders",
                    style = TextStyle(
                        color = orderIconColor.value,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                )
            }

        }


        Box(
            Modifier.clickable {
                homeIconColor.value = MyColor.unfocusedIconColor
                orderIconColor.value = MyColor.unfocusedIconColor
                analyiticIconColor.value = MyColor.focussedIconColor
                profileIconColor.value = MyColor.unfocusedIconColor
            },
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.analyticicon),
                    contentDescription = null,
                    Modifier.size(24.dp),
                    tint = analyiticIconColor.value
                )

                Text(
                    "Analytics",
                    style = TextStyle(
                        color = analyiticIconColor.value,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                )
            }

        }


        Box(
            Modifier.clickable {
                homeIconColor.value = MyColor.unfocusedIconColor
                orderIconColor.value = MyColor.unfocusedIconColor
                analyiticIconColor.value = MyColor.unfocusedIconColor
                profileIconColor.value = MyColor.focussedIconColor
            },
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.profileicon),
                    contentDescription = null,
                    Modifier.size(24.dp),
                    tint = profileIconColor.value
                )

                Text(
                    "Profile",
                    style = TextStyle(
                        color = profileIconColor.value,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                )
            }

        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topbar(Title: String, onNavigationBackClick: () -> Unit) {


    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF9FAFB),
        )
        ,

        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Your Order",
                    modifier = Modifier, textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )
            }
        },

        navigationIcon = {
            IconButton(onClick = { onNavigationBackClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null
                )
            }

        }
    )

}
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun orderScreenPreview() {
    val navController = NavController(LocalContext.current) // Create a mock NavController
    val orderManager = OrderManager()
    val analyticsManager = AnalyticsManager()
    val billingAndPaymentManager = BillingAndPaymentManager()
    val userManager = UserManager()
    val canesManager = CanesManager()
    orderScreen(navController, FireBaseViewModel(FireBaseRepository(orderManager, analyticsManager, billingAndPaymentManager, userManager, canesManager)), "userId")
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM")
    return currentDate.format(formatter)
}

val monthList = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)