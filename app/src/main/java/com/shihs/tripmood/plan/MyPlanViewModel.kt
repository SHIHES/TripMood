package com.shihs.tripmood.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Activity
import com.shihs.tripmood.dataclass.Schedule

class MyPlanViewModel : ViewModel() {

    private val _activity = MutableLiveData<Activity>()

    val activity: LiveData<Activity>
        get() = _activity

    private val _schedule = MutableLiveData<Schedule>()

    val schedule: LiveData<Schedule>
        get() = _schedule




    val liveCacheList = MutableLiveData<List<Activity>>()
    val cacheList = mutableListOf<Activity>()

    fun storeSchedule(activity: Activity){
        cacheList.add(activity)
        liveCacheList.value = cacheList
    }
}