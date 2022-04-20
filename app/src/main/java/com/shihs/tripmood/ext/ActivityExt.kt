package app.appworks.school.publisher.ext

import android.app.Activity
import android.view.Gravity
import android.widget.Toast
<<<<<<< HEAD
import app.appworks.school.publisher.factory.ViewModelFactory
import com.shihs.tripmood.TripMoodApplication
=======
import androidx.fragment.app.Fragment
import app.appworks.school.publisher.factory.PlanViewModelFactory
import app.appworks.school.publisher.factory.ViewModelFactory
import com.shihs.tripmood.TripMoodApplication
import com.shihs.tripmood.dataclass.Plan
>>>>>>> develop

/**
 * Extension functions for Activity.
 */
fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as TripMoodApplication).repository
    return ViewModelFactory(repository)
}

<<<<<<< HEAD
=======
//fun Activity.getVmFactory(plan: Plan?): PlanViewModelFactory {
//    val repository = (applicationContext as TripMoodApplication).repository
//    return PlanViewModelFactory(repository, plan)
//}

>>>>>>> develop
fun Activity?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}