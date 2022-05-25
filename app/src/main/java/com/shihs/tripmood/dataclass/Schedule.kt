package com.shihs.tripmood.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Schedule(
    var time: Long? = null,
    var title: String? = "",
    var note: String? = "",
    var scheduleId: String? = "",
    var planID: String? = "",
    var cost: String? = "",
    var catalog: String? = "",
    var location: Location? = null,
    var notification: Boolean = false,
    var theDay: Int = -1
) : Parcelable
