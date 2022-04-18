package com.shihs.tripmood.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Event
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Schedule

class MyPlanViewModel(arg: Plan) : ViewModel() {

    private val _activity = MutableLiveData<Event>()

    val event: LiveData<Event>
        get() = _activity

    private val _schedule = MutableLiveData<Schedule>()

    val schedule: LiveData<Schedule>
        get() = _schedule




    val liveCacheList = MutableLiveData<List<Event>>()
    val activityCacheList = mutableListOf<Event>()
    val scheduleCacheList = mutableListOf<Schedule>()


    fun getSchedulePosition(position: Int ){

    }

    fun storeActivityToSchedule(schedule: Schedule){
        schedule.events?.addAll(activityCacheList)
    }

    fun getActivity(event: Event){
        activityCacheList.add(event)
    }
}