package com.shihs.tripmood.user.child

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.network.LoadApiStatus
import com.shihs.tripmood.util.PlanStatusFilter
import com.shihs.tripmood.util.UserPlanFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class UserChildViewModel(private val repository: TripMoodRepo, userPlanFilter: UserPlanFilter) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    private val _selectedPlan = MutableLiveData<Plan>()

    val selectedPlan: LiveData<Plan>
        get() = _selectedPlan

    private var _plans = MutableLiveData<List<Plan>>()

    val plans: LiveData<List<Plan>>
        get() = _plans

    var livePlans = MutableLiveData<List<Plan>>()

    var viewpagerPlans = MutableLiveData<List<Plan>>()

    init {

        getLivePlansResult()
    }

    fun navigateToDetail(plan: Plan) {
        _selectedPlan.value = plan
        Log.d("QAQ", "${_selectedPlan.value}")
    }

    private fun getLivePlansResult() {
        livePlans = repository.getLivePlans()
    }

    fun onPlanNavigated() {
        _selectedPlan.value = null
    }

    val plan = mutableListOf<Plan>()

    fun planSorter(userPlanType: UserPlanFilter) {
        viewpagerPlans.value = when (userPlanType) {
            UserPlanFilter.MEMORY -> livePlans.value?.filter {
                it.status == PlanStatusFilter.END.code
            }
            UserPlanFilter.COLLECTION -> livePlans.value
        }
    }
}
