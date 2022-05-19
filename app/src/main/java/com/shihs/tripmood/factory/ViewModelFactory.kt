package app.appworks.school.publisher.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shihs.tripmood.MainViewModel

import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.favorite.FavoriteViewModel
import com.shihs.tripmood.home.HomeViewModel
import com.shihs.tripmood.home.childpage.ChildHomeViewModel
import com.shihs.tripmood.login.LoginViewModel
import com.shihs.tripmood.notification.NotificationViewModel
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

                isAssignableFrom(CreatePlanViewModel::class.java) ->
                    CreatePlanViewModel(repository)

                isAssignableFrom(SearchViewModel::class.java) ->
                    SearchViewModel(repository)

                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(repository)

                isAssignableFrom(NotificationViewModel::class.java) ->
                    NotificationViewModel(repository)

                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(repository)

                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(repository)

                isAssignableFrom(FavoriteViewModel::class.java) ->
                    FavoriteViewModel(repository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
