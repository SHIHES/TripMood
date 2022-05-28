package com.shihs.tripmood.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.shihs.tripmood.dataclass.source.DefaultTripMoodRepo
import com.shihs.tripmood.dataclass.source.TripMoodDataSource
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.dataclass.source.local.TripMoodLocalDataSource
import com.shihs.tripmood.dataclass.source.remote.TripMoodRemoteDataSource

/**
 * A Service Locator for the [TripMoodRepository].
 */
object ServiceLocator {

    @Volatile
    var repository: TripMoodRepo? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): TripMoodRepo {
        synchronized(this) {
            return repository
                ?: repository
                ?: createPublisherRepository(context)
        }
    }

    private fun createPublisherRepository(context: Context): TripMoodRepo {
        return DefaultTripMoodRepo(
            TripMoodRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): TripMoodDataSource {
        return TripMoodLocalDataSource(context)
    }
}
