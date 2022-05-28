package com.shihs.tripmood.dataclass

data class UserLocation(
    var lat: Double? = null,
    var lng: Double? = null,
    var userUID: String? = null,
    var userPhotoUrl: String? = null,
    var userName: String? = null
)
