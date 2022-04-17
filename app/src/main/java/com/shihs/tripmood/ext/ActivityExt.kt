package com.shihs.tripmood.ext

import android.app.Activity
import android.view.Gravity
import android.widget.Toast
import com.shihs.tripmood.TripMoodApplication
import com.shihs.tripmood.factory.ViewModelFactory

fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (applicationContext as TripMoodApplication).repository
    return ViewModelFactory(repository)
}

fun Activity?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }
}