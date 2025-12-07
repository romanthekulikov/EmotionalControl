package ru.kulikov.core.utils.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

internal typealias FirebaseUserId = String

object AppFirebase {
    val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    internal val database = Firebase.database("https://emotional-control-default-rtdb.firebaseio.com/").reference
    val usersReference = UsersReference(database)
}


class UsersReference(private val databaseReference: DatabaseReference) {
    private fun reference(): DatabaseReference = databaseReference.child("users")
    fun userReference(userId: FirebaseUserId): UserReference {
        return UserReference(reference().child(userId))
    }
}

class UserReference(private val userReference: DatabaseReference) {

    fun idReference(): DatabaseReference = userReference.child("user_id")

    fun nameReference(name: String): DatabaseReference = userReference.child("name")

    fun indicatorsDataReference(): IndicatorsDataReference = IndicatorsDataReference(userReference.child("indicators_value"))

    fun avatarUrlReference(): DatabaseReference = userReference.child("avatar_url")
}

class IndicatorsDataReference(private val indicatorsDataReference: DatabaseReference) {
    fun dateReference(date: String): DateReference = DateReference(indicatorsDataReference.child(date))
}

class DateReference(private val dateReference: DatabaseReference) {
    fun indicatorReference(indicator: String) = dateReference.child(indicator)
}