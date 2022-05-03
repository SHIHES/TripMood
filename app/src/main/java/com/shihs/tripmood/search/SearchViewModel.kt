package com.shihs.tripmood.search

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

class SearchViewModel(private val repository: TripMoodRepo) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private var _plans = MutableLiveData<List<Plan>>()

    val plans: LiveData<List<Plan>>
        get() = _plans

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error


    private val _selectedPlan = MutableLiveData<Plan>()

    val selectedPlan: LiveData<Plan>
        get() = _selectedPlan

    init {
        getLivePlansResult()
    }

    fun navigateToDetail(plan: Plan) {
        _selectedPlan.value = plan
        Log.d("QAQ", "${_selectedPlan.value}")
    }

    private fun getLivePlansResult() {

        _plans = repository.getLivePublicPlan()
        Log.d("QAQ", "plansvalue ${_plans.value}")

    }

    fun onPlanNavigated() {
        _selectedPlan.value = null
    }

}