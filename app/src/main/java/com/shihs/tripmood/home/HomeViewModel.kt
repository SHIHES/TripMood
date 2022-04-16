package com.shihs.tripmood.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class HomeViewModel (private val repository: TripMoodRepo): ViewModel() {


    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateToSelectedPlan = MutableLiveData<Plan?>()
    val navigateToSelectedProperty : LiveData<Plan?>
        get() = _navigateToSelectedPlan


    fun displayPlanDetails(plan: Plan){
        _navigateToSelectedPlan.value = plan

        Logger.d("displayPlanDetails ${plan}")
    }

}