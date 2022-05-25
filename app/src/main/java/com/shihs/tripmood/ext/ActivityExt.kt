package com.shihs.tripmood.ext

import android.app.Activity
import app.appworks.school.publisher.factory.ViewModelFactory
import com.shihs.tripmood.TripMoodApplication

/**
 * Extension functions for Activity.
 */
fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as TripMoodApplication).repository
    return ViewModelFactory(repository)
}

