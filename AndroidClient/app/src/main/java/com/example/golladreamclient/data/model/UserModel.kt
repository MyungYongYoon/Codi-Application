package com.example.golladreamclient.data.model

import android.os.Parcelable
import com.example.golladreamclient.data.entity.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    val id: String = "",
    val name: String = "",
    val birth: String = "",
    val height : String = "",
    val weight : String = "",
    val sex: String = ""
) : Parcelable {
    fun getUserEntity() : User { return User(id, name, birth, height, weight, sex) }
}