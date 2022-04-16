package com.shihs.tripmood.dataclass.source.remote

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.source.TripMoodDataSource
import com.shihs.tripmood.util.Logger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.shihs.tripmood.dataclass.Result


object TripMoodRemoteDataSource : TripMoodDataSource {

    private const val PATH_PLANS = "plans"
    private const val KEY_STARTDATE = "startDate"


    override suspend fun getPlans(): Result<List<Plan>> = suspendCoroutine { continuation ->
        FirebaseFirestore.getInstance()
            .collection(PATH_PLANS)
            .orderBy(KEY_STARTDATE, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val list = mutableListOf<Plan>()
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

                Logger.i("Find addSnapshotLister")

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

    override suspend fun postPlan(plan: Plan): Result<Boolean> = suspendCoroutine{ continuation ->
        val plans = FirebaseFirestore.getInstance().collection(PATH_PLANS)
        val document = plans.document()

        plan.id = document.id

        document.set(plan).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                Logger.i("Publish: $plan")

                continuation.resume(Result.Success(true))
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

    override suspend fun delete(plan: Plan): Result<Boolean> = suspendCoroutine { continuation ->
        plan.id?.let {
            FirebaseFirestore.getInstance()
                .collection(PATH_PLANS)
                .document(it)
                .delete()
                .addOnSuccessListener {
                    Logger.i("Delete: $plan")

                    continuation.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    Logger.w("[${this::class.simpleName}] Error delete documents. ${it.message}")
                    continuation.resume(Result.Error(it))
                }
        }
    }
}