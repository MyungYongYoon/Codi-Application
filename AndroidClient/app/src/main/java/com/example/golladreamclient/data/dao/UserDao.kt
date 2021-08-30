package com.example.golladreamclient.data.dao

import androidx.room.*
import com.example.golladreamclient.data.entity.User
import io.reactivex.Completable

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserData(userdata : User) : Completable

    @Query("DELETE from user WHERE id=:userId")
    fun deleteUserData(userId : String)

    @Query("SELECT * from user ORDER BY id DESC LIMIT 1 ")
    fun getUserData() : User?

    @Query("SELECT id from user ORDER BY id DESC LIMIT 1 ")
    fun getUserToken() : String?
}