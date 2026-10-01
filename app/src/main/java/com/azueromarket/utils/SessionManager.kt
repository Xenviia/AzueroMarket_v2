package com.azueromarket.utils

import android.content.Context
import android.content.SharedPreferences
import com.azueromarket.model.TipoUsuario

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "AzueroMarketSession"
        const val KEY_IS_LOGGED_IN  = "isLoggedIn"
        const val KEY_USER_ID       = "userId"
        const val KEY_USER_NAME     = "userName"
        const val KEY_USER_EMAIL    = "userEmail"
        const val KEY_USER_TYPE     = "userType"
        const val KEY_USER_PHONE    = "userPhone"
        const val KEY_USER_LOCATION = "userLocation"
    }

    fun saveSession(
        userId: String, name: String, email: String,
        tipoUsuario: TipoUsuario, phone: String = "", location: String = ""
    ) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_ID,       userId)
            putString(KEY_USER_NAME,     name)
            putString(KEY_USER_EMAIL,    email)
            putString(KEY_USER_TYPE,     tipoUsuario.name)
            putString(KEY_USER_PHONE,    phone)
            putString(KEY_USER_LOCATION, location)
            apply()
        }
    }

    fun isLoggedIn()       : Boolean     = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    fun getUserId()        : String      = prefs.getString(KEY_USER_ID,       "") ?: ""
    fun getUserName()      : String      = prefs.getString(KEY_USER_NAME,     "") ?: ""
    fun getUserEmail()     : String      = prefs.getString(KEY_USER_EMAIL,    "") ?: ""
    fun getUserPhone()     : String      = prefs.getString(KEY_USER_PHONE,    "") ?: ""
    fun getUserLocation()  : String      = prefs.getString(KEY_USER_LOCATION, "") ?: ""
    fun getTipoUsuario()   : TipoUsuario {
        val t = prefs.getString(KEY_USER_TYPE, TipoUsuario.CLIENTE.name)
        return TipoUsuario.valueOf(t ?: TipoUsuario.CLIENTE.name)
    }

    fun clearSession() = prefs.edit().clear().apply()
}
