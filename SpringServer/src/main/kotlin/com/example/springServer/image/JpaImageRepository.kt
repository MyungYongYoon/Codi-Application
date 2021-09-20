package com.example.springServer.image


import org.springframework.data.jpa.repository.JpaRepository

interface JpaImageRepository : JpaRepository<Image, String>, ImageRepository {

}
