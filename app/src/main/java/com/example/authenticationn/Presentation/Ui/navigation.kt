package com.example.authenticationn.Presentation.Ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authenticationn.Presentation.FireBaseViewModel
import org.koin.androidx.compose.koinViewModel
import placeOrderScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun navigation(){
 val viewModel = koinViewModel<FireBaseViewModel>()
    val navHostController = rememberNavController()

    NavHost(navController = navHostController,startDestination = route.signInScreen){
         composable<route.signInScreen> {
             loginScreen(navHostController,viewModel)
         }
        composable<route.singUpScreen>{
            signUpScreen(navHostController,viewModel)
        }
        composable<route.homeScreen>{
             userHomeScreen(navHostController,viewModel,viewModel.userid.value)
        }
        composable<route.placeOrderScreen> {
            placeOrderScreen(navHostController,viewModel,viewModel.userid.value)
        }

        composable<route.orderScreen> {
            orderScreen(navHostController,viewModel,viewModel.userid.value)
        }


    }
}