package com.example.authenticationn.Presentation.Ui//package com.example.authenticationn.ui.theme.Presentation.Ui



import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.authenticationn.Presentation.FireBaseViewModel
import com.example.authenticationn.R



@Composable
fun loginScreen(navController: NavController,viewModel: FireBaseViewModel){

    val phoneNumber=remember{mutableStateOf("")}
    val password=remember{mutableStateOf("")}
    Column(modifier=Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    MyColor.lightBlue,
                    Color(0xFFA0D0FF), // Another color in the middle
                    MyColor.darkerBlue
                )
            )
        )
        , horizontalAlignment = Alignment.CenterHorizontally
        , verticalArrangement = Arrangement.Top

    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(painter = painterResource(R.drawable.frame),
            contentDescription = null,
            modifier=Modifier.size(80.dp)

        )
        Spacer(modifier = Modifier.height(60.dp))
        Text("Phone Number",Modifier
            .align(alignment = Alignment.Start)
            .padding(start = 16.dp),
            style = TextStyle(
                fontSize = 16.sp,
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(end = 16.dp)
                .padding(start = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = cardElevation(defaultElevation = 20.dp)

            ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = phoneNumber.value,
                onValueChange = {
                    phoneNumber.value = it
                },
                label = { Text("Enter your phone number") },
                colors= TextFieldDefaults.colors(
                    focusedLabelColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White

                )
            )
        }


        Spacer(modifier = Modifier.height(30.dp))
        Text("Password",Modifier
            .align(alignment = Alignment.Start)
            .padding(start = 16.dp),
            style = TextStyle(
                fontSize = 16.sp,
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        Card( modifier = Modifier .fillMaxWidth()
            .align(alignment = Alignment.Start)
            .padding(end = 16.dp)
            .padding(start = 16.dp)
           ,
            shape = RoundedCornerShape(20.dp),
            elevation = cardElevation(defaultElevation = 20.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                label = { Text("Enter your password") },
                colors= TextFieldDefaults.colors(
                    focusedLabelColor = Color.Black,
                   focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White

                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        val context=LocalContext.current
        Button(
            onClick = {
                viewModel.signIn(
                    email = phoneNumber.value,
                    password = password.value,
                    onSuccess = {
                        it->
                      Toast.makeText(context,"Login Successful",Toast.LENGTH_LONG).show()
                        navController.navigate(route.homeScreen)
                         viewModel.updateUserId(it)
                    },
                    onFailure = { exception ->
                        Toast.makeText(context,"Incorrect Login/Password ",Toast.LENGTH_LONG).show()
                    }
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MyColor.green, // Use containerColor for Material 3
                contentColor = Color.White, // Enabled text/icon color
                disabledContainerColor = Color.Gray, // Use disabledContainerColor for Material 3
                disabledContentColor = Color.DarkGray // Disabled text/icon color
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            enabled = true // You can add a condition to enable/disable it.
        ) {
            Text(
                "Login",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White,
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Text("Forgot Password?",
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )

        )




        Row (modifier=Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){

            Button(
                onClick = {
                    navController.navigate(route.singUpScreen)


                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MyColor.darkBlue, // Enabled background
                    contentColor = Color.White,       // Enabled text/icon color
                    disabledContainerColor = Color.Gray, // Disabled background
                    disabledContentColor = Color.DarkGray // Disabled text/icon color
                ),
                modifier = Modifier
                    .padding(24.dp)
                    .size(
                        width = 160.dp,
                        height = 50.dp
                    ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Sign Up",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.White,
//                            fontWeight = FontWeight.SemiBold
                    )

                )
            }

            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MyColor.darkBlue, // Enabled background
                    contentColor = Color.White,       // Enabled text/icon color
                    disabledContainerColor = Color.Gray, // Disabled background
                    disabledContentColor = Color.DarkGray // Disabled text/icon color
                ),
                modifier = Modifier
                    .padding(24.dp)
                    .size(
                        width = 160.dp,
                        height = 50.dp
                    ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Sign Up",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.White,
//                            fontWeight = FontWeight.SemiBold
                    )

                )
            }

        }





    }


}



