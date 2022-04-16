package com.shihs.tripmood.dataclass.source

import androidx.lifecycle.MutableLiveData
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Result

interface TripMoodRepo {

    suspend fun getPlans(): Result<List<Plan>>

    fun getLivePlans(): MutableLiveData<List<Plan>>

    suspend fun postPlan(plan: Plan): Result<Boolean>

    suspend fun delete(plan: Plan): Result<Boolean>
}