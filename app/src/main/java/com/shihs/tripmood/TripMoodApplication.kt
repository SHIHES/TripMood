package com.shihs.tripmood

import android.app.Application
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.util.ServiceLocator
import kotlin.properties.Delegates

class TripMoodApplication : Application() {

    val repository: TripMoodRepo
            get() = ServiceLocator.provideRepo(this)

    companion object{
        var instance: TripMoodApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun isLiveDataDesign() = true
}