package com.example.springServer.user

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
data class User (
    @Id var id : String = "",
    var pwd : String = "",
    var birth : String = "",
    var name : String = "",
    var sex : String = "",
    var height : String = "",
    var weight : String = ""){
    fun changeToUserModel() : UserModel = UserModel(name, birth, height, weight, sex, id, pwd)
}


/*
drop table if exists user CASCADE;
create table user
(
id varchar(255) unique,
pwd varchar(255) not null,
birth varchar(255) not null,
name varchar(255) not null,
sex varchar(255) not null,
height varchar(255) not null,
weight varchar(255) not null,
primary key (id)
);*/
