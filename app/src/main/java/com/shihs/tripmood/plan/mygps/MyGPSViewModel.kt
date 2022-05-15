package com.shihs.tripmood.plan.mygps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Location
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MyGPSViewModel(private val repository: TripMoodRepo) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _selectedLocation = MutableLiveData<Location>()

    val selectedLocation: LiveData<Location>
        get() = _selectedLocation

    private val _nearbyLocation = MutableLiveData<List<Location>>()

    val nearbyLocation: LiveData<List<Location>>
        get() = _nearbyLocation

    private val _userSaveLocation = MutableLiveData<List<Location>>()

    val userSaveLocation: LiveData<List<Location>>
        get() = _userSaveLocation

    private val _planTitle = MutableLiveData<String>()

    val planTitle: LiveData<String>
        get() = _planTitle


    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error


    var planID = ""






    fun getPlanTitle(title: String){
        _planTitle.value = title
    }


    fun clearNearbyLocation(){
        _nearbyLocation.value = null
    }

    fun clearUserAddLocationList(){
        _userSaveLocation.value = null
        userAddLocationList.clear()
    }

    fun getNearbyLocation(locations: List<Location>){
        _nearbyLocation.value = locations
    }


    fun getLocationFromCard(location: Location){
        _selectedLocation.value = location
    }


    var userAddLocationList = mutableListOf<Location>()

    fun userAddLocation(location: Location){
        userAddLocationList.add(location)
        _userSaveLocation.value = userAddLocationList
    }

    fun packageGPSPlan(plan: Plan){

        val calendar = Calendar.getInstance(Locale.TAIWAN)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val date = calendar.get(Calendar.DATE)
        val startTimeString = "$year/$month/$date"
        val startTimeMilli = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH).parse(startTimeString).time

        val endTime = startTimeMilli

        plan.startDate = startTimeMilli
        plan.endDate = endTime
        plan.title = _planTitle.value

    }

    fun uploadScheduleAndGPSLocation(){
        var schedule = Schedule()

        for(location in _userSaveLocation.value!!){
            Log.d("QAQ, ","uploadScheduleAndGPSLocation ${location}")
            postNewSchedule(schedule, location)
        }

    }

    fun packageGPSSchedule(schedule: Schedule, location: Location){

        val now = Calendar.getInstance(Locale.TAIWAN).timeInMillis

        schedule.planID = planID
        schedule.time = now
        schedule.location = location

        Log.d("QAQ, ","packageGPSSchedule ${location}")


    }



    fun postNewPlan(plan: Plan){

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.postPlan(plan = plan)) {
                is Result.Success -> {

                    planID = result.data
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

    fun postNewSchedule(schedule: Schedule, location: Location){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            packageGPSSchedule(schedule = schedule, location = location)

            when (val result =
                repository.postSchedule(schedule = schedule, planID = schedule.planID!!)
            ) {
                is Result.Success -> {
                    Log.d("QAQ, ","postNewSchedule ${schedule.location}")
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