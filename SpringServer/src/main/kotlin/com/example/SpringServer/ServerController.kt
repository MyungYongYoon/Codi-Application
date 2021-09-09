package com.example.springServer

import com.example.springServer.recommend.InputData
import com.example.springServer.recommend.OutputData
import com.example.springServer.recommend.RecommendInfo
import com.example.springServer.user.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import retrofit2.http.Body
import java.time.LocalDateTime

@RestController
class ServerController {

    @Autowired
    private lateinit var userService : UserService

    @GetMapping("/user/find/id")
    fun findUserId(@RequestParam("name") name : String, @RequestParam("birth") birth : String) : UserIdItem {
        return userService.findUserId(name, birth)
    }
    @GetMapping("/user/find/pwd")
    fun findUserPwd(@RequestParam("name") name : String, @RequestParam("id") id : String) : UserPwdItem {
        return userService.findUserPwd(name, id)
    }
    @GetMapping("/user/check/id")
    fun checkIdExist(@RequestParam("id") id: String) : Boolean {
        return userService.checkIdExist(id)
    }
    @GetMapping("/user/check/user")
    fun checkUserInfo(@RequestParam("id") id : String, @RequestParam("pwd") pwd : String) : UserModelItem {
        return userService.checkUserInfo(id, pwd)
    }
    @PostMapping("/user/register")
    fun registerUser(@RequestBody body: UserModel) : UserModel{
        return userService.registerUser(body)
    }
    @GetMapping("/user/withdrawal")
    fun withdrawalUser(@RequestParam("id") id : String) : Boolean {
        return userService.withdrawalUser(id)
    }

    @PostMapping("recommend/input")
    fun makeRecommend(@Body data: InputData): OutputData { //TODO : 머신러닝 모델들이랑 연결하기.
        return OutputData(data.id, LocalDateTime.now().toString(), data.personalInfo, data.image, RecommendInfo())
    }

}