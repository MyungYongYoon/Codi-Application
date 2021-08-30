package com.example.golladreamclient.data.model

import com.example.golladreamclient.data.entity.User

data class UserModel(
    val id: String = "",
    val name: String = "",
    val birth: String = "",
    val sex: String = ""
){
    fun getUserEntity() : User { return User(id, name, birth, sex) }
}