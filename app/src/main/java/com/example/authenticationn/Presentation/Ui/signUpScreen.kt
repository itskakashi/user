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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.authenticationn.Data.FireStoreDatabase.Models.User
import com.example.authenticationn.Presentation.FireBaseViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun signUpScreen(navController: NavController, viewModel: FireBaseViewModel) {

    val fullName = remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isPasswordMatch by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
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
        Text(
            "Sign up to get started",
            modifier = Modifier.padding(start = 2.dp),
            fontSize = 16.sp,
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            "Full Name",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(9.dp))

        // Full Name Text Field
        TextField(
            value = fullName.value,
            onValueChange = { fullName.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter your full name") },
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
        Text(
            "Phone Number",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(9.dp))
        TextField(
            value = phoneNumber,
            onValueChange = {
                // Allow only numeric input for phone number

                    phoneNumber = it

            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter your phone number") },
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
        Text(
            "Password",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(9.dp))
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Create Your Password") },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color(153, 153, 153, 30),
                focusedContainerColor = Color(153, 153, 153, 30),
                disabledContainerColor = Color(153, 153, 153, 30)
            ),
//            trailingIcon = {
//                val icon = if (passwordVisible)
//                    Icons.Filled.Visibility
//                else
//                    Icons.Filled.VisibilityOff
//                IconButton(onClick = {
//                    passwordVisible = !passwordVisible
//                }) {
//                    Icon(icon, contentDescription = "Visibility")
//                }
//            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "Confirm Password",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(9.dp))
        TextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it
                isPasswordMatch = password.value == confirmPassword.value},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Confirm Your Password") },
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color(153, 153, 153, 30),
                focusedContainerColor = Color(153, 153, 153, 30),
                disabledContainerColor = Color(153, 153, 153, 30)
            ),
//            trailingIcon = {
//                val icon = if (confirmPasswordVisible)
////                    Icons.Filled.Visibility
//                else
////                    Icons.Filled.VisibilityOff
//                IconButton(onClick = {
//                    confirmPasswordVisible = !confirmPasswordVisible
//                }) {
//                    Icon(icon, contentDescription = "Visibility")
//                }
//            }
        )
        if (!isPasswordMatch) {
            Text("Passwords do not match", color = Color.Red, modifier = Modifier.padding(start = 10.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if(isPasswordMatch) {
                    viewModel.signUp(
                        user = User(
                            name = fullName.value,
                            email = phoneNumber, // Use the value in the state
                        ),
                        password = password.value,
                        onSuccess = {
                            navController.popBackStack()
                            navController.navigate(route.signInScreen)
                        },
                        onFailure = { exception ->
                            Toast.makeText(context, "User already exists", Toast.LENGTH_LONG).show()
                        }
                    )
                }
                else {
                    Toast.makeText(context, "Passwords don't match", Toast.LENGTH_LONG).show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MyColor.darkBlue,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.DarkGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .size(
                    width = 160.dp,
                    height = 50.dp
                ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                "Sign Up",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = Color.White,
                )
            )
        }
    }
}