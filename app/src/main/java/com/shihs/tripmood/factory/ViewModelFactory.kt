package app.appworks.school.publisher.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.home.childpage.ChildHomeViewModel
import com.shihs.tripmood.plan.createplan.CreatePlanViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val repository: TripMoodRepo
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {


                isAssignableFrom(CreatePlanViewModel::class.java) ->
                    CreatePlanViewModel(repository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
