package com.shihs.tripmood.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.home.childpage.ChildHomeViewModel
import com.shihs.tripmood.util.HomePlanFilter

@Suppress("UNCHECKED_CAST")
class PlanTypeViewModelFactory(
    private val repository: TripMoodRepo,
    private val homePlanType: HomePlanFilter
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(ChildHomeViewModel::class.java) ->
                    ChildHomeViewModel(repository, homePlanType)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
