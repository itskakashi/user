package com.example.authenticationn.Presentation.Ui

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

sealed class route(){

@Serializable
    data object signInScreen: route()
    @Serializable
    data object singUpScreen: route()

    @Serializable
    data object homeScreen: route()

    @Serializable
    data object placeOrderScreen: route()
    @Serializable
    data object orderScreen: route()

    @Serializable
    data object billScreen: route()

    @Serializable
    data object userProfile: route()
    @Serializable
    data object analyticsScreen: route()

}


