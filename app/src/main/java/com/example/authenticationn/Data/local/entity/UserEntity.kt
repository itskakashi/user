package com.example.authenticationn.Data.local.entity

// UserEntity.kt

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.authenticationn.Data.FireStoreDatabase.Models.User

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey val userId: String,
    var name: String? = null,
    var initial: String? = null,
    var userName: String? = null,
    var contactInfo: String? = null,
    var amount: Double? = null,
    var profilePictureUrl: String? = null,
    var address: String? = null,
    var defaultJarSize: String? = null,
    var preferredDeliveryTime: String? = null,
    var isStaff: Boolean? = false, // Default to false for customers
    var isCompany: Boolean? = null,
    var serviceName: String? = null,
    var coldWaterPrice: Double? = null,
    var regularWaterPrice: Double? = null,
    var isOpen: Boolean? = null,
    var depositMoney: Double? = null,
    var canesTaken: Int? = null,
    var canesReturned: Int? = null,

    ) {
    fun toUser(): User {
        return User(
            userId = userId,
            name = name,
            initial = initial,
            userName = userName,
            contactInfo = contactInfo,
            amount = amount,
            profilePictureUrl = profilePictureUrl,
            address = address,
            defaultJarSize = defaultJarSize,
            preferredDeliveryTime = preferredDeliveryTime,
            isStaff = isStaff,
            isCompany = isCompany,
            serviceName = serviceName,
            coldWaterPrice = coldWaterPrice,
            regularWaterPrice = regularWaterPrice,
            isOpen = isOpen,
            depositMoney = depositMoney,
            canesTaken = canesTaken,
            canesReturned = canesReturned
        )
    }
}