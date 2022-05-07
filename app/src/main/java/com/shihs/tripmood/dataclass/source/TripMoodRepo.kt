package com.shihs.tripmood.dataclass.source

import androidx.lifecycle.MutableLiveData
import com.shihs.tripmood.dataclass.*

interface TripMoodRepo {

    suspend fun getPlans(): Result<List<Plan>>

    fun getLivePlans(): MutableLiveData<List<Plan>>

    suspend fun postPlan(plan: Plan): Result<String>

    suspend fun deletePlan(planID: String): Result<Boolean>

    suspend fun deleteSchedule(planID: String, scheduleID: String): Result<Boolean>

    fun getLiveSchedule(planID: String): MutableLiveData<List<Schedule>>

    suspend fun postSchedule(planID: String, schedule: Schedule): Result<Boolean>

    suspend fun updatePlan(plan: Plan): Result<Boolean>

    suspend fun updateSchedule(planID: String, schedule: Schedule): Result<Boolean>

    suspend fun updatePlanToPersonal(planID: String): Result<Boolean>

    suspend fun updatePlanToPublic(planID: String): Result<Boolean>

    fun getLivePublicPlan(): MutableLiveData<List<Plan>>

    suspend fun updatePlanStatus(planID: String, newStatus: Int): Result<Boolean>

    suspend fun useEmailFindUser(email: String): Result<User>

    suspend fun postPlanInvite(invite: Invite): Result<Boolean>

    suspend fun getSendReply(): Result<List<Invite>>

    suspend fun getReceiveInvite(): Result<List<Invite>>

    suspend fun acceptInvite(inviteID: String): Result<Boolean>

    suspend fun refusedInvite(inviteID: String): Result<Boolean>
}