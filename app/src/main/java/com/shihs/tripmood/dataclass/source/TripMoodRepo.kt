package com.shihs.tripmood.dataclass.source

import androidx.lifecycle.MutableLiveData
import com.shihs.tripmood.dataclass.Plan

import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.Schedule

interface TripMoodRepo {

    suspend fun getPlans(): Result<List<Plan>>

    fun getLivePlans(): MutableLiveData<List<Plan>>

    suspend fun postPlan(plan: Plan): Result<Boolean>

    suspend fun deletePlan(planID: String): Result<Boolean>

    suspend fun deleteSchedule(planID: String, scheduleID: String): Result<Boolean>

    fun getLiveSchedule(planID: String): MutableLiveData<List<Schedule>>

    suspend fun postSchedule(planID: String, schedule: Schedule): Result<Boolean>

    suspend fun updatePlan(plan: Plan): Result<Boolean>

    suspend fun updateSchedule(planID: String, schedule: Schedule): Result<Boolean>
}