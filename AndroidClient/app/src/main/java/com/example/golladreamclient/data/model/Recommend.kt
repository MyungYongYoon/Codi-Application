package com.example.golladreamclient.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

data class InputData (val id : String, val dateTime : String, val personalInfo : PersonalInfo, val styleInfo : String, val image : File)

data class PersonalInfo(val id: String = "",
                        val name: String = "",
                        val birth: String = "",
                        val height : String = "",
                        val weight : String = "",
                        val sex: String = "")

//--------------------------------------------------------------------------------------------------------------------------------------------

/*data class ResultData(val id : String, val dateTime : String, val personalInfo : PersonalInfo, val image : String, val recommendInfo : RecommendInfo)*/

data class ReceiverRecommendOutput(val type : RecommendResult, val data1 : String?, val data2 : String?)

data class ReceiverSaveInput(val exist : Boolean, val returnData : InputItem)

data class InputItem (val id : String, val styleInfo: String, val imageName : String)

@Parcelize
enum class RecommendResult : Parcelable { NONE, ONE, TWO, NOT_EXIST, NOT_RIGHT, ERROR }
