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
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: TripMoodRepo) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private var _publicPlans = MutableLiveData<List<Plan>>()

    val publicPlans: LiveData<List<Plan>>
        get() = _publicPlans

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error


    private val _selectedPlan = MutableLiveData<Plan>()

    val selectedPlan: LiveData<Plan>
        get() = _selectedPlan

    private val _searchPlans = MutableLiveData<List<Plan>>()

    val searchPlans: LiveData<List<Plan>>
        get() = _searchPlans

    init {
        getLivePlansResult()
    }

    fun navigateToDetail(plan: Plan) {
        _selectedPlan.value = plan
        Log.d("QAQ", "${_selectedPlan.value}")
    }

    private fun getLivePlansResult() {

        _publicPlans = repository.getLivePublicPlan()
        Log.d("QAQ", "plansvalue ${_publicPlans.value}")

    }

    fun onPlanNavigated() {
        _selectedPlan.value = null
    }

    fun filterSearch(query: String?) {

        if (query.isNullOrBlank()) {
            _searchPlans.value = _publicPlans.value
        } else {
            _searchPlans.value = _publicPlans.value?.filter {
                it.title!!.contains(query.toString())
            }
        }

    }

    fun addFavoritePlan(plan: Plan) {  coroutineScope.launch {
        repository.addFavoritePlan(plan)

    }

    }

    fun cancelFavoritePlan(plan: Plan) { coroutineScope.launch {
        repository.cancelFavoritePlan(plan)
    }

    }

}