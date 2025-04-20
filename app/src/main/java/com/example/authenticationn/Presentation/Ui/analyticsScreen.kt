package com.example.authenticationn.Presentation.Ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.authenticationn.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.YearMonth
import java.time.Year
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController) {
    var selectedTimePeriod by remember { mutableStateOf(TimePeriod.Weekly) }
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedYear by remember { mutableStateOf(Year.now()) }
    var chartData by remember { mutableStateOf(getWaterConsumptionData(selectedTimePeriod,selectedMonth)) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                ),
                title = {
                    Text(
                        "Usage Analytics",
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
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
        bottomBar = {bottomBar(navController)}
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF9FAFB)),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                DropdownMenuWithLabel(
                    selectedTimePeriod = selectedTimePeriod,
                    onTimePeriodSelected = { newTimePeriod ->
                        selectedTimePeriod = newTimePeriod
                        chartData = getWaterConsumptionData(selectedTimePeriod,selectedMonth)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (selectedTimePeriod == TimePeriod.Monthly) {
                    MonthSelectionRow(
                        selectedMonth = selectedMonth,
                        onMonthChanged = { newMonth ->
                            selectedMonth = newMonth
                            chartData = getWaterConsumptionData(selectedTimePeriod,selectedMonth)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if(selectedTimePeriod == TimePeriod.Yearly){
                    YearSelectionRow(selectedYear = selectedYear, onYearChanged = {newYear ->
                        selectedYear = newYear
                        chartData = getWaterConsumptionData(selectedTimePeriod,selectedMonth,selectedYear)

                    })
                    Spacer(modifier = Modifier.height(16.dp))
                }
                WaterConsumptionChart(data = chartData,selectedTimePeriod)
                Spacer(modifier = Modifier.height(24.dp))
                StatisticsSection()
                Spacer(modifier = Modifier.height(24.dp))
                SmartInsightsSection()
            }
        }
    }
}

@Composable
fun DropdownMenuWithLabel(selectedTimePeriod: TimePeriod, onTimePeriodSelected: (TimePeriod) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val items = TimePeriod.values().toList()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = selectedTimePeriod.label, modifier = Modifier.padding(end = 8.dp))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Dropdown",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { expanded = true },
                tint = Color.Gray
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(120.dp)
        ) {
            items.forEach { timePeriod ->
                DropdownMenuItem(onClick = {
                    onTimePeriodSelected(timePeriod)
                    expanded = false
                }, text = { Text(text = timePeriod.label) })
            }
        }
    }
}
enum class TimePeriod(val label: String) {
    Weekly("Weekly"),
    Monthly("Monthly"),
    Yearly("Yearly")
}

@Composable
fun WaterConsumptionChart(data: List<Float>,selectedTimePeriod: TimePeriod) {
    val primaryColor = Color(0xFF2196F3)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Water Consumption",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            )
            Text(text =  when(selectedTimePeriod){
                TimePeriod.Weekly -> "Last 7 Days"
                TimePeriod.Monthly -> "Last Month"
                TimePeriod.Yearly -> "Last Year"
            }, style = TextStyle(fontSize = 14.sp, color = Color.Gray))
            Spacer(modifier = Modifier.height(16.dp))
            LineChart(data = data,selectedTimePeriod)
        }
    }
}

@Composable
fun LineChart(data: List<Float>,selectedTimePeriod: TimePeriod) {
    val maxValue = data.maxOrNull() ?: 0f
    val primaryColor = Color(0xFF2196F3)
    val points = data.mapIndexed { index, value ->
        val x = (index.toFloat() / (data.size - 1))
        val y = 1f - (value / (if (maxValue == 0f) 1f else maxValue))
        Offset(x, y)
    }
    val days = when (selectedTimePeriod) {
        TimePeriod.Weekly -> listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        TimePeriod.Monthly -> (1..data.size).map { it.toString() }
        TimePeriod.Yearly -> (1..data.size).map { "M$it" }

    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path()
            val chartWidth = size.width
            val chartHeight = size.height

            points.forEachIndexed { index, offset ->
                val x = offset.x * chartWidth
                val y = offset.y * chartHeight

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }

                drawCircle(
                    color = primaryColor,
                    radius = 5.dp.toPx(),
                    center = Offset(x, y)
                )

                if (index < points.size - 1) {
                    // Draw vertical lines for each point
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(x, 0f),
                        end = Offset(x, chartHeight),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }

            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 3.dp.toPx())
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            days.forEach { day ->
                Text(text = day, style = TextStyle(fontSize = 12.sp, color = Color.Gray))
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.TopStart),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0..8 step 2) {
                Text(text = "$i", style = TextStyle(fontSize = 12.sp, color = Color.Gray))
            }
        }
    }
}

