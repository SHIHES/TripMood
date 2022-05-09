package com.shihs.tripmood

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.User
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.network.LoadApiStatus
import com.shihs.tripmood.util.CurrentFragmentType
import com.shihs.tripmood.util.Logger
import com.shihs.tripmood.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.shihs.tripmood.dataclass.Result

class MainViewModel(private val repository: TripMoodRepo) : ViewModel() {

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val isLoggedIn
        get() = UserManager.isLoggedIn


    val currentFragmentType = MutableLiveData<CurrentFragmentType>()


    fun setupUser(user: User) {

        _user.value = user
        Logger.i("=============")
        Logger.i("| setupUser |")
        Logger.i("user=$user")
        Logger.i("MainViewModel=$this")
        Logger.i("=============")
    }

//    fun checkUser() {
//        if (user.value == null) {
//            UserManager.userToken?.let {
//                getUserProfile(it)
//            }
//        }
//    }

//    private fun getUserProfile(token: String) {
//
//        coroutineScope.launch {
//
//            _status.value = LoadApiStatus.LOADING
//
//            val result = repository.getUserProfile(token)
//
//            _user.value = when (result) {
//
//                is Result.Success -> {
//                    _error.value = null
//                    _status.value = LoadApiStatus.DONE
//                    result.data
//                }
//                is Result.Fail -> {
//                    _error.value = result.error
//                    _status.value = LoadApiStatus.ERROR
//                    if (result.error.contains("Invalid Access Token", true)) {
//                        UserManager.clear()
//                    }
//                    null
//                }
//                is Result.Error -> {
//                    _error.value = result.exception.toString()
//                    _status.value = LoadApiStatus.ERROR
//                    null
//                }
//                else -> {
//                    _error.value = "error get profile"
//                    _status.value = LoadApiStatus.ERROR
//                    null
//                }
//            }
//        }
//    }


}