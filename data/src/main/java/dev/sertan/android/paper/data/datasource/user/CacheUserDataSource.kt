package dev.sertan.android.paper.data.datasource.user

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal class CacheUserDataSource(private val sharedPref: SharedPreferences) : UserDataSource {
    private val userUid = MutableStateFlow<String?>(null)

    private val sharedPrefListener = OnSharedPreferenceChangeListener { _, key ->
        if (key == USER_UID_SHARED_PREF_KEY) refreshUserUid()
    }

    init {
        sharedPref.registerOnSharedPreferenceChangeListener(sharedPrefListener)
        refreshUserUid()
    }

    private fun refreshUserUid() {
        userUid.update { sharedPref.getString(USER_UID_SHARED_PREF_KEY, null) }
    }

    override fun getUserUidAsStream(): Flow<String?> = userUid

    override suspend fun setUserUid(userUid: String): Boolean {
        return sharedPref.edit().putString(USER_UID_SHARED_PREF_KEY, userUid).commit()
    }

    private companion object {
        const val USER_UID_SHARED_PREF_KEY = "userUidSharedPrefKey"
    }
}
