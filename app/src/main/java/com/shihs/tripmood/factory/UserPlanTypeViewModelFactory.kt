package com.shihs.tripmood.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.user.child.UserChildViewModel
import com.shihs.tripmood.util.UserPlanFilter

@Suppress("UNCHECKED_CAST")
class UserPlanTypeViewModelFactory(
    private val repository: TripMoodRepo,
    private val userPlanFilter: UserPlanFilter
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {
                isAssignableFrom(UserChildViewModel::class.java) ->
                    UserChildViewModel(repository, userPlanFilter)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
