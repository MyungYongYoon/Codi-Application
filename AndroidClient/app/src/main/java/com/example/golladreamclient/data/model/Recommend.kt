package com.example.golladreamclient.data.model

data class InputData (val id : String, val dateTime : String, val personalInfo : PersonalInfo, val colorInfo : String, val image : String)

data class PersonalInfo(val id: String = "",
                        val name: String = "",
                        val birth: String = "",
                        val height : String = "",
                        val weight : String = "",
                        val sex: String = "")

data class OutputData(val id : String, val dateTime : String, val personalInfo : PersonalInfo, val image : String, val recommendInfo : RecommendInfo)

//TODO
data class RecommendInfo(val test : String = "")