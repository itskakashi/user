package com.example.authenticationn.Presentation.Ui

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.authenticationn.Data.FireStoreDatabase.Models.Bill
import com.example.authenticationn.Presentation.FireBaseViewModel
import com.example.authenticationn.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.Date
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.time.Month
import java.time.Year
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillScreen(
    navController: NavController,
    viewModel: FireBaseViewModel = koinInject(),
    userId: String
) {
    // Collect states from the ViewModel
    val bills by viewModel.bills.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()

    // The current user id can be empty or equal to the id send in parameters
    LaunchedEffect(key1 = userId, key2 = selectedMonth, key3 = selectedYear) {
        if (userId.isNotEmpty()) {
            viewModel.setUserid(userId)
            viewModel.getBillsForMonthAndYear(userId, selectedMonth, selectedYear)
            val db: FirebaseFirestore = Firebase.firestore
            // Check if there are no bills for the user, then create a test bill
            if (bills.isEmpty()) {
                val testBill = Bill(
                    billId = "testBill_${userId}_${System.currentTimeMillis()}",
                    userId = db.collection("users").document(userId),
                    amount = 150.0,
                    totalJars = 10,
                    billDate = Timestamp(java.util.Date()),
                    paymentStatus = "Unpaid",
                    isPaid = false
                )

                viewModel.createBill(testBill,
                    onSuccess = {
                        println("test bill is added")
                    },
                    onFailure = { e ->
                        println("test bill is failure ${e.message}")

                    })
            }
        }
    }


    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                ),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Your Bills",
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF9FAFB))
        ) {
            MonthYearSelector(
                selectedMonth = selectedMonth,
                selectedYear = selectedYear,
                onMonthSelected = { viewModel.updateSelectedMonth(it) },
                onYearSelected = { viewModel.updateSelectedYear(it) }
            )

            if (isLoading) {
                // Show loading indicator
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading...")
                }
            } else if (error != null) {
                // Show error message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $error")
                }
            } else {
                // Show data
                BillSummarySection(
                    bills, viewModel, userId,
                    onFailure = { e ->
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    })
                Spacer(modifier = Modifier.height(24.dp))
                ItemizedDetailsSection(
                    bills, viewModel, userId,
                    onFailure = { e ->
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    })
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthYearSelector(
    selectedMonth: Month,
    selectedYear: Year,
    onMonthSelected: (Month) -> Unit,
    onYearSelected: (Year) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { showDialog = true }, // Open dialog on click
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${selectedMonth.name} ${selectedYear.value}",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "Select Month/Year",
            tint = Color.Black
        )
    }

    if (showDialog) {
        MonthYearPickerDialog(
            onDismiss = { showDialog = false },
            onMonthYearSelected = { month, year ->
                onMonthSelected(month)
                onYearSelected(year)
                showDialog = false
            },
            selectedMonth = selectedMonth,
            selectedYear = selectedYear
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthYearPickerDialog(
    onDismiss: () -> Unit,
    onMonthYearSelected: (Month, Year) -> Unit,
    selectedMonth: Month,
    selectedYear: Year
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Month and Year") },
        text = {
            Column {
                MonthPicker(
                    selectedMonth = selectedMonth,
                    onMonthSelected = { month ->
                        onMonthYearSelected(month, selectedYear)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                YearPicker(
                    selectedYear = selectedYear,
                    onYearSelected = { year ->
                        onMonthYearSelected(selectedMonth, year)
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthPicker(selectedMonth: Month, onMonthSelected: (Month) -> Unit) {
    LazyColumn {
        items(Month.values()) { month ->
            Text(
                text = month.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMonthSelected(month) }
                    .padding(8.dp),
                color = if (month == selectedMonth) Color.Blue else Color.Black // Highlight selected month
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YearPicker(selectedYear: Year, onYearSelected: (Year) -> Unit) {
    LazyColumn {
        items((2020..2030).toList()) { year ->
            Text(
                text = year.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onYearSelected(Year.of(year)) }
                    .padding(8.dp),
                color = if (Year.of(year) == selectedYear) Color.Blue else Color.Black // Highlight selected year
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BillSummarySection(
    bills: List<Bill>,
    viewModel: FireBaseViewModel,
    userId: String,
    onFailure: (e: Exception) -> Unit
) {
    // Calculate total jars and total amount from the bills
    val totalJars = bills.sumOf { it.totalJars ?: 0 }
    val totalAmount = bills.sumOf { it.amount ?: 0.0 }
    val paymentStatus = if (bills.isNotEmpty()) bills.first().paymentStatus else "unpaid"
    val colorStatus = if (paymentStatus == "Unpaid") Color.Red else Color.Green
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Total Jars Delivered",
            style = TextStyle(
                color = Color.Gray,
                fontSize = 14.sp
            )
        )
        Text(
            text = "$totalJars",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Total Amount",
            style = TextStyle(
                color = Color.Gray,
                fontSize = 14.sp
            )
        )
        Text(
            text = "₹${totalAmount}",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Payment Status",
            style = TextStyle(
                color = Color.Gray,
                fontSize = 14.sp
            )
        )
        Text(
            text = paymentStatus.toString(),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = colorStatus
            )
        )
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ButtonWithIcon(
                label = "Download PDF",
                icon = R.drawable.downloadicon,
                onClick = { /*TODO*/ })
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                // Handle pay now for all bills in the month
                viewModel.markAllBillsAsPaidForMonth(
                    userId,
                    viewModel.selectedMonth.value,
                    viewModel.selectedYear.value,
                    onSuccess = {
                        println("pay all  now is success")
                    },
                    onFailure = { e ->
                        onFailure(e)
                        println("pay all  now is failure ${e.message}")
                    })
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(MyColor.focussedIconColor, shape = RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = MyColor.focussedIconColor)
        ) {
            Text(
                text = "Pay Now",
                style = TextStyle(color = Color.White, fontSize = 16.sp)
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemizedDetailsSection(
    bills: List<Bill>,
    viewModel: FireBaseViewModel,
    userId: String,
    onFailure: (e: Exception) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            text = "Itemized Details",
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = Color.LightGray,
            thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
        if(bills.isEmpty()){
            Text(text = "There are no bills for this month", modifier = Modifier.padding(16.dp))
        }
        else{
            LazyColumn {
                items(bills) { bill ->
                    BillItem(bill, userId, viewModel,onFailure)
                }
            }
        }

    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BillItem(bill: Bill, userId: String, viewModel: FireBaseViewModel,onFailure: (e: Exception) -> Unit) {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val formattedDate = bill.billDate?.toDate()?.let { formatter.format(it) } ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Bill Date: $formattedDate",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Amount: ₹${bill.amount}", style = TextStyle(fontSize = 14.sp))
            }
            Button(
                onClick = {
                    viewModel.markBillAsPaidForUser(
                        userId,
                        bill.billId!!,
                        onSuccess = {
                            println("pay one bill is success")
                        },
                        onFailure = { e ->
                            onFailure(e)
                            println("pay one  bill is failure ${e.message}")
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (bill.isPaid == true) Color.Green else Color.Red
                )
            ) {
                Text(
                    text = if (bill.isPaid == true) "Paid" else "Pay",
                    style = TextStyle(color = Color.White)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.LightGray,
            thickness = 1.dp
        )
    }
}
@Composable
fun ButtonWithIcon(label: String, icon: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD8D8D8), shape = RoundedCornerShape(8.dp)), // Light Gray background
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD8D8D8)) // Ensure color consistency
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
            Text(text = label)
        }
    }
}