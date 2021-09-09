package com.example.golladreamclient.ui

fun makeBirthText(birth : String): String {
    var birthText : String = ""
    birthText += birth.subSequence(0 until 4)
    birthText += "/"
    birthText += birth.subSequence(4 until 6)
    birthText += "/"
    birthText += birth.subSequence(6 until 8)
    return birthText
}
fun makeHeightText(height : String): String {
    return when (height){
        "150미만대" -> "150 미만"
        "150대" -> "150 ~ 159"
        "160대" -> "160 ~ 169"
        "170대" -> "170 ~ 179"
        "180대" -> "180 ~ 189"
        "190이상대" -> "190 이상"
        else -> "알수없음"
    }
}
fun makeWeightText(weight : String): String {
    return when (weight){
        "40미만대" -> "40 미만"
        "40대" -> "40 ~ 49"
        "50대" -> "50 ~ 59"
        "60대" -> "60 ~ 69"
        "70대" -> "70 ~ 79"
        "80대" -> "80 ~ 89"
        "90대" -> "90 ~ 99"
        "100이상대" -> "100 이상"
        else -> "알수없음"
    }
}
fun getSexPosition(sex : String) : Int {
    return when(sex){
        "남자" -> 1
        "여자" -> 2
        else -> 0
    }
}

fun getHeightPosition(height : String) : Int {
    return when (height){
        "150미만대" -> 1
        "150대" -> 2
        "160대" -> 3
        "170대" -> 4
        "180대" -> 5
        "190이상대" -> 6
        else -> 0
    }
}

fun getWeightPosition(weight : String) : Int {
    return when (weight){
        "40미만대" -> 1
        "40대" -> 2
        "50대" -> 3
        "60대" -> 4
        "70대" -> 5
        "80대" -> 6
        "90대" -> 7
        "100이상대" -> 8
        else -> 0
    }
}