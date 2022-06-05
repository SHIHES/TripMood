package com.shihs.tripmood

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.User
import com.shihs.tripmood.dataclass.UserLocation
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.network.LoadApiStatus
import com.shihs.tripmood.util.CurrentFragmentType
import com.shihs.tripmood.util.Logger
import com.shihs.tripmood.util.UserManager
import kotlinx.coroutines.*

class MainViewModel(private val repository: TripMoodRepo) : ViewModel() {

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()

    val error: LiveData<String?>
        get() = _error

    private val _isUserLocatedServiceReady = MutableLiveData<Boolean?>()

    val isUserLocatedServiceReady: LiveData<Boolean?>
        get() = _isUserLocatedServiceReady

    private val _userLocation = MutableLiveData<Location?>()

    val userLocation: LiveData<Location?>
        get() = _userLocation

    private val _isBroadcastRegistered = MutableLiveData<Boolean?>()

    val isBroadcastRegistered: LiveData<Boolean?>
        get() = _isBroadcastRegistered

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

    fun setBroadcastRegistered() {
        _isBroadcastRegistered.value = true
    }

    fun resetBroadcastStatus() {
        _isBroadcastRegistered.value = null
    }

    fun getUserLocatedServiceStatus(userLocatedServiceBound: Boolean) {
        _isUserLocatedServiceReady.value = userLocatedServiceBound
    }

    fun resetUserLocateServiceStatus() {
        _isUserLocatedServiceReady.value = null
    }

    fun onUpdateUserLocation() {
        _userLocation.value = null
    }

    fun setUserLocation(location: Location) {
        _userLocation.value = location
    }

    fun updateUserLocation(location: Location?) {
        
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            val userLocation = UserLocation(
                userUID = UserManager.userUID,
                userName = UserManager.userName,
                userPhotoUrl = UserManager.userPhotoUrl,
                lat = location?.latitude,
                lng = location?.longitude
            )

            when (
                val result =
                    repository.sendMyLocation(userLocation = userLocation)
            ) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
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
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }
    
}
