package com.shihs.tripmood.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.plan.createschedule.CreateScheduleViewModel
import com.shihs.tripmood.plan.mygps.MyGPSViewModel

/**
 * Factory for all ViewModels which need [plan], [schedule], [adapterPosition].
 */
@Suppress("UNCHECKED_CAST")
class CreateScheduleViewModelFactory(
    private val repository: TripMoodRepo,
    private val plan: Plan?,
    private val schedule: Schedule?,
    private val adapterPosition: Int?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(CreateScheduleViewModel::class.java) ->
                    CreateScheduleViewModel(repository, plan, schedule, adapterPosition)

                isAssignableFrom(MyGPSViewModel::class.java) ->
                    MyGPSViewModel(repository, plan, schedule, adapterPosition)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
