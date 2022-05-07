package com.shihs.tripmood

import android.app.Application
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.util.ServiceLocator
import kotlin.properties.Delegates

/**
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 */
class TripMoodApplication : Application() {

    // Depends on the flavor,
    val repository: TripMoodRepo
        get() = ServiceLocator.provideRepository(this)

    companion object {
        var instance: TripMoodApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun isLiveDataDesign() = true
}
