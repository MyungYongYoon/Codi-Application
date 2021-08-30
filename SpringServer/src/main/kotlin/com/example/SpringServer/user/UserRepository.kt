package com.example.springServer.user

interface UserRepository {
    fun save(user : User) : User
    fun findById(id : String) : User?
    fun findByIdAndPwd(id : String, pwd : String) : User?
    fun findByNameAndBirth(name : String, birth : String) : User?
    fun findByIdAndName(id : String, name : String) : User?
    //fun saveAll (places : ArrayList<Place>?)  // 구현 왜 안되는지 모르겠다.
}