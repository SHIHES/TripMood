package com.shihs.tripmood.dataclass

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Location (
    var latitude: Double? = null,
    var longitude: Double? = null,
    var name: String? = null,
    var address: String? = null,
    var type: List<String>? = null,
    var image: String? = null,
    var icon: String? = null,
    var rating: Double? = null
):Parcelable