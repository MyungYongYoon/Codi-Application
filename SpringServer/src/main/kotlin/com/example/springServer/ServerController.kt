package com.example.springServer


import com.example.springServer.image.ImageModel
import com.example.springServer.image.ImageService
import com.example.springServer.recommend.ReceiverRecommendOutput
import com.example.springServer.recommend.SaveResult
import com.example.springServer.recommend.ReceiverSaveInput
import com.example.springServer.recommend.RecommendResult
import com.example.springServer.user.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.time.LocalDateTime
import java.util.*

@RestController
class ServerController {
    private val BASE_URL = "http:/192.168.200.155:8080"

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
                           @RequestParam("image") imageName : String) : ReceiverRecommendOutput {
        val detectionProcessBuilder = ProcessBuilder("cmd.exe", "/c", "E:",
            "&cd", "yolov5", "&python", "detect.py","--source", "../Codi_ML/Input/$imageName", "--name", userId, "--exist-ok", "--save-txt")

        try {
            val detectionProcess : Process = detectionProcessBuilder.start()
            val detectionReader = BufferedReader(InputStreamReader(detectionProcess.inputStream))
            println(detectionReader.readText())
            val detectionExitVal = detectionProcess.waitFor()
            if (detectionExitVal == 0){ println("Detecting Succeed!") }
            else { return ReceiverRecommendOutput(RecommendResult.ERROR, null, null) }


            val txtName = imageName.replace(".jpg", ".txt")
            val colorProcessBuilder = ProcessBuilder("cmd.exe", "/c", "mkdir", "E:\\Recommend_ML\\Input\\$userId",
                "&python", "E://RecommendModules/extract.py", "-a", "E://Codi_ML/Output/$userId/labels/$txtName",
                "-i", "E://Codi_ML/Input/$imageName", "-l", "E://RecommendModules//colors_label.txt",
                "-o", "E://Recommend_ML/Input/$userId/$txtName")


            val colorProcess : Process = colorProcessBuilder.start()
            val colorReader = BufferedReader(InputStreamReader(colorProcess.inputStream))
            println(colorReader.readText())
            val colorExitVal = colorProcess.waitFor()
            if (colorExitVal == 0){ println("Get Color Succeed!") }
            else { return ReceiverRecommendOutput(RecommendResult.ERROR, null, null) }


            val searchProcessBuilder = ProcessBuilder("cmd.exe", "/c", "mkdir", "E:\\Recommend_ML\\Output\\$userId",
                "&python", "E://RecommendModules/search.py", "--info",
                "E://Recommend_ML/Base/$styleInfo/info.txt", "--input", "E://Recommend_ML/Input/$userId/$txtName",
                "--output", "E://Recommend_ML/Output/$userId/$txtName")
            val searchProcess : Process = searchProcessBuilder.start()
            val searchReader = BufferedReader(InputStreamReader(searchProcess.inputStream))
            println(searchReader.readText())
            val searchExitVal = searchProcess.waitFor()
            if (searchExitVal == 0){ println("Searching Succeed!") }
            else { return ReceiverRecommendOutput(RecommendResult.ERROR, null, null) }

            val resultTextFile = File("E://Recommend_ML/Output/$userId/$txtName")
            val resultTextFileReader : FileReader = FileReader(resultTextFile)
            val resultTextList : List<String> = resultTextFileReader.readLines()
            val resultImageBasePath = "$styleInfo/"

            when (resultTextList[0]){
                "-1"-> { return ReceiverRecommendOutput(RecommendResult.NOT_RIGHT, null, null) }
                "1" -> { return ReceiverRecommendOutput(RecommendResult.NONE, null, null) }
                "0" -> {
                    when (resultTextList.size-1){
                        0 -> { return ReceiverRecommendOutput(RecommendResult.NOT_EXIST, null, null) }
                        1 -> {
                            val fileName = resultTextList[1]
                            val fileDir = resultImageBasePath + fileName
                            val finalFile = File("E://Recommend_ML/Base/$fileDir")
                            return if (finalFile.exists()){
                                ReceiverRecommendOutput(RecommendResult.ONE, "$BASE_URL/getFiles/$fileDir", null)
                            }else ReceiverRecommendOutput(RecommendResult.ERROR, null, null)
                        }
                        2 -> {
                            val fileName1 =resultTextList[1]
                            val fileName2 =resultTextList[2]
                            val fileDir1 = resultImageBasePath + fileName1
                            val fileDir2 = resultImageBasePath + fileName2
                            val finalFile1 = File("E://Recommend_ML/Base/$fileDir1")
                            val finalFile2 = File("E://Recommend_ML/Base/$fileDir2")
                            return if (finalFile1.exists() && finalFile2.exists())
                                ReceiverRecommendOutput(RecommendResult.TWO,"$BASE_URL/getFiles/$fileDir1", "$BASE_URL/getFiles/$fileDir2")
                            else ReceiverRecommendOutput(RecommendResult.ERROR, null, null)
                        }
                        else -> { return ReceiverRecommendOutput(RecommendResult.ERROR, null, null) }
                    }
                }
                else -> { return ReceiverRecommendOutput(RecommendResult.ERROR, null, null) }
            }

        }catch (e : IOException) {
            println(e)
            return ReceiverRecommendOutput(RecommendResult.ERROR, null, null)
        }
    }

}

