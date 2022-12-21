package com.example.chat_app

data class Message(
    var word: String? = null,
    var fromUID: String ?= null,
    var toUID: String ?= null,
    var time: String ?= null
)
