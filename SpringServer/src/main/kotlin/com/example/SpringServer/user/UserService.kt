package com.example.springServer.user

import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class UserService {
    private val userRepository : UserRepository
    @Autowired
    constructor(userRepository : UserRepository){
        this.userRepository = userRepository
    }
}
