package com.shihs.tripmood.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.detail.DetailViewModel
import com.shihs.tripmood.plan.MyPlanViewModel


@Suppress("UNCHECKED_CAST")
class ScheduleViewModelFactory(
    private val repository: TripMoodRepo,
    private val schedule: Schedule?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {

                isAssignableFrom(DetailViewModel::class.java) ->
                    DetailViewModel(repository, schedule)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

}