package com.example.authenticationn.DI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.authenticationn.Data.FireStoreDatabase.managers.AnalyticsManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.BillingAndPaymentManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.CanesManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.OrderManager
import com.example.authenticationn.Data.FireStoreDatabase.managers.UserManager

import com.example.authenticationn.Domain.FireBaseRepository
import com.example.authenticationn.Presentation.FireBaseViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import kotlin.coroutines.EmptyCoroutineContext.get

val appModule = module {

    // Database
//    single {
//        Room.databaseBuilder(
//            androidApplication(),
//            AppDatabase::class.java,
//            "app_database"
//        ).build()
//    }

    // DAOs
//    single { get<AppDatabase>().userDao() }
//    single { get<AppDatabase>().orderDao() }
//    single { get<AppDatabase>().billDao() }
//    single { get<AppDatabase>().analyticsDao() }
//    single { get<AppDatabase>().paymentDao() }
//    single { get<AppDatabase>().pendingPaymentDao() }

    // Managers (Firestore)
    singleOf(:: UserManager)  // <--- Use the correct UserManager
    singleOf(:: OrderManager)


    singleOf(:: BillingAndPaymentManager)
    singleOf(:: AnalyticsManager)
    singleOf(:: CanesManager)

    singleOf(:: FireBaseRepository)

 viewModelOf(:: FireBaseViewModel)



     // Repositories
//    single { UserRepository(get(), get()) }
//    single { OrderRepository(get(), get()) }
//    single { BillRepository(get(), get()) }
//    single { AnalyticsRepository(get(), get()) }
//
//    // ViewModels
//    viewModel { OrderViewModel(get()) }
//    viewModel { BillViewModel(get()) }
//    viewModel { AnalyticsViewModel(get()) }



}