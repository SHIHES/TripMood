package com.shihs.tripmood.plan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.Exception

class MyPlanViewModel(private val repository: TripMoodRepo, arguments: Plan?) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private val _plan = MutableLiveData<Plan>().apply {
        value = arguments
    }
    val plan: LiveData<Plan>
        get() = _plan

    private val _schedules = MutableLiveData<List<Schedule>>()

    val schedules: LiveData<List<Schedule>>
        get() = _schedules

    private val _selectedSchedule = MutableLiveData<Schedule>()

    val selectedSchedule: LiveData<Schedule>
        get() = _selectedSchedule

    private val _dayOfSchedule = MutableLiveData<List<Schedule>>()

    val dayOfSchedule: LiveData<List<Schedule>>
        get() = _dayOfSchedule

<<<<<<< HEAD
    var liveSchedules = MutableLiveData<List<Schedule>>()

=======
    private val _selectedAdapterPosition = MutableLiveData<Int>()

    val selectedAdapterPosition: LiveData<Int>
        get() = _selectedAdapterPosition

    var liveSchedules = MutableLiveData<List<Schedule>>()

    var adapterPosition = -1

    var clickSchedule: Schedule = Schedule()


>>>>>>> develop

    init {
        getLiveSchedule()
        daysCalculator()

    }

    fun getLiveSchedule(){
        if (_plan.value != null){
            _plan.value!!.id?.let {
                liveSchedules = repository.getLiveSchedule(it) }
        }
    }

    fun daysCalculator(){
            try {

                var start = _plan.value?.startDate
                var end = _plan.value?.endDate

                var list = mutableListOf<Schedule>()

                if (start != null && end != null){
                    while (start <= end){

                        list.add(Schedule(time = start,))

                        start += 86400000
                    }
                    _schedules.value = list
                }
                Log.d("QAQ", "${_schedules.value}")

            } catch (e: Exception){
                Log.d("QAQ","daysCalculator error $e")
            }

        }

    fun getSelectedSchedule(selectedSchedule: Schedule){
        _selectedSchedule.value = selectedSchedule
<<<<<<< HEAD
=======
        clickSchedule = selectedSchedule
>>>>>>> develop
    }

    fun findTimeRangeSchedule(){
        try {
            val aDayOfSchedule = _selectedSchedule.value?.time?.plus(86400000)?.minus(1)

            _dayOfSchedule.value = liveSchedules.value?.filter {
                it.time in _selectedSchedule.value?.time!!.plus(1)..aDayOfSchedule!!
            }

        } catch (e: Exception){
            Log.d("QAQ","findTimeRangeSchedule error $e")

        }
    }
<<<<<<< HEAD
=======

    fun getSelectedAdapterPosition(position: Int){
        adapterPosition = position
    }
>>>>>>> develop
}