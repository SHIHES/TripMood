package com.shihs.tripmood.dataclass.source.repository

import androidx.lifecycle.MutableLiveData
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.source.TripMoodDataSource
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.dataclass.source.local.TripMoodLocalDataSource

class DefaultTripMoodRepo(
    private val remoteDataSource: TripMoodDataSource,
    private val localDataSource: TripMoodDataSource
) : TripMoodRepo {
    override suspend fun getPlans(): Result<List<Plan>> {
        return remoteDataSource.getPlans()
    }

    override fun getLivePlans(): MutableLiveData<List<Plan>> {
        return remoteDataSource.getLivePlans()
    }

    override suspend fun postPlan(plan: Plan): Result<Boolean> {
        return remoteDataSource.postPlan(plan)
    }

    override suspend fun delete(plan: Plan): Result<Boolean> {
        return remoteDataSource.delete(plan)
    }
}