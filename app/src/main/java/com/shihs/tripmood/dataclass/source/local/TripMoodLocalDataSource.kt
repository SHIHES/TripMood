package com.shihs.tripmood.dataclass.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.source.TripMoodDataSource

class TripMoodLocalDataSource (val context: Context) : TripMoodDataSource{
    override suspend fun getPlans(): Result<List<Plan>> {
        TODO("Not yet implemented")
    }

    override fun getLivePlans(): MutableLiveData<List<Plan>> {
        TODO("Not yet implemented")
    }

    override suspend fun postPlan(plan: Plan): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(plan: Plan): Result<Boolean> {
        TODO("Not yet implemented")
    }
}