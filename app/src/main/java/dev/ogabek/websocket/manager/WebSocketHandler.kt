package dev.ogabek.websocket.manager

import okhttp3.Response
import okio.ByteString

interface WebSocketHandler {
    fun onMessage(text: String)
    fun onMessage(byte: ByteString)
    fun onClosing(code: Int, reason: String)
    fun onFailure(t: Throwable, respond: Response?)
}