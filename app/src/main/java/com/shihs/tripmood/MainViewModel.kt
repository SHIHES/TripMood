package com.shihs.tripmood

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.util.CurrentFragmentType

class MainViewModel(private val tripMoodRepository: TripMoodRepo) : ViewModel() {


    val currentFragmentType = MutableLiveData<CurrentFragmentType>()


}