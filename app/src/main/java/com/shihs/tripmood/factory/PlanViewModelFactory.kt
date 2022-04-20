package app.appworks.school.publisher.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.plan.MyPlanViewModel
import com.shihs.tripmood.plan.createschedule.CreateScheduleViewModel

/**
 * Factory for all ViewModels which need [plan].
 */
@Suppress("UNCHECKED_CAST")
class PlanViewModelFactory(
    private val repository: TripMoodRepo,
    private val plan: Plan?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass) {
            when {

                isAssignableFrom(MyPlanViewModel::class.java) ->
                    MyPlanViewModel(repository, plan)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

}