package com.shihs.tripmood.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.util.Logger

class HomeViewModel : ViewModel() {

    private val _selectedPlan = MutableLiveData<Plan>()

    val selectedPlan: LiveData<Plan>
        get() = _selectedPlan

    fun navigateToDetail(plan: Plan) {
        _selectedPlan.value = plan
        Log.d("SS", "${_selectedPlan.value}")
    }




}