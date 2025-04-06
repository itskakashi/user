package com.example.authenticationn.Presentation.Ui

import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.authenticationn.R
import com.example.authenticationn.Data.FireStoreDatabase.Models.Order
import com.example.authenticationn.Presentation.FireBaseViewModel
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
            onSuccess ={} ,
            onFailure ={}
        )
    }

    val orders by viewModel.getOrdersByMonth.observeAsState(emptyList())

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
                                        color = Color(0xFF2C55E),
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
                            pendingScreenItem(it)
                    }
                }


            } else if (completedColor.value == MyColor.focussedIconColor) {

                LazyColumn {

                    items(orders) {
                        it->
                        if (it.deliveryStatus == "Completed")
                            pendingScreenItem(it)
                    }

                }


            }


        }

    }

}


@Composable
fun pendingScreenItem(order: Order) {

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
                    .size(if (order.deliveryStatus == "Completed") 111.dp else 90.dp, 36.dp)
                    .background(
                        shape = RoundedCornerShape(20.dp),
                        color = if (order.deliveryStatus == "Completed") Color(0xFF10B981) else Color(0xFFFFEF3C7)
                    )
                , contentAlignment = Alignment.Center
            ) {
                Text(
                    order.deliveryStatus ?: "",
                    style = TextStyle(
                        color = (if (order.deliveryStatus == "Completed") Color(0xFFD1FAE5) else Color(0xFFF59E0B)),
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
                "${if (order.deliveryStatus == "Pending") "Expected" else "completed "}: " +
                        " ${order.expectedDeliveryDate}",
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
                        color = if (order.deliveryStatus == "Pending") Color(0xFFF97316) else Color.White
                    )
                    .border(
                        1.dp,
                        color = if (order.deliveryStatus == "Completed") Color(0xFF2563EB) else Color.White,
                        RoundedCornerShape(20.dp)
                    )
                , contentAlignment = Alignment.Center
            ) {
                Text(
                    if (order.deliveryStatus == "Pending") "Cancel" else "Reorder",
                    style = TextStyle(
                        color = if (order.deliveryStatus == "Pending") Color.White else Color(0xFF2563EB),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                )

            }

        }
        Spacer(Modifier.height(12.dp))
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
fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM")
    return currentDate.format(formatter)
}

val monthList = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)