package com.shihs.tripmood.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Plan(
    var title: String? = null,
    var startDate: Long? = null,
    var endDate: Long? = null,
    var id: String? = null,
    var schedules: List<Schedule>? = null
) : Parcelable

@Parcelize
data class Schedule(
    var events: MutableList<Event>? = null,
    var date: Long? = null
) : Parcelable


@Parcelize
data class Event(
    var time: String? = null,
    var title: String? = null,
    var note: String? = null,
    var expand: Boolean = false
)  : Parcelable
