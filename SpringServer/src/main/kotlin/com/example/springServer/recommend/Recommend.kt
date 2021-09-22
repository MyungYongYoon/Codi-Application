package com.example.springServer.recommend

import java.io.File

data class InputData (val id : String, val dateTime : String, val personalInfo : PersonalInfo, val styleInfo : String, val image : String)

data class PersonalInfo(val id: String = "",
                        val name: String = "",
                        val birth: String = "",
                        val height : String = "",
                        val weight : String = "",
                        val sex: String = "")

//--------------------------------------------------------------------------------------------------------------------------------------------


/*data class ResultData(val id : String, val dateTime : String, val personalInfo : PersonalInfo, val image : String, val recommendInfo : RecommendInfo)*/

data class ReceiverRecommendOutput(val type : RecommendResult, val data1 : String?, val data2 : String?)

data class ReceiverSaveInput(val exist : Boolean, val returnData : SaveResult?)

data class SaveResult (val id : String, val styleInfo: String, val imageName : String)

enum class RecommendResult{ NONE, ONE, TWO, NOT_EXIST, NOT_RIGHT, ERROR }

