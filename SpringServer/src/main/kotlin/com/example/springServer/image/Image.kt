package com.example.springServer.image


import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Image(
    @Id val uuid : String,
    val userId : String,
    val saveTime : String,
    val styleInfo : String,
    val fileName : String,
    val originalFileName : String,
    val filePath : String
){
    fun changeToImageModel() : ImageModel = ImageModel(uuid, userId, saveTime, styleInfo, fileName, originalFileName, filePath)
}

/*
drop table if exists image CASCADE;
create table image
(
uuid varchar(255) unique,
user_id varchar(255) not null,
save_time varchar(255) not null,
style_info varchar(255) not null,
file_name varchar(255) not null,
original_file_name varchar(255) not null,
file_path varchar(255) not null,
primary key (uuid)
);
*/

