package com.example.authenticationn.Data.local.converter

// Converters.kt

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(java.util.Date(it)) }
    }

    @TypeConverter
    fun timestampToLong(timestamp: Timestamp?): Long? {
        return timestamp?.toDate()?.time
    }

    @TypeConverter
    fun documentReferenceToString(documentReference: DocumentReference?): String? {
        return documentReference?.path
    }

    @TypeConverter
    fun stringToDocumentReference(path: String?): DocumentReference? {
        return path?.let { FirebaseFirestore.getInstance().document(it) }
    }
}