package app.appworks.school.publisher.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.plan.MyPlanViewModel

/**
 * Factory for all ViewModels which need [plan].
 */
@Suppress("UNCHECKED_CAST")
class PlanViewModelFactory(
    private val repository: TripMoodRepo,
    private val plan: Plan?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPlanViewModel::class.java)) {
            return MyPlanViewModel(repository, plan) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

}