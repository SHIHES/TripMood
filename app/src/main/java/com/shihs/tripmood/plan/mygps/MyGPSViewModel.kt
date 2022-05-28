package com.shihs.tripmood.plan.mygps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Location
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MyGPSViewModel(private val repository: TripMoodRepo, arg1: Plan?, arg2: Schedule?, arg3: Int?) : ViewModel() {

    private var argPlan = arg1

    private var argSchedule = arg2

    private var argPosition = arg3?.plus(1)

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _selectedLocation = MutableLiveData<Location>()

    val selectedLocation: LiveData<Location>
        get() = _selectedLocation

    private val _nearbyLocation = MutableLiveData<List<Location>?>()

    val nearbyLocation: LiveData<List<Location>?>
        get() = _nearbyLocation

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    fun clearNearbyLocation() {
        _nearbyLocation.value = null
    }

    fun getNearbyLocation(locations: List<Location>) {
        _nearbyLocation.value = locations
    }

    fun getLocationFromCard(location: Location) {
        _selectedLocation.value = location
    }

    fun packageGPSSchedule(location: Location) {
        val schedule = Schedule()

        val fmt = SimpleDateFormat("HH:mm", Locale.TAIWAN)
        val scheduleTime = fmt.parse("12:00")?.time
        val postTime = argSchedule?.time?.let { it -> scheduleTime?.plus(it) }

        schedule.theDay = argPosition!!
        schedule.planID = argPlan?.id
        schedule.time = postTime
        schedule.location = location

        Log.d("QAQ, ", "packageGPSSchedule $location")

        postNewSchedule(schedule = schedule)
    }

    private fun postNewSchedule(schedule: Schedule) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (
                val result =
                    repository.postSchedule(schedule = schedule, planID = schedule.planID!!)
            ) {
                is Result.Success -> {
                    Log.d("QAQ, ", "postNewSchedule ${schedule.location}")
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
