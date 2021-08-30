package com.example.golladreamclient.data.model

data class ReceiverUser (
    var name : String = "",
    var birth : String = "",
    var height : String = "",
    var weight : String = "",
    var sex : String = "",
    var id : String = "",
    var pwd : String = ""
){
    fun getUserModel() : UserModel = UserModel(id, name, birth, height, weight, sex)
}

data class ReceiverUserItem(val exist : Boolean, val user : ReceiverUser?)

data class ReceiverUserIdItem(val exist : Boolean, val userId : String?)

data class ReceiverUserPwdItem(val exist : Boolean, val userPwd : String?)