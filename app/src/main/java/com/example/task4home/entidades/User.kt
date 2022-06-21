package com.example.task4home.entidades

data class User(
    var email: String = "",
    var fullname: String = "",
    var username: String = "",
    var group: String = "",
    var groupAdmin: Boolean? = null,
    var onGroup: Boolean? = null,
    var imgUrl: String = ""
)