package com.shihs.tripmood.dataclass.source.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.shihs.tripmood.dataclass.*
import com.shihs.tripmood.dataclass.source.TripMoodDataSource

class TripMoodLocalDataSource (val context: Context) : TripMoodDataSource {
    override suspend fun getPlans(): Result<List<Plan>> {
        TODO("Not yet implemented")
    }

    override fun getLivePlans(): MutableLiveData<List<Plan>> {
        TODO("Not yet implemented")
    }

    override suspend fun postPlan(plan: Plan): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlan(planID: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSchedule(planID: String, scheduleID: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getLiveSchedule(planID: String): MutableLiveData<List<Schedule>> {
        TODO("Not yet implemented")
    }

    override suspend fun postSchedule(planID: String, schedule: Schedule): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlan(plan: Plan): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSchedule(planID: String, schedule: Schedule): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlanToPersonal(planID: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlanToPublic(planID: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getLivePublicPlan(): MutableLiveData<List<Plan>> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlanStatus(planID: String, newStatus: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun useEmailFindUser(email: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun postPlanInvite(invite: Invite): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getSendReply(): Result<List<Invite>> {
        TODO("Not yet implemented")
    }

    override suspend fun getReceiveInvite(): Result<List<Invite>> {
        TODO("Not yet implemented")
    }

    override suspend fun acceptInvite(inviteID: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun refusedInvite(inviteID: String): Result<Boolean> {
        TODO("Not yet implemented")
    }


}