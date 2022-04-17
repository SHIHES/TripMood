package com.shihs.tripmood.ext

import androidx.fragment.app.Fragment
import com.shihs.tripmood.TripMoodApplication
import com.shihs.tripmood.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as TripMoodApplication).repository
    return ViewModelFactory(repository)
}

//fun Fragment.getVmFactory(author: Author?): AuthorViewModelFactory {
//    val repository = (requireContext().applicationContext as TripMoodApplication).repository
//    return AuthorViewModelFactory(repository, author)
//}