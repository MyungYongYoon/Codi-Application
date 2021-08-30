package com.example.golladreamclient.data

import com.example.golladreamclient.data.model.ReceiverUser
import com.example.golladreamclient.data.model.ReceiverUserItem
import com.example.golladreamclient.data.model.ReceiverUserIdItem
import com.example.golladreamclient.data.model.ReceiverUserPwdItem
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServerAPI {

    companion object {
        private const val BASE_URL = "http:/192.168.25.37:8080"   //TODO : (Linking change) local -> remote.

        fun create(): ServerAPI {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val headerInterceptor = Interceptor {
                val request = it.request().newBuilder().build()
                return@Interceptor it.proceed(request)
            }
            val client = OkHttpClient.Builder().addInterceptor(headerInterceptor)
                .addInterceptor(httpLoggingInterceptor).build()
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build()
            return retrofit.create(ServerAPI::class.java)
        }
    }

    @GET("/user/check/id")
    fun checkIdUsable(@Query("id") id: String): Single<Boolean>


    @POST("/user/register")
    fun registerUser(@Body body: ReceiverUser?) : Single<ReceiverUser>

    @GET("/user/check/user")
    fun checkUserInfo(@Query("id")id : String, @Query("pwd")pwd : String) : Single<ReceiverUserItem>

    @GET("/user/find/id")
    fun findUserId(@Query("name") name : String, @Query("birth") birth : String) : Single<ReceiverUserIdItem>

    @GET("/user/find/pwd")
    fun findUserPwd(@Query("name") name : String, @Query("id") id : String) : Single<ReceiverUserPwdItem>

}