package com.shihs.tripmood.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.User
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.network.LoadApiStatus
import com.shihs.tripmood.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: TripMoodRepo) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _navigateToLoginSuccess = MutableLiveData<String>()

    val navigateToLoginSuccess: LiveData<String>
        get() = _navigateToLoginSuccess

    fun checkUserExist(user: User) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            when (val result = repository.checkUserExist(userID = user.uid!!)) {
                is Result.Success -> {
                    if (result.data.uid == null) {
                        createNewUser(user = user)
                    } else {
                        Log.d("SS", "${result.data}")
                        UserManager.userName = result.data.name
                        UserManager.userUID = result.data.uid
                        UserManager.userPhotoUrl = result.data.image
                        _error.value = "1"
                        _status.value = LoadApiStatus.DONE
                        _user.value = result.data!!
                        _navigateToLoginSuccess.value = UserManager.userUID
                    }
                }
                is Result.Fail -> {
                    Log.d("SS", "Fail${result.error}")
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    Log.d("SS", "Error${result.exception}")
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    Log.d("SS", "else$result")
                    _error.value = "login failed"
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun createNewUser(user: User) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.postUser(user)) {
                is Result.Success -> {
                    UserManager.userName = user.name
                    UserManager.userUID = user.uid
                    UserManager.userPhotoUrl = user.image
                    _error.value = "1"
                    _status.value = LoadApiStatus.DONE
                    _user.value = user
                    _navigateToLoginSuccess.value = UserManager.userUID
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _error.value = "login failed"
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun navigateToLoginSuccessEnd() {
        _navigateToLoginSuccess.value = null
    }
}
