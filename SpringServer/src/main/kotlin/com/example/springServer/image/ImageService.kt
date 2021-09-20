package com.example.springServer.image


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Transactional
class ImageService {
    private val imageRepository : ImageRepository

    @Autowired
    constructor(imageRepository : ImageRepository){
        this.imageRepository = imageRepository
    }

    fun saveImage(imageData : ImageModel) : ImageModel {
        return imageRepository.save(imageData.changeToImage()).changeToImageModel()
    }
}