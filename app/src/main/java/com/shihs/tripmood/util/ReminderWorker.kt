package com.shihs.tripmood.util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ReminderWorker(context: Context, workerParameters: WorkerParameters) : Worker(
    context,
    workerParameters
) {

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}
