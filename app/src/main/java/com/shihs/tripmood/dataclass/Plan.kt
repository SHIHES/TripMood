package com.shihs.tripmood.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Plan(
    var title: String? = null,
    var startDate: Long? = null,
    var endDate: Long? = null
) : Parcelable
