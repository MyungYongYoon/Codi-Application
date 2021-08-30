package com.example.springServer

import com.example.springServer.user.UserService
import org.springframework.beans.factory.annotation.Autowired

class ServerController {

    @Autowired
    private lateinit var userService : UserService

}