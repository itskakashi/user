package com.example.authenticationn

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.authenticationn.Presentation.FireBaseViewModel
import com.example.authenticationn.Presentation.Ui.navigation
import com.example.authenticationn.theme.AuthenticationnTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    private val viewModel: FireBaseViewModel by viewModels()
    private val db = Firebase.firestore
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuthenticationnTheme {
             navigation()
            }
        }
    }
}