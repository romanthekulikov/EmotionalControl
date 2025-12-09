package ru.kulikov.core.utils.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import ru.kulikov.core.utils.domain.AppSharedPreferences
import ru.kulikov.core.utils.domain.UserId
import javax.inject.Inject

class AppSharedPreferencesImpl @Inject constructor(context: Context) : AppSharedPreferences {
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun saveUserId(userId: UserId) {
        preferences.edit {
            putInt(KEY_USER_ID, userId)
        }
    }

    override fun getUserId(): UserId = preferences.getInt(KEY_USER_ID, UNSAVED_INT_VALUE)

    override fun containsUserId(): Boolean = preferences.getInt(KEY_USER_ID, UNSAVED_INT_VALUE) != UNSAVED_INT_VALUE

    override fun savePartnerId(partnerId: UserId) {
        preferences.edit {
            putInt(KEY_PARTNER_ID, partnerId)
        }
    }

    override fun getPartnerId(): UserId = preferences.getInt(KEY_PARTNER_ID, UNSAVED_INT_VALUE)

    override fun containsPartnerId(): Boolean = preferences.getInt(KEY_PARTNER_ID, UNSAVED_INT_VALUE) != UNSAVED_INT_VALUE

    override fun forgotUserId() {
        preferences.edit {
            remove(KEY_USER_ID)
        }
    }

    override fun forgotPartnerId() {
        preferences.edit {
            remove(KEY_PARTNER_ID)
        }
    }

    private companion object {
        const val PREFERENCES_NAME = "emotional_control_sp"
        const val KEY_USER_ID = "user_id"
        const val KEY_PARTNER_ID = "partner_id"
        const val UNSAVED_INT_VALUE = -1
    }

}