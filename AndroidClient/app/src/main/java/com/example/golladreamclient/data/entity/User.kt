package com.example.golladreamclient.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.golladreamclient.data.model.UserModel

@Entity(tableName = "user")
data class User(
    @PrimaryKey @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "birth")
    var birth: String = "",
    @ColumnInfo(name = "height")
    var height: String = "",
    @ColumnInfo(name = "weight")
    var weight: String = "",
    @ColumnInfo(name = "sex")
    var sex: String = ""
){
    fun getUserModel() : UserModel { return UserModel(id, name, birth, height, weight, sex) }
}