package com.example.authenticationn.Presentation.Ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.authenticationn.Data.FireStoreDatabase.Models.User
import com.example.authenticationn.Presentation.FireBaseViewModel

@Composable

fun  signUpScreen(navController: NavController,viewModel: FireBaseViewModel){

    val fullName=remember{mutableStateOf("")}
    val phoneNumber=remember{mutableStateOf("")}
    val password=remember{mutableStateOf("")}
    val confirmPassword=remember{mutableStateOf("")}

    Column(modifier=Modifier.fillMaxSize().background(color = Color.White)
        .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start

        ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            "Create Account ",

            color = Color.Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Sign up to get started",
            modifier = Modifier.padding(start=2.dp),
            fontSize = 16.sp,
            color=Color.Gray,
            )

        Spacer(modifier = Modifier.height(30.dp))
        Text("Full Name",
//            modifier = Modifier.padding(start=2.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    Spacer(modifier = Modifier.height(9.dp))



//        text filed for all things


        TextField(value = fullName.value, onValueChange = {
        fullName.value=it },
            modifier = Modifier.fillMaxWidth(),
        label = { Text("Enter your full name") },
        shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color(153, 153, 153, 30), // Changed to unfocusedContainerColor
                focusedContainerColor = Color(153, 153, 153, 30),
                disabledContainerColor = Color(153, 153, 153, 30)
            )
        )


        Spacer(modifier = Modifier.height(20.dp))
        Text("Phone Number",
//            modifier = Modifier.padding(start=2.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(9.dp))
        TextField(value = phoneNumber.value, onValueChange = {
            phoneNumber.value=it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter your phone number") },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color(153, 153, 153, 30), // Changed from backgroundColor
                focusedContainerColor = Color(153, 153, 153, 30),
                disabledContainerColor = Color(153, 153, 153, 30)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text("Password",
//            modifier = Modifier.padding(start=2.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(9.dp))
        TextField(value = password.value, onValueChange = {
            password.value=it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Create Your Password") },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color(153, 153, 153, 30), // Use containerColor
                focusedContainerColor = Color(153, 153, 153, 30),
                disabledContainerColor = Color(153, 153, 153, 30)
            )
        )


        Spacer(modifier = Modifier.height(20.dp))
        Text("Confirm Password",
//            modifier = Modifier.padding(start=2.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(9.dp))
        TextField(value = confirmPassword.value, onValueChange = {
            confirmPassword.value=it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Confirm Your Password") },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color(153, 153, 153, 30),
                focusedContainerColor = Color(153, 153, 153, 30),
                disabledContainerColor = Color(153, 153, 153, 30)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))
val context= LocalContext.current
        Button(
            onClick = {
                viewModel.signUp(
                    user = User(
                        name = fullName.value,
                        email  = phoneNumber.value,
                    ),
                    password = password.value,

                    onSuccess = {
                        navController.popBackStack()
                        navController.navigate(route.signInScreen)

                    },
                    onFailure = { exception ->
                        Toast.makeText(context,"User already exist",Toast.LENGTH_LONG).show()

                    }
                )


            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MyColor.darkBlue, // Enabled background
                contentColor = Color.White, // Enabled text/icon color
                disabledContainerColor = Color.Gray, // Disabled background
                disabledContentColor = Color.DarkGray // Disabled text/icon color
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(24.dp).size(
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




