


import android.os.Build
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.authenticationn.Data.FireStoreDatabase.managers.BillingAndPaymentManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.CanesManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.OrderManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.UserManager
import com.example.authenticationn.Domain.FireBaseRepository
import com.example.authenticationn.Presentation.FireBaseViewModel
import com.example.authenticationn.Presentation.Ui.MyColor
import com.example.authenticationn.Presentation.Ui.bottomBar
import com.example.authenticationn.Presentation.Ui.route
import com.example.authenticationn.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun placeOrderScreen (navController: NavController, viewModel: FireBaseViewModel, userId:String){
    val  quantity= remember { mutableStateOf(0) }
    val  returningCane= remember { mutableStateOf(0) }
    val  isNormalWaterSelected= remember { mutableStateOf(true) }
    val  isColdWaterSelected= remember { mutableStateOf(false) }
    val normalWaterColor=  remember { mutableStateOf(MyColor.focusedWaterColor) }
    val coldWaterColor= remember { mutableStateOf(MyColor.unfocusedWaterColor) }
    var selectedItem by remember { mutableStateOf(0) }
    val totalPrice= remember { mutableStateOf(0) }


    Scaffold(

        bottomBar = {
            bottomBar(navController)

//                modifier = Modifier
//                    .height(70.dp)
//                    .background(MyColor.primary),
//                containerColor = MyColor.primary,
//                contentPadding = BottomAppBarDefaults.ContentPadding
//            ) {
//                items.forEachIndexed { index, item ->
//                    Row(
//                        Modifier.weight(1f),
//                        verticalAlignment = Alignment.CenterVertically,
//
//                        ){
//                        NavigationBarItem(
//
//
//                            icon = {
//                                Icon(
//                                    item.icon,
//                                    contentDescription = item.title,
//                                    tint = Color.White
//                                )
//                            },
//                            label = { Text(item.title, color = Color.White) },
//                            selected = selectedItem == index,
//                            onClick = { selectedItem = index },
//                            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
//                                indicatorColor = MyColor.secondary
//                            )
//
//                        )
//                    }
//
//                }
//            }
        }
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MyColor.backGroundColor),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null
                )


                Text(
                    "Place Order",
                    modifier = Modifier
                        .weight(1F)
                        .padding(), textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )

            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 2.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(17.dp))



            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {


                Text(
                    "Select Water Type",
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    Box(
                        modifier = Modifier
                            .size(
                                width = 171.dp,
                                height = 152.dp
                            )
                            .border(
                                2.dp,
                                color = normalWaterColor.value, RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                isNormalWaterSelected.value = true
                                isColdWaterSelected.value = false
                                normalWaterColor.value = MyColor.focusedWaterColor
                                coldWaterColor.value = MyColor.unfocusedWaterColor

                            },

                        ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Spacer(modifier = Modifier.height(20.dp))
                            Icon(
                                painter = painterResource(R.drawable.normalwaterlogo),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = normalWaterColor.value,

                                )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Normal Water",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "₹ 30 ", style = TextStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            )

                        }


                    }


                    Box(
                        modifier = Modifier
                            .size(171.dp, 152.dp)
                            .border(2.dp, color = coldWaterColor.value, RoundedCornerShape(10.dp))
                            .clickable {
                                isNormalWaterSelected.value = false
                                isColdWaterSelected.value = true
                                normalWaterColor.value = MyColor.unfocusedWaterColor
                                coldWaterColor.value = MyColor.focusedWaterColor
                            },

                        ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Spacer(modifier = Modifier.height(20.dp))
                            Icon(
                                painter = painterResource(R.drawable.coldwaterlogo),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = coldWaterColor.value,

                                )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Cold Water",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "₹ 35 ", style = TextStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            )

                        }


                    }
                }

                Spacer(modifier = Modifier.height(30.dp))


                Text(
                    "Quantity",
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MyColor.backGroundColor),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,


                        ) {

                        IconButton(onClick = {
                            if (quantity.value > 0) {
                                quantity.value--
                            }
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.minuslogo),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.Black
                            )

                        }

                        Text(
                            "${quantity.value}",
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                        )

                        IconButton(onClick = {
                            quantity.value++
                        }) {


                            Icon(
                                painter = painterResource(R.drawable.addicon),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.Black
                            )

                        }


                    }
                }




                Spacer(modifier = Modifier.height(30.dp))


                Text(
                    "Returning Cane",
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MyColor.backGroundColor),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,


                        ) {

                        IconButton(onClick = {
                            if (returningCane.value > 0) {
                                returningCane.value--
                            }
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.minuslogo),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.Black
                            )

                        }

                        Text(
                            "${returningCane.value}",
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                        )

                        IconButton(onClick = {
                            returningCane.value++
                        }) {

                            Icon(
                                painter = painterResource(R.drawable.addicon),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.Black
                            )

                        }

                    }
                }



                Spacer(modifier = Modifier.height(30.dp))


                Text(
                    "Schedule Delivery",
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .size(50.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MyColor.backGroundColor),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,


                        ) {
                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            painter = painterResource(R.drawable.calendericon),
                            contentDescription = null,
                            modifier = Modifier.size(21.dp),
                            tint = Color(0xFF4B5563)
                        )

                        Spacer(modifier = Modifier.padding(start = 10.dp))

                        Text(
                            "Today, 2:00 PM",
                            style = TextStyle(
                                color = Color(0xFF212121),
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.padding(start = 183.dp))

                        Icon(
                            painter = painterResource(R.drawable.forwardicon),
                            contentDescription = null,
                            modifier = Modifier.size(11.5.dp)
                        )


                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {

                    Text(
                        "Set up recurring delivery",
                        style = TextStyle(
                            color = Color(0xFF212121),
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(40.dp, 24.dp)
                            .background(
                                shape = RoundedCornerShape(20.dp),
                                color = MyColor.backGroundColor
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.selectedcircleicon),
                            contentDescription = "Toggle",
                            tint = Color(0xFFFFFFFF), // Change color
                            modifier = Modifier.size(25.dp)
                        )

                    }


                }


                Text(
                    "Save time by scheduling regular delivery",
                    modifier = Modifier.padding(start = 8.dp)
                )


                Spacer(modifier = Modifier.height(25.dp))




                Column(
                    Modifier
                        .fillMaxSize()
                        .background(MyColor.backGroundColor, shape = RoundedCornerShape(10.dp))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start


                ) {
                    Spacer(modifier = Modifier.padding(top = 10.dp))

                    Text("Order Summery")
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {
                        Text("${if (isNormalWaterSelected.value) "Normal Water" else "Cold Water"}(${quantity.value})")
                        Text("₹ ${(if (isNormalWaterSelected.value) 30 else 35) * quantity.value}.00")

                    }
                    Spacer(modifier = Modifier.padding(top = 10.dp))


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {
                        Text("Delivery Fee")
                        Text("₹ 0.00")

                    }
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {
                        Text("Cane Returning ")
                        Text("${returningCane.value}")

                    }

                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    HorizontalDivider(
                        modifier = Modifier.padding(10.dp),
                        thickness = 2.dp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {
                        Text("Total")
                        totalPrice.value = (if (isNormalWaterSelected.value) 30 else 35) * quantity.value
                        Text("₹ ${totalPrice.value } ")

                    }


                }

                Spacer(modifier = Modifier.height(25.dp))
                val currentDate = LocalDate.now()
                val currentMillis = currentDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
                val initialDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentMillis), ZoneOffset.UTC)
                val initialDate = Date.from(initialDateTime.toInstant(ZoneOffset.UTC))
                Button(
                    onClick = {

                        val newOrder = Order(
                             userName = viewModel.user.value?.name?:"no name",
                            userID = FirebaseFirestore.getInstance().collection("Users")
                                .document(userId),
                            waterType = if (isNormalWaterSelected.value) "Normal" else "Cold",
                            quantity = quantity.value,
                            canesReturning = returningCane.value,
                            orderNumber = generateOrderNumber(),
                            deliveryStatus = "Pending",
                            deliveryAddress = viewModel.user.value?.address,
                            totalAmount =totalPrice.value.toDouble(),
                            isDelivered = false,
                            expectedDeliveryDate = Timestamp(initialDate),

                        )
                         if(quantity.value>0){
                             viewModel.createOrder(userId,newOrder,onSuccess = {

                                 navController.navigate(route.homeScreen)
                                 Toast.makeText(navController.context, "Your ${it} has been placed successfully ", Toast.LENGTH_SHORT).show()
                             }, onFailure = {
                                 Toast.makeText(navController.context, "Error creating order ", Toast.LENGTH_SHORT).show()
                             })
                         }


                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(shape = RoundedCornerShape(20.dp), color = Color.Transparent),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyColor.green,
                        contentColor = Color.White,
                    )
                ) {
                    Text(
                        "Place Order",
                        textAlign = TextAlign.Center,
                    )
                }


            }


        }


    }

}
fun generateOrderNumber(): String {

    val canesManager = CanesManager()
    val userManager = UserManager()
    val billingAndPaymentManager = BillingAndPaymentManager()
    val analyticsManager = AnalyticsManager()
    val orderManager = OrderManager()
    val firebaseRepository = FireBaseRepository(
        orderManager,
        analyticsManager,
        billingAndPaymentManager,
        userManager,
        canesManager,

    )
    val timestamp = System.currentTimeMillis()
    return "ORDER-$timestamp"
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun placeOrderScreenPreview() {
    val canesManager = CanesManager()
    val firebaseRepository = FireBaseRepository(OrderManager(),AnalyticsManager(),BillingAndPaymentManager(),UserManager(),canesManager)
    val viewModel = FireBaseViewModel(firebaseRepository)
    placeOrderScreen(navController = NavController(androidx.compose.ui.platform.LocalContext.current), viewModel =viewModel , userId = "Sample user ID")
}





