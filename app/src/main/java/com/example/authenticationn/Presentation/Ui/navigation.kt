package com.example.authenticationn.Presentation.Ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authenticationn.Presentation.FireBaseViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel
import placeOrderScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun navigation(){
 val viewModel = koinViewModel<FireBaseViewModel>()
    val navHostController = rememberNavController()
    val firebaseAuth = FirebaseAuth.getInstance()

    NavHost(navController = navHostController,startDestination =  if (firebaseAuth.currentUser != null) {
        val currentUser = firebaseAuth.currentUser!!.uid
        viewModel.updateUserId(currentUser)
        route.homeScreen // User is already logged in
    } else {
        route.signInScreen // User needs to log in
    }){
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
        composable<route.billScreen> {
            BillScreen(navHostController, viewModel, viewModel.userid.value)
        }


    }
}