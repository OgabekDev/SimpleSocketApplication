package dev.ogabek.websocket.model

data class Respond(
    val channel: String?,
    val data: DataForRespond?,
    val event: String?
)