package com.example.springServer


import com.example.springServer.image.ImageModel
import com.example.springServer.image.ImageService
import com.example.springServer.recommend.ReceiverRecommendOutput
import com.example.springServer.recommend.SaveResult
import com.example.springServer.recommend.ReceiverSaveInput
import com.example.springServer.user.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.util.*

@RestController
class ServerController {

    @Autowired
    private lateinit var userService : UserService
    @Autowired
    private lateinit var imageService: ImageService

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
    fun makeRecommend(@RequestParam("id") id : String, @RequestParam("style") style : String,
                      @RequestParam ("image") image : MultipartFile): ReceiverSaveInput {
        if (image.isEmpty) return ReceiverSaveInput(false, null)
        else {
            val uuid = UUID.randomUUID().toString()
            val saveTime = LocalDateTime.now().toString()
            val newName = id + "_" + uuid + "_" + image.originalFilename
            val newDestination = "E:\\Codi_ML\\Input\\$newName"
            val newFile = File(newDestination)
            var savedResult : ImageModel? = null
            try {
                image.originalFilename?.let {
                    image.transferTo(newFile)
                    val imageData = ImageModel(uuid, id, saveTime, style, newName, it, newDestination)
                    savedResult = imageService.saveImage(imageData)
                }
            } catch (e : IOException) {
                e.printStackTrace()
                return ReceiverSaveInput(false, null)
            }
            return if (savedResult==null) ReceiverSaveInput(false, null)
            else ReceiverSaveInput(true, SaveResult(id, style, savedResult!!.fileName))
        }
    }

    @GetMapping("recommend/output")
    fun getRecommendOutput(@RequestParam("id")userId : String, @RequestParam("style")styleInfo : String,
                           @RequestParam("image") imageName : String)  {  //: ReceiverRecommendOutput
        val processBuilder = ProcessBuilder("cmd.exe", "/c", "start",
        "& E:", "& cd", "yolov5", "& python detect.py --source ../Codi_ML/Input/$imageName --name ljy3237 --exist-ok")

        try {
            val process : Process = processBuilder.start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val line = reader.readText()
            println(line)

            val exitVal = process.waitFor()
            if (exitVal != 0){

            }else { //성공
                println("Success")
            }
        }catch (e : IOException) {

        }
    }

}