package com.shihs.tripmood.dataclass.source

import androidx.lifecycle.MutableLiveData
import com.shihs.tripmood.dataclass.*

class DefaultTripMoodRepo (private val remoteDataSource: TripMoodDataSource,
                           private val localDataSource: TripMoodDataSource
                           ): TripMoodRepo {
    override suspend fun getPlans(): Result<List<Plan>> {
        return remoteDataSource.getPlans()
    }

    override fun getLivePlans(): MutableLiveData<List<Plan>> {
        return remoteDataSource.getLivePlans()
    }

    override suspend fun postPlan(plan: Plan): Result<String> {
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

    override suspend fun updatePlanToPersonal(planID: String): Result<Boolean> {
        return remoteDataSource.updatePlanToPersonal(planID = planID)
    }

    override suspend fun updatePlanToPublic(planID: String): Result<Boolean> {
        return remoteDataSource.updatePlanToPublic(planID = planID)
    }

    override fun getLivePublicPlan(): MutableLiveData<List<Plan>> {
        return remoteDataSource.getLivePublicPlan()
    }

    override suspend fun updatePlanStatus(planID: String, newStatus: Int): Result<Boolean> {
        return remoteDataSource.updatePlanStatus(planID = planID, newStatus = newStatus)
    }

    override suspend fun useEmailFindUser(email: String): Result<User> {
        return remoteDataSource.useEmailFindUser(email = email)
    }

    override suspend fun postPlanInvite(invite: Invite): Result<Boolean> {
        return remoteDataSource.postPlanInvite(invite = invite)
    }

    override suspend fun getSendReply(): Result<List<Invite>> {
        return remoteDataSource.getSendReply()
    }

    override suspend fun getReceiveInvite(): Result<List<Invite>> {
        return remoteDataSource.getReceiveInvite()
    }

    override suspend fun acceptInviteChangeStatus(inviteID: String): Result<Boolean> {
        return remoteDataSource.acceptInviteChangeStatus(inviteID = inviteID)
    }

    override suspend fun acceptInviteAddUserToPlan(planID: String, user: User): Result<Boolean> {
        return remoteDataSource.acceptInviteAddUserToPlan(planID = planID, user = user)
    }

    override suspend fun refusedInvite(inviteID: String): Result<Boolean> {
        return remoteDataSource.refusedInvite(inviteID = inviteID)
    }

    override fun getLiveChats(planID: String): MutableLiveData<List<Chat>> {
        return remoteDataSource.getLiveChats(planID = planID)
    }

    override suspend fun postChats(chat: Chat): Result<Boolean> {
        return remoteDataSource.postChats(chat = chat)
    }

    override suspend fun postUser(user: User): Result<String> {
        return remoteDataSource.postUser(user = user)
    }

    override suspend fun checkUserExist(userID: String): Result<User> {
        return remoteDataSource.checkUserExist(userID = userID)
    }

    override fun getCoWorkLivePlan(): MutableLiveData<List<Plan>> {
        return remoteDataSource.getCoWorkLivePlan()
    }

}