package com.example.springServer.image

data class ImageModel(
    val uuid : String,
    val userId : String,
    val saveTime : String,
    val styleInfo : String,
    val fileName : String,
    val originalFileName : String,
    val filePath : String
){
    fun changeToImage() : Image = Image(uuid, userId, saveTime, styleInfo, fileName, originalFileName, filePath)
}