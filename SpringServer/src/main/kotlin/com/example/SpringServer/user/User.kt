package com.example.springServer.user

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var key: Long = 0L
    var id : String = ""
    var pwd : String = ""
    var birth : String = ""
    var name : String = ""
    var sex : String = ""
    var weight : Double = 0.0
    var height : Double = 0.0
}
