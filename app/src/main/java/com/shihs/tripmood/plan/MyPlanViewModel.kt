package com.shihs.tripmood.plan

import android.util.Log
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
import java.lang.Exception
import java.util.*

class MyPlanViewModel(private val repository: TripMoodRepo, arguments: Plan?) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()

    val error: LiveData<String?>
        get() = _error

    private val _plan = MutableLiveData<Plan>().apply {
        value = arguments
    }
    val plan: LiveData<Plan>
        get() = _plan

    private val _schedules = MutableLiveData<List<Schedule>>()

    val schedules: LiveData<List<Schedule>>
        get() = _schedules

    private val _dayOfSchedule = MutableLiveData<List<Schedule>?>()

    val dayOfSchedule: LiveData<List<Schedule>?>
        get() = _dayOfSchedule

    private val _navigationToDetail = MutableLiveData<Schedule?>()

    val navigationToDetail: LiveData<Schedule?>
        get() = _navigationToDetail

    var liveSchedules = MutableLiveData<List<Schedule>>()

    var adapterPosition = MutableLiveData(0)

    private val _positionControlSchedule = MutableLiveData<Schedule>()

    val positionControlSchedule: LiveData<Schedule>
        get() = _positionControlSchedule

    init {
        getLiveSchedule()
        daysCalculator()
    }

    fun getLiveSchedule() {
        if (_plan.value != null) {
            _plan.value!!.id?.let {
                liveSchedules = repository.getLiveSchedule(it)
            }
        }
    }

    fun daysCalculator() {
        try {
            var start = _plan.value?.startDate
            val end = _plan.value?.endDate
            val list = mutableListOf<Schedule>()

            if (start != null && end != null) {
                while (start <= end) {
                    list.add(Schedule(time = start))

                    start += 86400000
                }
                _schedules.value = list
            }
            Log.d("QAQ", "${_schedules.value}")
        } catch (e: Exception) {
            Log.d("QAQ", "daysCalculator error $e")
        }
    }

    fun getPositionAndDate(position: Int) {
        _positionControlSchedule.value = _schedules.value!![position]
    }

    fun findTimeRangeSchedule() {
        try {
            val aDayOfSchedule = _positionControlSchedule.value?.time?.plus(86400000)?.minus(1)

            _dayOfSchedule.value = liveSchedules.value?.filter {
                it.time in _positionControlSchedule.value?.time!!.plus(1)..aDayOfSchedule!!
            }?.sortedBy {
                it.time
            }
        } catch (e: Exception) {
            Log.d("QAQ", "findTimeRangeSchedule error $e")
        }
    }

    fun getSelectedAdapterPosition(position: Int) {
        adapterPosition.value = position
    }

    fun selectedScheduleClear() {
        _dayOfSchedule.value = null
    }

    fun navigationToDetail(schedule: Schedule) {
        _navigationToDetail.value = schedule
    }

    fun navigationToDetailEnd() {
        _navigationToDetail.value = null
    }

    fun showAnimation(schedule: Schedule, position: Int): Boolean {
        val calendar = Calendar.getInstance(Locale.TAIWAN).timeInMillis

        // 判斷data不為空
        if (_dayOfSchedule.value!!.isNotEmpty()) {
            // 判斷list大小大於1個
            if (_dayOfSchedule.value?.size!! > 1) {
                // 判斷list的最後一個位置
                if (position == _dayOfSchedule.value?.size?.minus(1) ?: 0) {
                    // 判斷list最後一個位置在行程時間的區間
                    if (schedule.time!! < calendar) {
                        return false
                    } else if (schedule.time!! >= calendar &&
                        schedule.time!! <= _dayOfSchedule.value!![position - 1].time!!
                    ) {
                        return true
                    } else {
                        return false
                    }
                } else {
                    if (schedule.time!! < calendar) {
                        return false
                    } else if (schedule.time!! >= calendar &&
                        schedule.time!! <= _dayOfSchedule.value!![position + 1].time!!
                    ) {
                        return true
                    } else {
                        return false
                    }
                }
            } else {
                return false
            }
        } else {
            return true
        }
    }

    fun scheulesDelete(schedule: Schedule) {
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (
                val result = repository.deleteSchedule(
                    planID = schedule.planID!!,
                    scheduleID = schedule.scheduleId!!
                )
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
