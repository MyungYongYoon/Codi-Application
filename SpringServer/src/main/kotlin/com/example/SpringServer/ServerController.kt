package com.example.springServer

import com.example.springServer.user.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ServerController {

    @Autowired
    private lateinit var userService : UserService

  /*  @GetMapping("/user/check/id")
    fun checkIdUsable(@RequestParam("id") id: String) : Boolean {
        return userService.checkIdUsable(id)
    }*/

    @PostMapping("/user/register")
    fun registerUser(@RequestBody body: UserModel) : UserModel{
        return userService.registerUser(body)
    }
    @GetMapping("/user/check/id")
    fun checkIdExist(@RequestParam("id") id: String) : Boolean {
        return userService.checkIdExist(id)
    }
    @GetMapping("/user/check/user")
    fun checkUserInfo(@RequestParam("id") id : String, @RequestParam("pwd") pwd : String) : UserModelItem {
        return userService.checkUserInfo(id, pwd)
    }
    @GetMapping("/user/find/id")
    fun findUserId(@RequestParam("name") name : String, @RequestParam("birth") birth : String) : UserIdItem {
        return userService.findUserId(name, birth)
    }
    @GetMapping("/user/find/pwd")
    fun findUserPwd(@RequestParam("name") name : String, @RequestParam("id") id : String) : UserPwdItem {
        return userService.findUserPwd(name, id)
    }
}