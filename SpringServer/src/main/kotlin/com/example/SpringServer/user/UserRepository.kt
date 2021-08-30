package com.example.springServer.user

interface UserRepository {
    fun save(place : User)  //꼭 반환 X
    //fun saveAll (places : ArrayList<Place>?)  // 구현 왜 안되는지 모르겠다.
}