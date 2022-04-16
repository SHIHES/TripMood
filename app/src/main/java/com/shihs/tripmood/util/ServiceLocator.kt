package com.shihs.tripmood.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.shihs.tripmood.dataclass.source.TripMoodDataSource
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.dataclass.source.local.TripMoodLocalDataSource
import com.shihs.tripmood.dataclass.source.remote.TripMoodRemoteDataSource
import com.shihs.tripmood.dataclass.source.repository.DefaultTripMoodRepo

object ServiceLocator {

    @Volatile
    var repository: TripMoodRepo? = null
    @VisibleForTesting set

    fun provideRepo(context: Context) : TripMoodRepo{
        synchronized(this){
            return repository
                ?: repository
                ?: createTripMoodRepo(context)
        }
    }
    private fun createTripMoodRepo(context: Context): TripMoodRepo{
        return DefaultTripMoodRepo(
            TripMoodRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): TripMoodDataSource{
        return TripMoodLocalDataSource(context)
    }
}