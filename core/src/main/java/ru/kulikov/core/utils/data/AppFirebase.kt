package ru.kulikov.core.utils.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

internal typealias FirebaseUserId = String

const val FIREBASE_DATABASE_LINK = "https://emotional-control-default-rtdb.firebaseio.com/"
const val USERS_CHILD = "users"
const val USER_ID_CHILD = "user_id"
const val NAME_CHILD = "name"
const val INDICATORS_VALUE_CHILD = "indicators_value"
const val AVATAR_URL_CHILD = "avatar_url"
const val EMOTIONAL_CHILD = "emotional"

object AppFirebase {
    val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    internal val database = Firebase.database(FIREBASE_DATABASE_LINK).reference
    val usersReference = UsersReference(database)
}


class UsersReference(private val databaseReference: DatabaseReference) {
    fun reference(): DatabaseReference = databaseReference.child(USERS_CHILD)
    fun userReference(userId: FirebaseUserId): UserReference {
        return UserReference(reference().child(userId))
    }
}

class UserReference(private val userReference: DatabaseReference) {
    fun idReference(): DatabaseReference = userReference.child(USER_ID_CHILD)

    fun nameReference(name: String): DatabaseReference = userReference.child(NAME_CHILD)

    fun indicatorsDataReference(): IndicatorsDataReference = IndicatorsDataReference(userReference.child(INDICATORS_VALUE_CHILD))

    fun avatarUrlReference(): DatabaseReference = userReference.child(AVATAR_URL_CHILD)
}

class IndicatorsDataReference(private val indicatorsDataReference: DatabaseReference) {
    fun reference(date: String): DatabaseReference = indicatorsDataReference.child(date)
    fun dateReference(date: String): DateReference = DateReference(reference(date))
}

class DateReference(private val dateReference: DatabaseReference) {
    fun indicatorReference(indicator: String) = dateReference.child(indicator)
    fun emotionalIndicatorReference() = indicatorReference(EMOTIONAL_CHILD)
}

