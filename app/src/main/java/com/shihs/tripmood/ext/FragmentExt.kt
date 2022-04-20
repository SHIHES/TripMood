package app.appworks.school.publisher.ext

import androidx.fragment.app.Fragment
import app.appworks.school.publisher.factory.PlanViewModelFactory
import app.appworks.school.publisher.factory.ViewModelFactory
import com.shihs.tripmood.TripMoodApplication
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.factory.CreateScheduleViewModelFactory

/**
 * Extension functions for Fragment.
 */
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as TripMoodApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(plan: Plan?): PlanViewModelFactory {
    val repository = (requireContext().applicationContext as TripMoodApplication).repository
    return PlanViewModelFactory(repository, plan)
}

fun Fragment.getVmFactory(plan: Plan?, schedule: Schedule?, adapterPosition: Int?):CreateScheduleViewModelFactory {
    val repository = (requireContext().applicationContext as TripMoodApplication).repository
    return CreateScheduleViewModelFactory(repository, plan, schedule, adapterPosition)
}


//fun Fragment.getVmFactory(author: Author?): AuthorViewModelFactory {
//    val repository = (requireContext().applicationContext as TripMoodApplication).repository
//    return AuthorViewModelFactory(repository, author)
//}