@Composable
fun StatisticsSection() {
    val stats = listOf(
        StatisticItem(R.drawable.water, "24", "Total Jars"),
        StatisticItem(R.drawable.money, "$48", "Amount Spent"),
        StatisticItem(R.drawable.graph, "6", "Weekly Avg")
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(stats) { stat ->
            StatisticCard(stat)
        }
    }
}

@Composable
fun StatisticCard(item: StatisticItem) {
    val primaryColor = Color(0xFF2196F3)
    Card(
        modifier = Modifier.size(width = 110.dp, height = 100.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = item.label,
                modifier = Modifier.size(24.dp),
                tint = primaryColor
            )
            Text(text = item.value, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
            Text(text = item.label, style = TextStyle(fontSize = 12.sp, color = Color.Gray))
        }
    }
}

@Composable
fun SmartInsightsSection() {
    val insights = listOf(
        InsightItem(
            R.drawable.calender,
            "Your usage peaks on Mondays"
        ),
        InsightItem(
            R.drawable.lightening,
            "You could save $12 with a monthly plan"
        ),
        InsightItem(
            R.drawable.money,
            "Usage is 20% higher than last week"
        )
    )

    Column {
        Text(
            text = "Smart Insights",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        insights.forEach { insight ->
            InsightCard(insight)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun InsightCard(item: InsightItem) {
    val primaryColor = Color(0xFF2196F3)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = item.insight,
                modifier = Modifier.size(24.dp),
                tint = primaryColor
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = item.insight, style = TextStyle(fontSize = 14.sp))
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthSelectionRow(selectedMonth: YearMonth, onMonthChanged: (YearMonth) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val previousMonth = selectedMonth.minusMonths(1)
        Button(
            onClick = { onMonthChanged(previousMonth) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
        ) {
            Text(text = "<", color = Color.Blue)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(16.dp))
        val nextMonth = selectedMonth.plusMonths(1)
        Button(
            onClick = { onMonthChanged(nextMonth) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
        ) {
            Text(text = ">", color = Color.Blue)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YearSelectionRow(selectedYear: Year, onYearChanged: (Year) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val previousYear = selectedYear.minusYears(1)
        Button(
            onClick = { onYearChanged(previousYear) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
        ) {
            Text(text = "<", color = Color.Blue)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = selectedYear.value.toString(),
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(16.dp))
        val nextYear = selectedYear.plusYears(1)
        Button(
            onClick = { onYearChanged(nextYear) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier.clip(RoundedCornerShape(10.dp))

        ) {
            Text(text = ">", color = Color.Blue)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getWaterConsumptionData(timePeriod: TimePeriod, yearMonth: YearMonth = YearMonth.now(), year: Year = Year.now()): List<Float> {
    return when (timePeriod) {
        TimePeriod.Weekly -> listOf(4f, 3f, 5f, 2f, 4f, 6f, 3f) // Example data for a week
        TimePeriod.Monthly -> (1..yearMonth.lengthOfMonth()).map { it.toFloat() % 7 + 1 }
        TimePeriod.Yearly -> (1..12).map { (it % 5).toFloat() + 3 }
    }
}
data class StatisticItem(val icon: Int, val value: String, val label: String)
data class InsightItem(val icon: Int, val insight: String)


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun UsageAnalyticsScreenPreview() {
    AnalyticsScreen(navController = rememberNavController())
}