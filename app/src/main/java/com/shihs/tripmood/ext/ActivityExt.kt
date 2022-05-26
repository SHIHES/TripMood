package com.shihs.tripmood.ext

import android.app.Activity
import com.shihs.tripmood.TripMoodApplication
import com.shihs.tripmood.factory.ViewModelFactory

/**
 * Extension functions for Activity.
 */
fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as TripMoodApplication).repository
    return ViewModelFactory(repository)
}

