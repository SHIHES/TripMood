package app.appworks.school.publisher.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.home.childpage.ChildHomeViewModel
import com.shihs.tripmood.plan.ShowAllLocationViewModel
import com.shihs.tripmood.plan.createplan.CreatePlanViewModel
import com.shihs.tripmood.plan.mygps.MyGPSViewModel
import com.shihs.tripmood.search.SearchViewModel

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
                isAssignableFrom(MyGPSViewModel::class.java) ->
                    MyGPSViewModel(repository)

                isAssignableFrom(CreatePlanViewModel::class.java) ->
                    CreatePlanViewModel(repository)

                isAssignableFrom(SearchViewModel::class.java) ->
                    SearchViewModel(repository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
