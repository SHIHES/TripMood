package com.shihs.tripmood.home.childpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.util.HomePlanFilter
import com.shihs.tripmood.network.LoadApiStatus
import com.shihs.tripmood.util.PlanStatusFilter
import kotlinx.coroutines.*
import java.util.*

class ChildHomeViewModel(private val repository: TripMoodRepo, homePlanType: HomePlanFilter) : ViewModel() {

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




    fun navigateToDetail(plan: Plan) {
        _selectedPlan.value = plan
        Log.d("QAQ", "${_selectedPlan.value}")
    }

    init {

        Log.d("SS","ChildHomeViewModel $homePlanType")

        getLivePlansResult()

    }

    fun updatePlanStatus(plans: List<Plan>){

        val calendar = Calendar.getInstance(Locale.getDefault()).timeInMillis

        coroutineScope.launch {

            for (plan in plans){
                if (calendar > plan.startDate!! && calendar < plan.endDate!!){
                    repository.updatePlanStatus(planID = plan.id!!, PlanStatusFilter.ONGOING.code)
                } else if (calendar < plan.startDate!!){
                    repository.updatePlanStatus(planID = plan.id!!, PlanStatusFilter.PLANNING.code)
                } else{
                    repository.updatePlanStatus(planID = plan.id!!, PlanStatusFilter.END.code)
                }
            }
        }
    }



    private fun getLivePlansResult() {

        livePlans = repository.getLivePlans()

    }

    fun planSorter(homePlanType: HomePlanFilter){

        viewpagerPlans.value  = when (homePlanType) {

            HomePlanFilter.INDIVIDUAL -> livePlans.value?.filter {
                it.friends == null &&
                        it.status != PlanStatusFilter.END.code
            }
            HomePlanFilter.COWORK -> livePlans.value?.filter { it.friends != null }

        }

    }

    fun onPlanNavigated() {
        _selectedPlan.value = null
    }

    fun deletePlan(plan: Plan?) {

        coroutineScope.launch {
                    _status.value = LoadApiStatus.LOADING

                    when (val result = plan?.id?.let { repository.deletePlan(planID = it) }) {
                        is Result.Success -> {
                            _error.value = null
                            _status.value = LoadApiStatus.DONE
                        }
                        is Result.Fail -> {
                            _error.value = result.error
                            _status.value = LoadApiStatus.ERROR
                        }
                        is Result.Error -> {
                            _error.value = result.exception.toString()
                            _status.value = LoadApiStatus.ERROR
                        }
                        else -> {
                            _status.value = LoadApiStatus.ERROR
                        }
                    }
        }
    }

    fun changeToPersonal(plan: Plan){

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = plan.id?.let { repository.updatePlanToPersonal(planID = it) }) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun changeToPublic(plan: Plan){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = plan.id?.let { repository.updatePlanToPublic(planID = it) }) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }


}