package com.shihs.tripmood.dataclass.source

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.shihs.tripmood.dataclass.*

import kotlin.coroutines.suspendCoroutine

interface TripMoodDataSource {

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

    suspend fun refusedInvite(inviteID: String): Result<Boolean>

    suspend fun acceptInviteChangeStatus(inviteID: String): Result<Boolean>

    suspend fun acceptInviteAddUserToPlan(planID: String, user: User): Result<Boolean>

    fun getLiveChats(planID: String): MutableLiveData<List<Chat>>

    suspend fun postChats(chat: Chat): Result<Boolean>

    suspend fun postUser(user: User): Result<String>

    suspend fun checkUserExist(userID: String): Result<User>

    fun getCoWorkLivePlan(): MutableLiveData<List<Plan>>

    suspend fun getUserInfo(userID: String) : Result<User>

    fun getLiveCoworkLocation(): MutableLiveData<List<UserLocation>>

    suspend fun sendMyLocation(userLocation: UserLocation ) : Result<Boolean>

    suspend fun updateMyLocation(lat:Double, lng: Double) : Result<Boolean>

    suspend fun uploadImage(localUri: Uri) : Result<Uri>

    suspend fun addFavoritePlan(planID: String): Result<Boolean>

    suspend fun cancelFavoritePlan(planID: String): Result<Boolean>

    }