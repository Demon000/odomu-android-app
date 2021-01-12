package com.demon000.odomu.repositories

import android.content.Context
import android.util.Log
import com.demon000.odomu.dependencies.DependencyLocator
import com.demon000.odomu.utils.Constants.Companion.LOG_TAG
import com.demon000.odomu.utils.Constants.Companion.SHARED_PREFS_NAME

class UserRepository() {
    val ACCESS_TOKEN_PREF_NAME = "access-token"
    val REFRESH_TOKEN_PREF_NAME = "refresh-token"

    fun setToken(prefName: String, token: String?) {
        if (DependencyLocator.context == null) {
            Log.e(LOG_TAG, "Application context not available in dependency locator")
            return
        }

        val prefs = DependencyLocator.context!!.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(prefName, token).apply()
    }

    fun getToken(prefName: String): String? {
        if (DependencyLocator.context == null) {
            Log.e(LOG_TAG, "Application context not available in dependency locator")
            return null
        }

        val prefs = DependencyLocator.context!!.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(prefName, null)
    }

    var accessToken: String?
        get() {
            return getToken(ACCESS_TOKEN_PREF_NAME)
        }
        set(value) {
            setToken(ACCESS_TOKEN_PREF_NAME, value)
        }

    var refreshToken: String?
        get() {
            return getToken(REFRESH_TOKEN_PREF_NAME)
        }
        set(value) {
            setToken(REFRESH_TOKEN_PREF_NAME, value)
        }
}
