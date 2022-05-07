package com.shihs.tripmood.util

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shihs.tripmood.R
import com.shihs.tripmood.TripMoodApplication
import com.shihs.tripmood.dataclass.User
import com.shihs.tripmood.util.Util.getString

object UserManager {

    private const val USER_DATA = "user_data"
    private const val USER_TOKEN = "user_token"

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    var userToken: String? = null
        get() = TripMoodApplication.instance
            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            .getString(USER_TOKEN, null)
        set(value) {
            field = when (value) {
                null -> {
                    TripMoodApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .remove(USER_TOKEN)
                        .apply()
                    null
                }
                else -> {
                    TripMoodApplication.instance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(USER_TOKEN, value)
                        .apply()
                    value
                }
            }
        }

    val isLoggedIn: Boolean
        get() = userToken != null


    fun clear() {
        userToken = null
        _user.value = null
    }

    private var lastChallengeTime: Long = 0
    private var challengeCount: Int = 0
    private const val CHALLENGE_LIMIT = 23

//    fun challenge() {
//        if (System.currentTimeMillis() - lastChallengeTime > 5000) {
//            lastChallengeTime = System.currentTimeMillis()
//            challengeCount = 0
//        } else {
//            if (challengeCount == CHALLENGE_LIMIT) {
//                userToken = null
//                Toast.makeText(
//                    TripMoodApplication.instance,
//                    getString(R.string.profile_mystic_information),
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                challengeCount++
//            }
//        }
//    }
}