/*
@GetMapping("recommend/output")
fun getRecommendOutput(@RequestParam("id")userId : String, @RequestParam("style")styleInfo : String,
                       @RequestParam("image") imageName : String) : ReceiverRecommendOutput {  //: ReceiverRecommendOutput
    */
/*//*
/석용쓰
    try {
        val txtName = imageName.replace(".jpg", ".txt")
        val processBuilder2 = ProcessBuilder("cmd.exe", "/c", "mkdir", "E:\\Recommend_ML\\Input\\$userId",
            "&python", "E://RecommendModules/extract.py", "-a", "E://Codi_ML/Output/$userId/labels/$txtName",
            "-i", "E://Codi_ML/Input/$imageName", "-l", "E://RecommendModules//colors_label.txt",
            "-o", "E://Recommend_ML/Input/$userId/$txtName")
        val process2 : Process =  processBuilder2.start()
        val reader = BufferedReader(InputStreamReader(process2.inputStream))
        println(reader.readText())
        val searchExitVal = process2.waitFor()
        if (searchExitVal != 0){ println("Searching Succeed!") }
        else {
            //return ReceiverRecommendOutput(RecommendResult.ERROR, null, null)
        }
    }catch (e : IOException) {
        println(e)
        // return ReceiverRecommendOutput(RecommendResult.ERROR, null, null)
    }*//*


    //명성쓰
    try {
        val processBuilder2 = ProcessBuilder("cmd.exe", "/c", "mkdir", "E:\\Recommend_ML\\Output\\$userId",
            "&python", "E://RecommendModules/search.py", "--info",
            "E://Recommend_ML/Base/레트로/info.txt", "--input", "E://Recommend_ML/Input/$userId/test.txt",
            "--output", "E://Recommend_ML/Output/$userId/test2.txt")
        val process2 : Process =  processBuilder2.start()
        val reader = BufferedReader(InputStreamReader(process2.inputStream))
        println(reader.readText())
        val searchExitVal = process2.waitFor()
        if (searchExitVal != 0){ println("Searching Succeed!") }
        else {
            //return ReceiverRecommendOutput(RecommendResult.ERROR, null, null)
        }

        val resultTextFile = File("E://Recommend_ML/Output/$userId/test2.txt")
        val resultTextFileReader : FileReader = FileReader(resultTextFile)
        val resultTextList : List<String> = resultTextFileReader.readLines()
        val resultImageBasePath = "$styleInfo/"
        when (resultTextList[0]){
            "-1"-> {
                return ReceiverRecommendOutput(RecommendResult.NOT_RIGHT, null, null)
            }"1" -> {
            return ReceiverRecommendOutput(RecommendResult.NONE, null, null)
        }"0" -> {
            when (resultTextList.size-1){
                0 -> {
                    return ReceiverRecommendOutput(RecommendResult.NOT_EXIST, null, null)
                }
                1 -> {
                    */
/* val fileName = resultTextList[1]
                     val fileDir = resultImageBasePath + fileName
                     val fis = FileInputStream(fileDir)
                     val fos = ByteArrayOutputStream()
                     var readCount = 0
                     val buffer = ByteArray(1024)
                     var fileArray : ByteArray? = null

                     while ((readCount = fis.read(buffer)) > 0)*//*


                    val fileName = resultTextList[1]
                    val fileDir = resultImageBasePath + fileName
                    val finalFile = File("E://Recommend_ML/Base/$fileDir")


                    return if (finalFile.exists()){
                        ReceiverRecommendOutput(RecommendResult.ONE, "$BASE_URL/getFiles/$fileDir", null)
                    }else ReceiverRecommendOutput(RecommendResult.ERROR, null, null)
                }
                2 -> {
                    */
/*val fileName1 =resultTextList[1]
                    val fileName2 =resultTextList[2]
                    val fileDir1 = resultImageBasePath + fileName1
                    val fileDir2 = resultImageBasePath + fileName2
                   return ReceiverRecommendOutput(RecommendResult.TWO,  , null)*//*

                    return ReceiverRecommendOutput(RecommendResult.ERROR, null, null)
                }
                else -> {
                    return ReceiverRecommendOutput(RecommendResult.ERROR, null, null)
                }
            }
        }else -> {
            return ReceiverRecommendOutput(RecommendResult.ERROR, null, null)
        }
        }

    }catch (e : IOException) {
        println(e)
        return ReceiverRecommendOutput(RecommendResult.ERROR, null, null)
    }
}
*/
 */
