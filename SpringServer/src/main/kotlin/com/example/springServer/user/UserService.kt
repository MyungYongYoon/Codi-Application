package com.example.springServer.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestParam
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/*  성별 : 남/여
   신장 : 150미만 /150~160 / 160~170/ 170~180/ 180~190/ 190이상
   체중 : 40미만/ 40~50/ 50~60/ 60~70/ 70~80/ 80~90/ 90~100/ 100이상*/

@Transactional
class UserService {
    private val userRepository : UserRepository

    @Autowired
    constructor(userRepository : UserRepository){
        this.userRepository = userRepository
    }
    fun findUserId(name : String, birth : String ) : UserIdItem {
        return when (val user = userRepository.findByNameAndBirth(name, birth)){
            null -> UserIdItem(false, null)
            else -> UserIdItem(true, user.id)
        }
    }
    fun findUserPwd(name : String, id : String) : UserPwdItem {
        return when (val user = userRepository.findByIdAndName(id, name)){
            null -> UserPwdItem(false, null)
            else -> UserPwdItem(true, user.pwd)
        }
    }
    fun checkIdExist(id : String) : Boolean{
        return when (userRepository.findById(id)){
            null -> true
            else -> false
        }
    }
    fun checkUserInfo(id : String, pwd : String) : UserModelItem {
        return when (val user = userRepository.findByIdAndPwd(id, pwd)){
            null -> UserModelItem(false, null)
            else -> UserModelItem(true, user.changeToUserModel())
        }
    }
    fun registerUser(user : UserModel) : UserModel {
        return userRepository.save(user.changeToUser()).changeToUserModel()
    }
    fun withdrawalUser(id : String) : Boolean {
        return when (userRepository.findById(id)){
            null -> false
            else -> { userRepository.deleteById(id)
                true
            }
        }
    }

}
