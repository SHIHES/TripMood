package com.shihs.tripmood.dataclass.source.remote

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.shihs.tripmood.dataclass.*
import com.shihs.tripmood.dataclass.source.TripMoodDataSource
import com.shihs.tripmood.util.InviteFilter
import com.shihs.tripmood.util.Logger
import com.shihs.tripmood.util.Me
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object TripMoodRemoteDataSource : TripMoodDataSource {

    private const val PATH_PLANS = "plans"
    private const val PATH_SCHEDULES = "schedules"
    private const val PATH_USERS = "users"
    private const val PATH_INVITES = "invites"
    private const val KEY_INVITE_STATUS = "status"
    private const val KEY_EMAIL = "email"
    private const val KEY_STARTDATE = "startDate"
    private const val KEY_PRIVATE = "private"
    private const val KEY_PLAN_STATUS = "status"
    private const val KEY_SENDERID = "senderID"
    private const val KEY_RECEIVERID = "receiverID"


    override suspend fun getPlans(): Result<List<Plan>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
            .orderBy(KEY_STARTDATE, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val list = mutableListOf<Plan>()
                    Logger.i("Find addOnCompleteListener")
                    for (document in task.result){
                        Logger.d(document.id + " => " + document.data)

                        val plan = document.toObject(Plan::class.java)
                        list.add(plan)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail("getPlans fail"))
                }
            }
    }

    override fun getLivePlans(): MutableLiveData<List<Plan>> {

        val liveData = MutableLiveData<List<Plan>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
            .orderBy(KEY_STARTDATE, Query.Direction.DESCENDING)
            .addSnapshotListener{ snapshot, exception ->

                Logger.i("getLivePlans addSnapshotLister success")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Plan>()
                for (document in snapshot!!){
                    Logger.d(document.id + " => " + document.data)

                    val article = document.toObject(Plan::class.java)
                    list.add(article)
                }
                liveData.value = list
            }
        return liveData
    }

    override fun getLiveSchedule(planID : String): MutableLiveData<List<Schedule>> {

        val liveData = MutableLiveData<List<Schedule>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
            .document(planID)
            .collection(PATH_SCHEDULES)
            .addSnapshotListener { snapshot, error ->

                Logger.i("getLiveSchedule addSnapshotLister success")

                error?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list= mutableListOf<Schedule>()
                for (document in snapshot!!){
                    Logger.d(document.id + " => " + document.data)

                    var schedule = document.toObject(Schedule::class.java)
                    list.add(schedule)
                }
                liveData.value = list

                Logger.i("SHOWALLLOCATION${liveData.value}")

            }
        return liveData
    }

    override suspend fun postPlan(plan: Plan): Result<String> = suspendCoroutine{ continuation ->
        val plans = FirebaseFirestore.getInstance().collection(PATH_PLANS)
        val document = plans.document()


        val user = User(name = "Steven", email = "test@gamil.com", id = "test")


        plan.id = document.id
        plan.owner = user

        document.set(plan).addOnCompleteListener{ task ->
            if (task.isSuccessful){

                Logger.i("Publish: $plan")

                continuation.resume(Result.Success(document.id))
            } else{
                task.exception?.let {
                    Logger.w("[${this::class.simpleName}] Error posting documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                    return@addOnCompleteListener
                }
                continuation.resume(Result.Fail("postPlan Fail"))
            }
        }
    }

    override suspend fun postSchedule(planID: String, schedule: Schedule): Result<Boolean> = suspendCoroutine{ continuation ->
        val plans = FirebaseFirestore.getInstance().collection(PATH_PLANS)
                    .document(planID).collection(PATH_SCHEDULES)
        val document = plans.document()

        schedule.scheduleId = document.id
        schedule.planID = planID

        document.set(schedule).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                Logger.i("Publish postSchedule: $schedule")

                continuation.resume(Result.Success(true))
            } else{
                task.exception?.let {
                    Logger.w("[${this::class.simpleName}] Error posting documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                    return@addOnCompleteListener
                }
                continuation.resume(Result.Fail("postSchedule Fail"))
            }
        }
    }



    override suspend fun deletePlan(planID: String): Result<Boolean> = suspendCoroutine { continuation ->
        planID.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .document(it)
                .delete()
                .addOnSuccessListener {
                    Logger.i("Delete: $planID")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w("[${this::class.simpleName}] Error delete plan documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                }
        }
    }

    override suspend fun deleteSchedule(planID: String, scheduleID: String): Result<Boolean> = suspendCoroutine { continuation ->
        planID.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .document(it)
                .collection(PATH_SCHEDULES)
                .document(scheduleID)
                .delete()
                .addOnSuccessListener {
                    Logger.i("Delete: $scheduleID")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w("[${this::class.simpleName}] Error delete Schedule documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                }
        }
    }

    override suspend fun updatePlan(plan: Plan): Result<Boolean> = suspendCoroutine { continuation ->
        plan.id?.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .document(it)
                .set(plan)
                .addOnSuccessListener {
                    Logger.i("Update: $plan")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w("[${this::class.simpleName}] Error updatePlan documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                }
        }
    }

    override suspend fun updateSchedule(planID: String, schedule: Schedule): Result<Boolean> = suspendCoroutine { continuation ->
        planID.let {
            schedule.scheduleId?.let { it1 ->
                FirebaseFirestore.getInstance()
                    .collection(PATH_PLANS)
                    .document(it)
                    .collection(PATH_SCHEDULES)
                    .document(it1)
                    .set(schedule)
                    .addOnSuccessListener {
                        Logger.i("Update: $schedule")

                        continuation.resume(Result.Success(true))
                    }
                    .addOnFailureListener {
                        Logger.w("[${this::class.simpleName}] Error updateSchedule documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                    }
            }
        }
    }

    override suspend fun updatePlanToPersonal(planID: String): Result<Boolean> = suspendCoroutine{ continuation ->
        planID.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .document(it)
                .update(KEY_PRIVATE,true)
                .addOnSuccessListener {
                    Logger.i("updatePlanToPersonal: $it")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w("[${this::class.simpleName}] Error updatePlan documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                }
        }

    }

    override suspend fun updatePlanToPublic(planID: String): Result<Boolean> = suspendCoroutine {continuation ->
        planID.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .document(it)
                .update(KEY_PRIVATE,false)
                .addOnSuccessListener {
                    Logger.i("updatePlanToPublic: $it")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w("[${this::class.simpleName}] Error updatePlan documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                }
        }
    }

    override fun getLivePublicPlan(): MutableLiveData<List<Plan>> {
        val liveData = MutableLiveData<List<Plan>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
//            .orderBy(KEY_STARTDATE, Query.Direction.DESCENDING)
            .whereEqualTo(KEY_PRIVATE, false)
            .addSnapshotListener{ snapshot, exception ->

                Logger.i("getLivePublicPlan success")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Plan>()
                if (snapshot != null) {
                    for (document in snapshot){
                        Logger.d(document.id + " => " + document.data)

                        val article = document.toObject(Plan::class.java)
                        list.add(article)
                    }
                }

                liveData.value = list

            }
        return liveData
    }

    override suspend fun updatePlanStatus(planID: String, newStatus: Int): Result<Boolean> = suspendCoroutine {continuation ->
        planID.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .document(it)
                .update(KEY_PLAN_STATUS, newStatus)
                .addOnSuccessListener {

                    Logger.i("updatePlanStatus: $it")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w("[${this::class.simpleName}] Error updatePlanStatus documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                }
        }
    }

    override suspend fun useEmailFindUser(email: String): Result<User> = suspendCoroutine{ continuation ->
        email.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_USERS)
                .whereEqualTo(KEY_EMAIL, email)
                .get()
                .addOnCompleteListener{ task ->

                    if (task.isSuccessful){

                        Logger.i("useEmailFindUserID addOnCompleteListener ${task.result.documents.size}")
                        for (document in task.result){
                            Logger.d(document.id + " => " + document.data)

                            val user = document.toObject(User::class.java)
//                            list.add(user)
                            continuation.resume(Result.Success(user))
                        }
//                        val userID = list[0].id.toString()

                    } else {
                        task.exception?.let {
                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("useEmailFindUserID fail"))
                    }
                }
        }
    }

    override suspend fun postPlanInvite(invite: Invite): Result<Boolean> = suspendCoroutine { continuation ->

        val invites = FirebaseFirestore.getInstance().collection(PATH_INVITES)
        val document = invites.document()

        invite.id = document.id

        document.set(invite).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                Logger.i("sendPlanInvite $invite")

                continuation.resume(Result.Success(true))
            } else{
                task.exception?.let {
                    Logger.w("[${this::class.simpleName}] Error posting documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                    return@addOnCompleteListener
                }
                continuation.resume(Result.Fail("postSchedule Fail"))
            }
        }
    }


    override suspend fun getSendReply(): Result<List<Invite>> = suspendCoroutine { continuation ->

        FirebaseFirestore.getInstance().collection(PATH_INVITES)
            .whereEqualTo(KEY_RECEIVERID, Me.user.id)
            .whereEqualTo(KEY_INVITE_STATUS, InviteFilter.REFUSED.status)
            .whereEqualTo(KEY_INVITE_STATUS, InviteFilter.APPROVAL.status)
            .get()
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val list = mutableListOf<Invite>()
                    Logger.i("Find getInvite addOnCompleteListener")
                    for (document in task.result){

                        Logger.d(document.id + " => " + document.data)

                        val invite = document.toObject(Invite::class.java)

                        Logger.i("getInvite $invite")

                        list.add(invite)

                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail("getPlans fail"))
                }
            }
    }

    override suspend fun getReceiveInvite(): Result<List<Invite>> = suspendCoroutine { continuation ->

        FirebaseFirestore.getInstance().collection(PATH_INVITES)
            .whereEqualTo(KEY_INVITE_STATUS,InviteFilter.WAITING.status)
            .whereEqualTo(KEY_RECEIVERID, Me.user.id)
            .get()
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val list = mutableListOf<Invite>()
                    Logger.i("Find getInvite addOnCompleteListener")
                    for (document in task.result){

                        Logger.d(document.id + " => " + document.data)

                        val invite = document.toObject(Invite::class.java)

                        Logger.i("getInvite $invite")

                        list.add(invite)

                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail("getPlans fail"))
                }
            }
    }

    override suspend fun acceptInvite(inviteID: String): Result<Boolean> = suspendCoroutine { continuation ->

        FirebaseFirestore.getInstance()
            .collection(PATH_INVITES)
            .document(inviteID)
    }

    override suspend fun refusedInvite(inviteID: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

}