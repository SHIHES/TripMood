package com.shihs.tripmood.util

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.shihs.tripmood.TripMoodApplication
import com.shihs.tripmood.dataclass.User

object UserManager {

    private const val USER_DATA = "user_data"
    private const val USER_UID = "user_uid"
    private const val USER_PHOTOURL = "user_photoUrl"
    private const val USER_NAME = "user_name"


    var userUID: String? = null
        get() = TripMoodApplication.instance
            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            .getString(USER_UID, null)
        set(value) {
            field = when (value) {
                null -> {
                    TripMoodApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .remove(USER_UID)
                        .apply()
                    null
                }
                else -> {
                    TripMoodApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(USER_UID, value)
                        .apply()
                    value
                }
            }
        }

    var userPhotoUrl: String? = null
        get() = TripMoodApplication.instance
            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            .getString(USER_PHOTOURL, null)
        set(value) {
            field = when (value) {
                null -> {
                    TripMoodApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .remove(USER_PHOTOURL)
                        .apply()
                    null
                }
                else -> {
                    TripMoodApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(USER_PHOTOURL, value)
                        .apply()
                    value
                }
            }
        }

    var userName: String? = null
        get() = TripMoodApplication.instance
            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            .getString(USER_NAME, null)
        set(value) {
            field = when (value) {
                null -> {
                    TripMoodApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .remove(USER_NAME)
                        .apply()
                    null
                }
                else -> {
                    TripMoodApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(USER_NAME, value)
                        .apply()
                    value
                }
            }
        }

    val isLoggedIn: Boolean
        get() = userUID != null


    fun clear() {
        userUID = null
        userPhotoUrl = null
        userName = null
    }



}