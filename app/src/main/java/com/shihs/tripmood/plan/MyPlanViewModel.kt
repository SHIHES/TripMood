package com.shihs.tripmood.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Schedule

class MyPlanViewModel : ViewModel() {

    private val _schedule = MutableLiveData<Schedule>()

    val schedule: LiveData<Schedule>
        get() = _schedule


    val liveCacheList = MutableLiveData<List<Schedule>>()
    val cacheList = mutableListOf<Schedule>()

    fun storeSchedule(schedule: Schedule){
        cacheList.add(schedule)
        liveCacheList.value = cacheList
    }
}