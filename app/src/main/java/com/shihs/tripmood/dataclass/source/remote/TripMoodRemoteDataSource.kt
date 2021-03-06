package com.shihs.tripmood.dataclass.source.remote

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.shihs.tripmood.dataclass.*
import com.shihs.tripmood.dataclass.source.TripMoodDataSource
import com.shihs.tripmood.util.InviteFilter
import com.shihs.tripmood.util.Logger
import com.shihs.tripmood.util.UserManager
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object TripMoodRemoteDataSource : TripMoodDataSource {

    private const val PATH_PLANS = "plans"
    private const val PATH_SCHEDULES = "schedules"
    private const val PATH_USERS = "users"
    private const val PATH_CHATS = "chats"
    private const val PATH_INVITES = "invites"
    private const val PATH_COWORKING_LOCATION = "coworkingLocation"
    private const val PATH_FAVORITE = "favoritePlan"
    
    private const val KEY_LATITUDE = "lat"
    private const val KEY_LONGITUDE = "lng"
    private const val KEY_CHATS_CREATED_TIME = "createdTime"
    private const val KEY_OWNER = "ownerID"
    private const val KEY_UID = "uid"
    private const val KEY_COWORKING_LIST = "coworkingList"
    private const val KEY_INVITE_STATUS = "status"
    private const val KEY_EMAIL = "email"
    private const val KEY_START_DATE = "startDate"
    private const val KEY_PRIVATE = "private"
    private const val KEY_PLAN_STATUS = "status"
    private const val KEY_SENDER_ID = "senderID"
    private const val KEY_RECEIVER_ID = "receiverID"

    override suspend fun getPlans(): Result<List<Plan>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
            .orderBy(KEY_START_DATE, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Plan>()
                    Logger.i("Find addOnCompleteListener")
                    for (document in task.result) {
                        Logger.d(document.id + " => " + document.data)

                        val plan = document.toObject(Plan::class.java)
                        list.add(plan)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        Logger.w(
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
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
            .whereEqualTo(KEY_OWNER, UserManager.userUID)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("getLivePlans addSnapshotLister success ${UserManager.userUID}")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Plan>()
                if (snapshot != null) {
                    for (document in snapshot) {
                        Logger.d(document.id + " => " + document.data)
        
                        val article = document.toObject(Plan::class.java)
                        list.add(article)
                    }
                }
                liveData.value = list
            }
        return liveData
    }

    override fun getLiveSchedule(planID: String): MutableLiveData<List<Schedule>> {
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

                val list = mutableListOf<Schedule>()
                if (snapshot != null) {
                    for (document in snapshot) {
                        Logger.d(document.id + " => " + document.data)
        
                        val schedule = document.toObject(Schedule::class.java)
                        list.add(schedule)
                    }
                }
                liveData.value = list
            }
        return liveData
    }

    override suspend fun postPlan(plan: Plan): Result<String> = suspendCoroutine { continuation ->
        val plans = FirebaseFirestore.getInstance().collection(PATH_PLANS)
        val document = plans.document()

        plan.id = document.id
        plan.ownerID = UserManager.userUID

        document.set(plan).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Logger.i("Publish: $plan")

                continuation.resume(Result.Success(document.id))
            } else {
                task.exception?.let {
                    Logger.w("[${this::class.simpleName}] Error posting documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                    return@addOnCompleteListener
                }
                continuation.resume(Result.Fail("postPlan Fail"))
            }
        }
    }

    override suspend fun postSchedule(planID: String, schedule: Schedule): Result<Boolean> =
        suspendCoroutine { continuation ->
            val plans = FirebaseFirestore.getInstance().collection(PATH_PLANS)
                .document(planID).collection(PATH_SCHEDULES)
            val document = plans.document()

            schedule.scheduleId = document.id
            schedule.planID = planID

            document.set(schedule).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.i("Publish postSchedule: $schedule")

                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {
                        Logger.w(
                            "[${this::class.simpleName}] Error posting documents. ${it.message}"
                        )
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail("postSchedule Fail"))
                }
            }
        }

    override suspend fun deletePlan(planID: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            planID.let {
                FirebaseFirestore.getInstance()
                    .collection(PATH_PLANS)
                    .document(it)
                    .delete()
                    .addOnSuccessListener {
                        Logger.i("Delete: $planID")

                        continuation.resume(Result.Success(true))
                    }
                    .addOnFailureListener { exception ->
                        Logger.w(
                            "[${this::class.simpleName}] Error delete plan documents. ${exception.message}"
                        )
                        continuation.resume(Result.Error(exception))
                    }
            }
        }

    override suspend fun deleteSchedule(planID: String, scheduleID: String): Result<Boolean> =
        suspendCoroutine { continuation ->
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
                    .addOnFailureListener { e ->
                        Logger.w(
                            "[${this::class.simpleName}] Error delete Schedule documents. ${e.message}"
                        )
                        continuation.resume(Result.Error(e))
                    }
            }
        }

    override suspend fun updatePlan(plan: Plan): Result<Boolean> =
        suspendCoroutine { continuation ->
            plan.id?.let {
                FirebaseFirestore.getInstance()
                    .collection(PATH_PLANS)
                    .document(it)
                    .set(plan)
                    .addOnSuccessListener {
                        Logger.i("Update: $plan")

                        continuation.resume(Result.Success(true))
                    }
                    .addOnFailureListener { e ->
                        Logger.w(
                            "[${this::class.simpleName}] Error updatePlan documents. ${e.message}"
                        )
                        continuation.resume(Result.Error(e))
                    }
            }
        }

    override suspend fun updateSchedule(planID: String, schedule: Schedule): Result<Boolean> =
        suspendCoroutine { continuation ->
                    FirebaseFirestore.getInstance()
                        .collection(PATH_PLANS)
                        .document(planID)
                        .collection(PATH_SCHEDULES)
                        .document(schedule.scheduleId ?: return@suspendCoroutine)
                        .set(schedule)
                        .addOnSuccessListener {
                            Logger.i("Update: $schedule")

                            continuation.resume(Result.Success(true))
                        }
                        .addOnFailureListener {
                            Logger.w(
                                "[${this::class.simpleName}] Error updateSchedule documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                        }
                
            
        }

    override suspend fun updatePlanToPersonal(planID: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            planID.let {
                FirebaseFirestore.getInstance()
                    .collection(PATH_PLANS)
                    .document(it)
                    .update(KEY_PRIVATE, true)
                    .addOnSuccessListener {
                        continuation.resume(Result.Success(true))
                    }
                    .addOnFailureListener { e ->
                        Logger.w(
                            "[${this::class.simpleName}] Error updatePlan documents. ${e.message}"
                        )
                        continuation.resume(Result.Error(e))
                    }
            }
        }

    override suspend fun updatePlanToPublic(planID: String): Result<Boolean> =
        suspendCoroutine { continuation ->
            planID.let {
                FirebaseFirestore.getInstance()
                    .collection(PATH_PLANS)
                    .document(it)
                    .update(KEY_PRIVATE, false)
                    .addOnSuccessListener {
                        continuation.resume(Result.Success(true))
                    }
                    .addOnFailureListener { e ->
                        Logger.w(
                            "[${this::class.simpleName}] Error updatePlan documents. ${e.message}"
                        )
                        continuation.resume(Result.Error(e))
                    }
            }
        }

    override fun getCoworkingLivePlan(): MutableLiveData<List<Plan>> {
        val liveData = MutableLiveData<List<Plan>>()

        UserManager.userUID?.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .whereArrayContains(KEY_COWORKING_LIST, it)
                .addSnapshotListener { snapshot, exception ->

                    Logger.i("getCoWorkLivePlan success")

                    exception?.let { e ->
                        Logger.w(
                            "[${this::class.simpleName}] Error getting documents. ${e.message}"
                        )
                    }

                    val list = mutableListOf<Plan>()
                    if (snapshot != null) {
                        for (document in snapshot) {
                            Logger.d(document.id + " getCoWorkLivePlan => " + document.data)

                            val plan = document.toObject(Plan::class.java)
                            list.add(plan)
                        }
                    }

                    liveData.value = list
                }
        }
        return liveData
    }

    override fun getLivePublicPlan(): MutableLiveData<List<Plan>> {
        val liveData = MutableLiveData<List<Plan>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
            .whereEqualTo(KEY_PRIVATE, false)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("getLivePublicPlan success")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Plan>()
                if (snapshot != null) {
                    for (document in snapshot) {
                        Logger.d(document.id + " => " + document.data)

                        val article = document.toObject(Plan::class.java)
                        list.add(article)
                    }
                }

                liveData.value = list
            }
        return liveData
    }

    override suspend fun updatePlanStatus(planID: String, newStatus: Int): Result<Boolean> =
        suspendCoroutine { continuation ->
            planID.let {
                FirebaseFirestore.getInstance()
                    .collection(PATH_PLANS)
                    .document(it)
                    .update(KEY_PLAN_STATUS, newStatus)
                    .addOnSuccessListener {
                        continuation.resume(Result.Success(true))
                    }
                    .addOnFailureListener { e ->
                        Logger.w(
                            "[${this::class.simpleName}] Error updatePlanStatus documents. ${e.message}"
                        )
                        continuation.resume(Result.Error(e))
                    }
            }
        }

    override suspend fun useEmailFindUser(email: String): Result<User> =
        suspendCoroutine { continuation ->
            email.let {
                FirebaseFirestore.getInstance()
                    .collection(PATH_USERS)
                    .whereEqualTo(KEY_EMAIL, email)
                    .get()
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            Logger.i(
                                "useEmailFindUserID addOnCompleteListener ${task.result.documents.size}"
                            )
                            for (document in task.result) {
                                Logger.d(document.id + " => " + document.data)

                                val user = document.toObject(User::class.java)
                                continuation.resume(Result.Success(user))
                            }
                        } else {
                            task.exception?.let {
                                Logger.w(
                                    "[${this::class.simpleName}] Error getting documents. ${it.message}"
                                )
                                continuation.resume(Result.Error(it))
                                return@addOnCompleteListener
                            }
                            continuation.resume(Result.Fail("useEmailFindUserID fail"))
                        }
                    }
            }
        }

    override suspend fun postPlanInvite(invite: Invite): Result<Boolean> =
        suspendCoroutine { continuation ->

            val invites = FirebaseFirestore.getInstance().collection(PATH_INVITES)
            val document = invites.document()

            invite.id = document.id

            document.set(invite).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Logger.i("sendPlanInvite $invite")

                    continuation.resume(Result.Success(true))
                } else {
                    task.exception?.let {
                        Logger.w(
                            "[${this::class.simpleName}] Error posting documents. ${it.message}"
                        )
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail("postSchedule Fail"))
                }
            }
        }

    override suspend fun getSendReply(): Result<List<Invite>> = suspendCoroutine { continuation ->

        FirebaseFirestore.getInstance().collection(PATH_INVITES)
            .whereEqualTo(KEY_SENDER_ID, UserManager.userUID)
            .whereEqualTo(KEY_INVITE_STATUS, InviteFilter.REFUSED.status)
            .whereEqualTo(KEY_INVITE_STATUS, InviteFilter.APPROVAL.status)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Invite>()
                    Logger.i("Find getInvite addOnCompleteListener")
                    for (document in task.result) {
                        Logger.d(document.id + " => " + document.data)

                        val invite = document.toObject(Invite::class.java)

                        Logger.i("getInvite $invite")

                        list.add(invite)
                    }
                    continuation.resume(Result.Success(list))
                } else {
                    task.exception?.let {
                        Logger.w(
                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                        )
                        continuation.resume(Result.Error(it))
                        return@addOnCompleteListener
                    }
                    continuation.resume(Result.Fail("getPlans fail"))
                }
            }
    }

    override suspend fun getReceiveInvite(): Result<List<Invite>> =
        suspendCoroutine { continuation ->

            FirebaseFirestore.getInstance().collection(PATH_INVITES)
                .whereEqualTo(KEY_INVITE_STATUS, InviteFilter.WAITING.status)
                .whereEqualTo(KEY_RECEIVER_ID, UserManager.userUID)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = mutableListOf<Invite>()
                        Logger.i("Find getInvite addOnCompleteListener")
                        for (document in task.result) {
                            Logger.d(document.id + " => " + document.data)

                            val invite = document.toObject(Invite::class.java)

                            Logger.i("getInvite $invite")

                            list.add(invite)
                        }
                        continuation.resume(Result.Success(list))
                    } else {
                        task.exception?.let {
                            Logger.w(
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("getPlans fail"))
                    }
                }
        }

    override suspend fun acceptInviteChangeStatus(inviteID: String): Result<Boolean> =
        suspendCoroutine { continuation ->

            FirebaseFirestore.getInstance()
                .collection(PATH_INVITES)
                .document(inviteID)
                .update(KEY_PLAN_STATUS, InviteFilter.APPROVAL.status)
                .addOnSuccessListener {
                    Logger.i("acceptInvite: $it")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w(
                        "[${this::class.simpleName}] Error acceptInvite documents. ${it.message}"
                    )
                    continuation.resume(Result.Error(it))
                }
        }

    override suspend fun acceptInviteAddUserToPlan(planID: String, user: User): Result<Boolean> =
        suspendCoroutine { continuation ->

            val db = FirebaseFirestore.getInstance()
            val ref = db.collection(PATH_PLANS).document(planID)

            db.runBatch { batch ->
                batch.update(ref, KEY_COWORKING_LIST, FieldValue.arrayUnion(user.uid))
                batch.update(ref, KEY_COWORKING_LIST, FieldValue.arrayUnion(UserManager.userUID))
            }
                .addOnSuccessListener {
                    Logger.i("acceptInvite: $it")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w(
                        "[${this::class.simpleName}] Error acceptInvite documents. ${it.message}"
                    )
                    continuation.resume(Result.Error(it))
                }
        }

    override suspend fun refusedInvite(inviteID: String): Result<Boolean> =
        suspendCoroutine { continuation ->

            FirebaseFirestore.getInstance()
                .collection(PATH_INVITES)
                .document(inviteID)
                .update(KEY_PLAN_STATUS, InviteFilter.REFUSED.status)
                .addOnSuccessListener {
                    Logger.i("acceptInvite: $it")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w(
                        "[${this::class.simpleName}] Error acceptInvite documents. ${it.message}"
                    )
                    continuation.resume(Result.Error(it))
                }
        }

    override fun getLiveChats(planID: String): MutableLiveData<List<Chat>> {
        val liveData = MutableLiveData<List<Chat>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
            .document(planID)
            .collection(PATH_CHATS)
            .orderBy(KEY_CHATS_CREATED_TIME, Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("getLiveChats addSnapshotLister success")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Chat>()
                if (snapshot != null) {
                    for (document in snapshot) {
                        Logger.d(document.id + " => " + document.data)
        
                        val chat = document.toObject(Chat::class.java)
                        list.add(chat)
                    }
                }
                liveData.value = list
            }
        return liveData
    }

    override suspend fun postChats(chat: Chat): Result<Boolean> = suspendCoroutine { continuation ->
        val chats = chat.planID?.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .document(it)
                .collection(PATH_CHATS)
        }

        val document = chats?.document()

        chat.id = document?.id

        document?.set(chat)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Logger.i("postChats: $chat")

                continuation.resume(Result.Success(true))
            } else {
                task.exception?.let {
                    Logger.w("[${this::class.simpleName}] Error postChats. ${it.message}")
                    continuation.resume(Result.Error(it))
                    return@addOnCompleteListener
                }
                continuation.resume(Result.Fail("postChats Fail"))
            }
        }
    }

    override suspend fun postUser(user: User): Result<String> = suspendCoroutine { continuation ->
        val users = FirebaseFirestore.getInstance().collection(PATH_USERS)

        val document = users.document(user.uid ?: return@suspendCoroutine)

        document.set(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Logger.i("postUser: $user")

                continuation.resume(Result.Success(document.id))
            } else {
                task.exception?.let {
                    Logger.w("[${this::class.simpleName}] Error postUser. ${it.message}")
                    continuation.resume(Result.Error(it))
                    return@addOnCompleteListener
                }
                continuation.resume(Result.Fail("postUser Fail"))
            }
        }
    }

    override suspend fun checkUserExist(userID: String): Result<User> =
        suspendCoroutine { continuation ->

            FirebaseFirestore.getInstance().collection(PATH_USERS)
                .whereEqualTo(KEY_UID, userID)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var realUser = User()

                        Logger.i("Find checkUserExist addOnCompleteListener")
                        Logger.i("Find checkUserExist ${task.result.size()}")
                        for (document in task.result) {
                            Logger.d(document.id + " => " + document.data)

                            val user = document.toObject(User::class.java)

                            realUser = user

                            Logger.i("checkUserExist $user")
                        }

                        continuation.resume(Result.Success(realUser))
                    } else {
                        task.exception?.let {
                            Logger.w(
                                "[${this::class.simpleName}] Error getting documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("checkUserExist fail"))
                    }
                }
        }

    override suspend fun getUserInfo(userID: String): Result<User> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection(PATH_USERS)
                .whereEqualTo(KEY_UID, userID)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("Find addOnCompleteListener getUserInfo")
                        for (document in task.result) {
                            Logger.d(document.id + " => " + document.data)

                            val user = document.toObject(User::class.java)

                            continuation.resume(Result.Success(user))
                        }
                    } else {
                        task.exception?.let {
                            Logger.w(
                                "[${this::class.simpleName}] Error getting getUserInfo documents. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("getUserInfo fail"))
                    }
                }
        }

    override suspend fun sendMyLocation(userLocation: UserLocation): Result<Boolean> =
        suspendCoroutine { continuation ->

            FirebaseFirestore.getInstance()
                .collection(PATH_COWORKING_LOCATION)
                .document(UserManager.userUID ?: return@suspendCoroutine)
                .set(userLocation)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Logger.i("sendMyLocation: $userLocation")

                        continuation.resume(Result.Success(true))
                    } else {
                        task.exception?.let {
                            Logger.w(
                                "[${this::class.simpleName}] Error sendMyLocation. ${it.message}"
                            )
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(Result.Fail("sendMyLocation Fail"))
                    }
                }
        }

    override suspend fun updateMyLocation(lat: Double, lng: Double): Result<Boolean> =
        suspendCoroutine { continuation ->

            val db = FirebaseFirestore.getInstance()
            val ref = db.collection(PATH_COWORKING_LOCATION)
                .document(UserManager.userUID ?: return@suspendCoroutine)

            db.runBatch { batch ->
                batch.update(ref, KEY_LONGITUDE, lng)
                batch.update(ref, KEY_LATITUDE, lat)
            }
                .addOnSuccessListener {
                    Logger.i("updateMyLocation addOnSuccessListener")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w(
                        "[${this::class.simpleName}] Error acceptInvite documents. ${it.message}"
                    )
                    continuation.resume(Result.Error(it))
                }
        }

    override fun getLiveCoworkingLocation(): MutableLiveData<List<UserLocation>> {
        val liveData = MutableLiveData<List<UserLocation>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_COWORKING_LOCATION)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("getLiveCoworkingLocation addSnapshotLister success")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<UserLocation>()
                if (snapshot != null) {
                    for (document in snapshot) {
                        Logger.d(document.id + " => " + document.data)
        
                        val userLocation = document.toObject(UserLocation::class.java)
                        list.add(userLocation)
                    }
                }
                liveData.value = list
            }
        return liveData
    }

    override suspend fun uploadImage(localUri: Uri): Result<Uri> =
        suspendCoroutine { continuation ->

            val time = Calendar.getInstance().timeInMillis
            val fileName = "${UserManager.userName}&$time"
            val storageReference = FirebaseStorage.getInstance().reference.child(fileName)

            val uploadTask = storageReference.putFile(localUri)

            uploadTask.continueWith { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageReference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.addOnSuccessListener {
                        continuation.resume(Result.Success(it))
                    }
                } else {
                    Logger.d(task.exception.toString())
                }
            }
        }

    override suspend fun addFavoritePlan(plan: Plan): Result<Boolean> =
        suspendCoroutine { continuation ->

            FirebaseFirestore.getInstance()
                .collection(PATH_USERS)
                .document(UserManager.userUID.toString())
                .collection(PATH_FAVORITE)
                .document(plan.id.toString())
                .set(plan)
                .addOnSuccessListener {
                    Logger.i("addFavoritePlan: $it")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w(
                        "[${this::class.simpleName}] Error addFavoritePlan documents. ${it.message}"
                    )
                    continuation.resume(Result.Error(it))
                }
        }

    override suspend fun cancelFavoritePlan(plan: Plan): Result<Boolean> =
        suspendCoroutine { continuation ->

            FirebaseFirestore.getInstance()
                .collection(PATH_USERS)
                .document(UserManager.userUID.toString())
                .collection(PATH_FAVORITE)
                .document(plan.id.toString())
                .delete()
                .addOnSuccessListener {
                    Logger.i("addFavoritePlan: $it")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w(
                        "[${this::class.simpleName}] Error addFavoritePlan documents. ${it.message}"
                    )
                    continuation.resume(Result.Error(it))
                }
        }

    override fun getLiveFavoritePlan(): MutableLiveData<List<Plan>> {
        val liveData = MutableLiveData<List<Plan>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_USERS)
            .document(UserManager.userUID.toString())
            .collection(PATH_FAVORITE)
            .addSnapshotListener { snapshot, exception ->

                Logger.i("getLiveCoWorkLocation addSnapshotLister success")

                exception?.let {
                    Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = mutableListOf<Plan>()
                if (snapshot != null) {
                    for (document in snapshot) {
                        Logger.d(document.id + " => " + document.data)
        
                        val plan = document.toObject(Plan::class.java)
                        list.add(plan)
                    }
                }
                liveData.value = list
            }
        return liveData
    }
}
