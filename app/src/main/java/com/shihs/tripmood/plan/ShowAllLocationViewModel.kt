package com.shihs.tripmood.plan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.dataclass.UserLocation
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ShowAllLocationViewModel(private val repository: TripMoodRepo, arg: Plan?) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _plan = MutableLiveData<Plan>().apply {
        value = arg
    }
    val plan: LiveData<Plan>
        get() = _plan

    private val _filterLocationData = MutableLiveData<List<Schedule>>()
    val filterLocationData: LiveData<List<Schedule>>
        get() = _filterLocationData

    private var _clickSchedule = MutableLiveData<Schedule>()
    val clickSchedule: LiveData<Schedule>
        get() = _clickSchedule

    private val _snapPosition = MutableLiveData<Int>()

    val snapPosition: LiveData<Int>
        get() = _snapPosition

    var reObserve = MutableLiveData<Boolean>()

    var liveSchedules = MutableLiveData<List<Schedule>>()

    var liveUsersLocation = MutableLiveData<List<UserLocation>>()

    var realCoworkUsersLocation = MutableLiveData<List<UserLocation>>()

    var realCoworkUsers = mutableListOf<UserLocation>()

    init {
        getAllSchedule()
        getAllUserLocation()
    }

    private fun getAllUserLocation() {
        liveUsersLocation = repository.getLiveCoworkingLocation()
    }

    fun sortUserLocation() {
        if (!_plan.value?.coworkingList.isNullOrEmpty()) {
            realCoworkUsers.clear()
            realCoworkUsersLocation.value = realCoworkUsers

            _plan.value?.coworkingList?.forEach {
                for (userLocation in liveUsersLocation.value!!) {
                    if (it == userLocation.userUID) {
                        realCoworkUsers.add(userLocation)
                        realCoworkUsersLocation.value = realCoworkUsers
                    }
                }
            }
        }
    }

    private fun getAllSchedule() {
        if (_plan.value != null) {
            _plan.value!!.id?.let {
                liveSchedules = repository.getLiveSchedule(it)

                reObserve.value = true

                Log.d("QAQQQQ", "liveSchedules ${repository.getLiveSchedule(it).value}")
            }
        }
    }

    fun filterWithLocationData(schedules: List<Schedule>) {
        Log.d("SSSS", "filterWithLocationData $schedules")
        _filterLocationData.value = schedules.filter {
            it.location != null
        }.sortedBy {
            it.time
        }

        Log.d("SSSS", "filterLocationData schedules ${_filterLocationData.value}")
    }

    fun getCLickData(schedule: Schedule) {
        _clickSchedule.value = schedule
    }

    fun onGalleryScrollChange(
        layoutManager: RecyclerView.LayoutManager?,
        linearSnapHelper: LinearSnapHelper
    ) {
        val snapView = linearSnapHelper.findSnapView(layoutManager)
        snapView?.let {
            layoutManager?.getPosition(snapView)?.let {
                if (it != snapPosition.value) {
                    _snapPosition.value = it
                }
            }
        }
    }
}
