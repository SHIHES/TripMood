package app.appworks.school.publisher.ext

import androidx.fragment.app.Fragment
import app.appworks.school.publisher.factory.PlanViewModelFactory
import app.appworks.school.publisher.factory.ViewModelFactory
import com.shihs.tripmood.TripMoodApplication
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.factory.CreateScheduleViewModelFactory
import com.shihs.tripmood.factory.PlanTypeViewModelFactory
import com.shihs.tripmood.factory.ScheduleViewModelFactory
import com.shihs.tripmood.factory.UserPlanTypeViewModelFactory
import com.shihs.tripmood.util.HomePlanFilter
import com.shihs.tripmood.util.UserPlanFilter

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

fun Fragment.getVmFactory(homePlanType: HomePlanFilter):PlanTypeViewModelFactory {
    val repository = (requireContext().applicationContext as TripMoodApplication).repository
    return PlanTypeViewModelFactory(repository, homePlanType)
}

fun Fragment.getVmFactory(userPlanFilter: UserPlanFilter):UserPlanTypeViewModelFactory {
    val repository = (requireContext().applicationContext as TripMoodApplication).repository
    return UserPlanTypeViewModelFactory(repository, userPlanFilter)
}

fun Fragment.getVmFactory(schedule: Schedule?): ScheduleViewModelFactory {
    val repository = (requireContext().applicationContext as TripMoodApplication).repository
    return ScheduleViewModelFactory(repository, schedule)
}



//fun Fragment.getVmFactory(author: Author?): AuthorViewModelFactory {
//    val repository = (requireContext().applicationContext as TripMoodApplication).repository
//    return AuthorViewModelFactory(repository, author)
//}