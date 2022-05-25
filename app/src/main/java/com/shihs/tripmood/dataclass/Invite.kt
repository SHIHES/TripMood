package com.shihs.tripmood.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Invite(
    var id: String? = null,
    var invitePlanID: String? = "",
    var invitePlanTitle: String? = "",
    var senderName: String? = "",
    var senderID: String? = "",
    var senderPhotoUrl: String? = "",
    var receiverName: String? = "",
    var receiverID: String? = "",
    var receiverPhotoUrl: String? = "",
    var status: Int = 0
) : Parcelable
