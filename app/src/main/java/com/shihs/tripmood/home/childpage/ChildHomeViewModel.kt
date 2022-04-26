package com.shihs.tripmood.home.childpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.home.PlanFilter
import com.shihs.tripmood.network.LoadApiStatus

class ChildHomeViewModel(private val repository: TripMoodRepo, planType: PlanFilter) : ViewModel() {

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _selectedPlan = MutableLiveData<Plan>()

    val selectedPlan: LiveData<Plan>
        get() = _selectedPlan

    var _plans = MutableLiveData<List<Plan>>()

    val plans: LiveData<List<Plan>>
        get() = _plans

    var livePlans = MutableLiveData<List<Plan>>()


    var viewpagerPlans = MutableLiveData<List<Plan>>()




    fun navigateToDetail(plan: Plan) {
        _selectedPlan.value = plan
        Log.d("QAQ", "${_selectedPlan.value}")
    }

    init {

        Log.d("SS","ChildHomeViewModel $planType")

        getLivePlansResult()

    }



    private fun getLivePlansResult() {

        livePlans = repository.getLivePlans()

    }

    fun planSorter(planType: PlanFilter){

        viewpagerPlans.value  = when (planType) {

            PlanFilter.INDIVIDUAL -> livePlans.value?.filter { it.friends == null }
            PlanFilter.COWORK -> livePlans.value?.filter { it.friends != null }

        }

    }

    fun onPlanNavigated() {
        _selectedPlan.value = null
    }



}