package com.example.springServer


import com.example.springServer.user.UserRepository
import com.example.springServer.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean

class Configuration {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Bean
    fun userService() : UserService {
        return UserService(userRepository)
    }
}