package com.shihs.tripmood.dataclass

import android.os.Parcelable
import com.shihs.tripmood.dataclass.source.Location
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Schedule(
    var time: Long? = null,
    var title: String? = null,
    var note: String? = null,
    var scheduleId: String? = null,
    var expand: Boolean? = false,
    var planID: String? = null,
    var location: Location? = null,
) : Parcelable
