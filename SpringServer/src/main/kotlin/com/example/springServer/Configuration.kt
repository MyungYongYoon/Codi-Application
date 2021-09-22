package com.example.springServer


import com.example.springServer.image.ImageRepository
import com.example.springServer.image.ImageService
import com.example.springServer.user.UserRepository
import com.example.springServer.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer




@Configuration
class Configuration {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var imageRepository: ImageRepository

    @Bean
    fun userService() : UserService {
        return UserService(userRepository)
    }
    @Bean
    fun imageService() : ImageService {
        return ImageService(imageRepository)
    }
}

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/getFiles/**")
            .addResourceLocations("file:///E:/Recommend_ML/Base/")
    }
}