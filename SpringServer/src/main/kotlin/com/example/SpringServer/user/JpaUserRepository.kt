package com.example.springServer.user

import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRepository : JpaRepository<User, Int>, UserRepository{
    //커스텀할 DB Function만 추가하기.
}
