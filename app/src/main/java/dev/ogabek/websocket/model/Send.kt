package dev.ogabek.websocket.model

data class Send(
    val event: String,
    val data: Data
)

data class Data(
    val channel: String
)