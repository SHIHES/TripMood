package com.shihs.tripmood.dataclass.source

import androidx.lifecycle.MutableLiveData
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.Schedule
import com.shihs.tripmood.dataclass.source.local.TripMoodLocalDataSource

class DefaultTripMoodRepo (private val remoteDataSource: TripMoodDataSource,
                           private val localDataSource: TripMoodDataSource
                           ): TripMoodRepo {
    override suspend fun getPlans(): Result<List<Plan>> {
        return remoteDataSource.getPlans()
    }

    override fun getLivePlans(): MutableLiveData<List<Plan>> {
        return remoteDataSource.getLivePlans()
    }

    override suspend fun postPlan(plan: Plan): Result<Boolean> {
        return remoteDataSource.postPlan(plan = plan)
    }

    override suspend fun deletePlan(planID: String): Result<Boolean> {
        return remoteDataSource.deletePlan(planID)
    }

    override suspend fun deleteSchedule(planID: String, scheduleID: String): Result<Boolean>{
        return remoteDataSource.deleteSchedule(planID, scheduleID)
    }

    override fun getLiveSchedule(planID: String): MutableLiveData<List<Schedule>> {
        return remoteDataSource.getLiveSchedule(planID = planID)
    }

    override suspend fun postSchedule(planID: String, schedule: Schedule): Result<Boolean> {
        return remoteDataSource.postSchedule(planID, schedule)
    }

    override suspend fun updatePlan(plan: Plan): Result<Boolean>{
        return remoteDataSource.updatePlan(plan)
    }

    override suspend fun updateSchedule(planID: String, schedule: Schedule): Result<Boolean> {
        return remoteDataSource.updateSchedule(planID, schedule)
    }

}