package com.example.springServer.user



data class UserModel(
    var name : String = "",
    var birth : String = "",
    var height : String = "",
    var weight : String = "",
    var sex : String = "",
    var id : String = "",
    var pwd : String = ""){
    fun changeToUser() : User = User(id, pwd, birth, name, sex, height, weight)
}

data class UserModelItem(val exist : Boolean, val user : UserModel?)

data class UserIdItem(val exist : Boolean, val userId : String?)

data class UserPwdItem(val exist : Boolean, val userPwd : String?)
