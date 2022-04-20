package com.shihs.tripmood.plan.createschedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

<<<<<<< HEAD
class CreateScheduleViewModel(private val repository: TripMoodRepo, arguments: Plan?) : ViewModel() {

    private val _plan = MutableLiveData<Plan>().apply {
        value = arguments
=======
class CreateScheduleViewModel(private val repository: TripMoodRepo, arg1: Plan?, arg2: Schedule?, arg3: Int?) : ViewModel() {

    private val _plan = MutableLiveData<Plan>().apply {
        value = arg1

>>>>>>> develop
    }
    val plan: LiveData<Plan>
        get() = _plan


    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    fun postNewSchedule(schedule: Schedule){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = _plan.value?.id?.let {
                    repository.postSchedule(schedule = schedule, planID = it)
                }) {
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