package com.shihs.tripmood.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class FavoriteViewModel(private val repository: TripMoodRepo) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var favoritePlans = MutableLiveData<List<Plan>>()

    private val _selectedPlan = MutableLiveData<Plan>()

    val selectedPlan: LiveData<Plan>
        get() = _selectedPlan





    init {
        getFavoriteLivePlans()
    }

    private fun getFavoriteLivePlans() {

        favoritePlans = repository.getLiveFavoritePlan()

    }

    fun navigateToDetail(plan: Plan) {
        _selectedPlan.value = plan
        Log.d("QAQ", "${_selectedPlan.value}")
    }

    fun onPlanNavigated() {
        _selectedPlan.value = null
    }






}