package com.shihs.tripmood.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.source.TripMoodRepo

class FavoriteViewModel(private val repository: TripMoodRepo) : ViewModel() {
    
    var favoritePlans = MutableLiveData<List<Plan>>()

    private val _selectedPlan = MutableLiveData<Plan?>()

    val selectedPlan: LiveData<Plan?>
        get() = _selectedPlan

    init {
        getFavoriteLivePlans()
    }

    private fun getFavoriteLivePlans() {
        favoritePlans = repository.getLiveFavoritePlan()
    }

    fun navigateToDetail(plan: Plan) {
        _selectedPlan.value = plan
    }

    fun onPlanNavigated() {
        _selectedPlan.value = null
    }
}
