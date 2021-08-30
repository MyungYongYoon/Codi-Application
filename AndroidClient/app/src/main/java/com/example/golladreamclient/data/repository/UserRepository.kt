package com.example.golladreamclient.data.repository

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.example.golladreamclient.data.AppDatabase
import com.example.golladreamclient.data.model.UserModel
import io.reactivex.Completable
import io.reactivex.Single


class UserRepository(appDatabase: AppDatabase) {

    companion object {
        private var sInstance: UserRepository? = null
        fun getInstance(database: AppDatabase): UserRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = UserRepository(database)
                    sInstance = instance
                    instance
                }
        }
    }

    private val userdataDao = appDatabase.userDao()
    private val SHARED_PREFERENCES_TOKEN = "GollaDream_client_token"
    private var storedToken: String? = null


    fun getUserToken(application : Application)  : String? {
        val sharedPreferences = application.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
        return if (storedToken != null) storedToken
        else {
            if (sharedPreferences.getString(SHARED_PREFERENCES_TOKEN , "") != "") {
                storedToken = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN , "")
                storedToken
            }else storedToken
        }
    }

    fun saveUserToken(token: String, context: Context){
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
        storedToken = token
        sharedPreferences.edit {
            putString(SHARED_PREFERENCES_TOKEN , token)
            apply()
        }
    }

    fun removeUserToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_TOKEN, Context.MODE_PRIVATE)
        storedToken = null
        sharedPreferences.edit { remove(SHARED_PREFERENCES_TOKEN ) }
    }

    fun getUserInfo() : Single<UserModel> {
        return Single.create<UserModel> { emitter ->
            val userEntityData = userdataDao.getUserData()
            try {
                if (userEntityData == null) emitter.onError(Throwable("Error of Getting UserInfo"))
                else emitter.onSuccess(userEntityData.getUserModel())
            }catch (e : Exception){
            }
        }
    }

    fun saveUserInfo(userData : UserModel) : Completable {
        return userdataDao.insertUserData(userData.getUserEntity())
    }

    fun deleteUserInfo(userId: String) : Completable {
        return Completable.create { emitter ->
            userdataDao.deleteUserData(userId)
            emitter.onComplete()
        }
    }


}