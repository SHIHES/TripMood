package com.shihs.tripmood.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.detail.DetailViewModel


/**
 * Factory for all ViewModels which need [plan], [schedule].
 */
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
