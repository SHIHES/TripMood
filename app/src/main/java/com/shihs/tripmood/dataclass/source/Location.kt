package com.shihs.tripmood.dataclass.source

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Location (
    var latitude: Double? = null,
    var longitude: Double? = null,
    var name: String? = null
):Parcelable