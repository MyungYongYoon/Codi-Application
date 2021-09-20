package com.example.springServer.image


interface ImageRepository {
    fun save(Image : Image) : Image
}