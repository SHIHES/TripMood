package com.shihs.tripmood.ext

import android.app.Activity
import android.view.Gravity
import android.widget.Toast
import app.appworks.school.publisher.factory.ViewModelFactory
import com.shihs.tripmood.TripMoodApplication

/**
 * Extension functions for Activity.
 */
fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as TripMoodApplication).repository
    return ViewModelFactory(repository)
}

// fun Activity.getVmFactory(plan: Plan?): PlanViewModelFactory {
//    val repository = (applicationContext as TripMoodApplication).repository
//    return PlanViewModelFactory(repository, plan)
// }

fun Activity?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}
