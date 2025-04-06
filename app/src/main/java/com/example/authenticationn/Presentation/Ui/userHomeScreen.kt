package com.example.authenticationn.Presentation.Ui



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.authenticationn.Presentation.FireBaseViewModel
import com.example.authenticationn.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun userHomeScreen(navController: NavController,viewModel: FireBaseViewModel,userId: String) {
    val user=mutableStateOf(viewModel.user.value)
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            bottomBar(navController)
//            BottomAppBar(
//                modifier = Modifier
//                    .height(70.dp)
//                    .background(MyColor.primary),
//                containerColor = MyColor.primary,
//                contentPadding = BottomAppBarDefaults.ContentPadding
//            ) {
//                items.forEachIndexed { index, item ->
//                    Row (Modifier.weight(1f),
//                  verticalAlignment = Alignment.CenterVertically,
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
//                            onClick = { selectedItem = index
//                                      if(item.title=="Orders"){
//                                          navController.navigate(route.placeOrderScreen)
//                                      }
//                                      else if(item.title=="Home"){
//                                          navController.navigate(route.homeScreen)
//                                      }
//
//                                      },
//                            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
//                                indicatorColor = MyColor.secondary
//                            )
//
//                        )
//                    }
//
//                }
//            }
        },
        containerColor = MyColor.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TopBar(user.value.canesTaken)
            Spacer(modifier = Modifier.height(24.dp))
            MonthlyUsageChart(
                monthlyData = listOf(
                    4, 4, 4, 4, 4, 4
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            OrderAndBillsButtons(navController)
            Spacer(modifier = Modifier.height(24.dp))
            TodaysDeliveryCard()
        }
    }
}




@Composable
fun TopBar(canesTaken: Int?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hi, Michael",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MyColor.textPrimary
                )
            )
            Text(
                text = "${canesTaken?:0} jars ordered this month",
                style = TextStyle(fontSize = 16.sp, color = MyColor.textSecondary)
            )
        }
//        Image( // Use the Image composable
//            painter = painterResource(id = R.drawable.profile),
//            contentDescription = "Profile",
//            modifier = Modifier
//                .size(60.dp)
//                .clip(CircleShape) // Use the clip modifier
//        )
    }
}


@Composable
fun MonthlyUsageChart(monthlyData: List<Int>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Monthly Usage",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MyColor.textPrimary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            val maxUsage = monthlyData.maxOrNull() ?: 0
            val months = listOf("Sep", "Oct", "Nov", "Dec", "Jan", "Feb")
            monthlyData.forEachIndexed { index, usage ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    val barHeight = if (maxUsage > 0) (usage.toFloat() / maxUsage.toFloat()) else 0f

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .fillMaxHeight(barHeight)
                            .background(MyColor.primary, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Text(
                        text = months[index],
                        style = TextStyle(fontSize = 12.sp),
                        color = MyColor.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MonthlyUsageChartPreview() {
    MonthlyUsageChart(monthlyData = listOf(4, 3, 5, 4, 5, 5))
}

@Composable
fun OrderAndBillsButtons(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Order Water Button
        Card(
            modifier = Modifier
                .weight(1f)
                .height(100.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MyColor.primary),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            onClick = {
                navController.navigate(route.placeOrderScreen)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.water),
                    contentDescription = "Order Water",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Order Water",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = "Quick order",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

        // View Bills Button
        Card(
            modifier = Modifier
                .weight(1f)
                .height(100.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MyColor.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bill),
                    contentDescription = "View Bills",
                    tint = MyColor.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "View Bills",
                    color = MyColor.textPrimary,
                    style = TextStyle(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = "Payment history",
                    color = MyColor.textSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}



@Composable
fun TodaysDeliveryCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MyColor.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            ) {
            Text(
                text = "Today's Delivery",
                style = TextStyle(fontWeight = FontWeight.Bold, color = MyColor.textPrimary)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Expected: 2:00 PM - 4:00 PM",
                style = TextStyle(fontSize = 14.sp, color = MyColor.textSecondary)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(Color.LightGray, RoundedCornerShape(5.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(10.dp)
                        .background(MyColor.primary, RoundedCornerShape(5.dp))
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Order ID: #WD12345",
                    style = TextStyle(fontSize = 14.sp, color = MyColor.textSecondary)
                )
                Text(
                    text = "On the way",
                    color = MyColor.primary,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

@Preview
@Composable
fun TodaysDeliveryCardPreview() {
    TodaysDeliveryCard()
}

data class NavigationItem(
    val title: String,
    val icon: ImageVector
)